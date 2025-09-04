package org.banking.transaction.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Getter
@Setter
public class TransactionDeleteVO implements Serializable {
    @NotBlank(message = "User ID is required")
    @Size(max = 100, message = "User ID must not exceed 100 characters")
    private String userUid;  // 用户uid

    @NotBlank(message = "Reference ID is reqired")
    @Size(max = 150, message = "Reference ID must not exceed 50 characters")
    private String referenceId; // 唯一交易编号（如订单号、流水号）
}
