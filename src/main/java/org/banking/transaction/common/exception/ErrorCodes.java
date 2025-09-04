package org.banking.transaction.common.exception;

public enum ErrorCodes {
    TRANSACTION_ALREADY_EXISTS("TX-001", "Transaction ID already exists"),
    TRANSACTION_NOT_FOUND("TX-002", "Transaction not found"),
    TRANSACTION_STATUS_NOT_ALLOWED("TX-003", "current transaction status is not allowed modify");

    private final String code;
    private final String message;

    ErrorCodes(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
