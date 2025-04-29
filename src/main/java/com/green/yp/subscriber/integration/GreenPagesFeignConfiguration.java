package com.green.yp.subscriber.integration;

import feign.Logger;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Slf4j
public class GreenPagesFeignConfiguration {

    @Value("${greenyp.backend.apiKey:}")
    protected String apiKey;

    @Bean
    Logger.Level feignLoggerLevel() {
        log.debug("Setting up Logger Level FULL");
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        log.info("Initialize requestInterceptor");
        return requestTemplate -> {
            if (StringUtils.isNotBlank(apiKey)) {
                requestTemplate.header("API-Key", apiKey);
            }
            requestTemplate.header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            requestTemplate.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        };
    }

    @Bean
    public DefaultFeignErrorDecoder decoder() {
        log.info("Initialize DefaultFeignErrorDecoder");
        return new DefaultFeignErrorDecoder();
    }
}
