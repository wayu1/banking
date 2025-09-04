package org.banking.transaction.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.banking.transaction.common.constants.TransactionType;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Getter
@Setter
public class TransactionCreateVO implements Serializable {
    @NotBlank(message = "User ID is required")
    @Size(max = 100, message = "User ID must not exceed 100 characters")
    private String userUid;  // 用户uid

    @NotBlank(message = "Reference ID is required")
    @Size(max = 150, message = "Reference ID must not exceed 50 characters")
    private String referenceId; // 唯一交易编号（如订单号、流水号）

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotBlank(message = "Transaction type is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private TransactionType type; // 如: DEPOSIT, WITHDRAW, TRANSFER, REFUND

    @Size(max = 200)
    private String description;
}
