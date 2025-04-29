package com.green.yp.subscriber.integration;

import com.green.yp.api.apitype.common.ErrorCodeType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ViewBusinessException extends RuntimeException {
    private final ErrorCodeType errorCodeType;
    private final String displayMessage;

    public ViewBusinessException(ErrorCodeType errorCodeType, String displayMessage) {
        super(displayMessage);
        this.errorCodeType = errorCodeType;
        this.displayMessage = displayMessage;

    }
}
