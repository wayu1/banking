package org.banking.transaction.common.exception;

public class BusinessException extends RuntimeException {
    private final String code;

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCodes errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCodes errorCode, String customMessage) {
        super(customMessage);
        this.code = errorCode.getCode();
    }

    public String getCode() {
        return code;
    }
}
