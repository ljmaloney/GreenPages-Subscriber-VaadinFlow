package com.green.yp.subscriber.integration;

import com.green.yp.api.apitype.common.ErrorCodeType;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.ReadContext;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpStatus;

@Slf4j
public class DefaultFeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        log.debug("Decode error response for methodKey: {} - response: {}", methodKey, response);
        try {
            if (response != null) {
                String requestBody = getRequestBody(response);
                int status = response.status();
                if (response.body() != null) {

                    String content =
                            IOUtils.toString(response.body().asInputStream(), Charset.defaultCharset());

                    log.error(
                            "Request is failed. status: {} requestUrl: {}, requestBody: {}, responseBody: {}",
                            status,
                            response.request().url(),
                            requestBody,
                            content);
                    if (isErrorPayload(content)) {
                        return handleErrorPayload(response, status, content);
                    }
                } else {
                    return handleError(methodKey, requestBody, response.request().url(), status);
                }
            }
            return new ViewBusinessException(ErrorCodeType.RPC_CALL_FAILED,
                    "A remote system call failed, and we have been notified. Please try again later");
        } catch (IOException e) {
            log.error("{}: {}", e.getMessage(), e);
            return new ViewBusinessException(ErrorCodeType.RPC_CALL_FAILED,
                    "A remote system call failed, and we have been notified. Please try again later");
        }
    }

    private Exception handleError(String methodKey, String requestBody, String url, int httpStatus) {
        return switch (httpStatus) {
            case HttpStatus.SC_NOT_FOUND -> new ViewBusinessException(ErrorCodeType.RPC_CALL_FAILED,
                    "The remote server could not find the requested resource, and we have been notified. Please try again later");
            default -> new ViewBusinessException(ErrorCodeType.RPC_CALL_FAILED,
                    "A remote system call failed, and we have been notified. Please try again later");
        };
    }

    private Exception handleErrorPayload(Response response, int httpStatus, String responseContent) {
        ReadContext ctx = JsonPath.parse(responseContent);
        String errorCode = ctx.read("$.errorCode");
        String errorMessage = ctx.read("$.errorMessage");

        return new ViewBusinessException(ErrorCodeType.findErrorCode(errorCode), errorMessage);
    }

    protected boolean isErrorPayload(String responseContent) {
        try {
            ReadContext ctx = JsonPath.parse(responseContent);
            ctx.read("$.errorCode");
            ctx.read("$.errorMessage");
        } catch (PathNotFoundException nfe) {
            return false;
        }
        return true;
    }

    String getRequestBody(Response response) throws UnsupportedEncodingException {
        String requestBody = "<no request body>";
        if (ArrayUtils.isNotEmpty(response.request().body())) {
            requestBody = new String(response.request().body(), StandardCharsets.UTF_8);
        }
        return requestBody;
    }
}
