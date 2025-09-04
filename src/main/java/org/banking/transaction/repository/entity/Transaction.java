package org.banking.transaction.repository.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.banking.transaction.common.constants.TransactionStatus;
import org.banking.transaction.common.constants.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "reference_id")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank(message = "User ID is required")
    @Size(max = 100, message = "User ID must not exceed 100 characters")
    @Column(name = "user_uid", length = 100, nullable = false)
    private String userUid;  // 用户uid

    @NotBlank(message = "Reference ID is required")
    @Size(max = 150, message = "Reference ID must not exceed 150 characters")
    @Column(name = "reference_id", length = 150, nullable = false, unique = true)
    private String referenceId; // 唯一交易编号（如订单号、流水号）

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    @Column(name = "amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "type", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type; // 如: DEPOSIT, WITHDRAW, TRANSFER, REFUND

    @Column(name = "status", length = 20,nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status; // SUCCESS, FAILED, PENDING,DELETED

    @Size(max = 200)
    @Column(name = "description")
    private String description;

    @NotNull(message = "Timestamp is required")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // JPA 更新时间处理（可在 Repository 或 Service 中手动设置）
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
