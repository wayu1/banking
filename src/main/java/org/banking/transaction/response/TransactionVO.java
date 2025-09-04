package org.banking.transaction.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.banking.transaction.common.constants.TransactionStatus;
import org.banking.transaction.common.constants.TransactionType;

import java.io.Serializable;

@Data
@Getter
@Setter
public class TransactionVO implements Serializable {
    private String userUid;  // 用户uid

    private String referenceId; // 唯一交易编号（如订单号、流水号）

    private String amount;

    private TransactionType type; // 如: DEPOSIT, WITHDRAW, TRANSFER, REFUND

    private TransactionStatus status; // SUCCESS, FAILED, PENDING,DELETED

    private String description;

    private Long createdAt;

    private Long updatedAt;
}
