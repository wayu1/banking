package org.banking.transaction.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ErrorResponse {
    private final String code;
    private final String message;
    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
