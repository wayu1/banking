package org.banking.transaction.repository;

import jakarta.transaction.Transactional;
import org.banking.transaction.common.constants.TransactionStatus;
import org.banking.transaction.repository.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // 根据 referenceId,userUid 查询交易（用于唯一性校验）
    @Query("SELECT t FROM Transaction t WHERE t.referenceId = :referenceId and t.userUid= :userUid and t.status!='DELETED'")
    Optional<Transaction> findByReferenceId(@Param("referenceId") String referenceId,
                                            @Param("userUid") String userUid);

    // 分页查询交易（支持排序和分页）
    @Query("SELECT t FROM Transaction t WHERE t.userUid=:userUid and t.status!='DELETED'")
    Page<Transaction> findAllWithPagination(Pageable pageable,@Param("userUid") String userUid);

    @Modifying
    @Transactional
    @Query("UPDATE Transaction t SET t.amount = :amount," +
            "t.description=:description," +
            "t.updatedAt=:updatedAt WHERE t.referenceId = :referenceId and t.userUid= :userUid and t.status!='DELETED'")
    void updateTransactionById(@Param("amount") BigDecimal amount,
                               @Param("description") String description,
                               @Param("updatedAt") LocalDateTime updatedAt,
                               @Param("referenceId") String referenceId,
            @Param("userUid") String userUid);

    @Modifying
    @Transactional
    @Query("UPDATE Transaction t SET " +
            "t.status=:status," +
            "t.updatedAt=:updatedAt WHERE t.referenceId = :referenceId and t.userUid= :userUid")
    void updateTransactionStatusById(
                               @Param("status") TransactionStatus status,
                               @Param("updatedAt") LocalDateTime updatedAt,
                               @Param("referenceId") String referenceId,
                               @Param("userUid") String userUid);

}