package org.banking.transaction;

import org.banking.transaction.common.PageRes;
import org.banking.transaction.common.constants.TransactionStatus;
import org.banking.transaction.common.constants.TransactionType;
import org.banking.transaction.common.exception.BusinessException;
import org.banking.transaction.common.exception.ErrorCodes;
import org.banking.transaction.repository.TransactionRepository;
import org.banking.transaction.repository.entity.Transaction;
import org.banking.transaction.request.TransactionCreateVO;
import org.banking.transaction.request.TransactionDeleteVO;
import org.banking.transaction.request.TransactionUpdateVO;
import org.banking.transaction.response.TransactionVO;
import org.banking.transaction.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class BankingApplicationTests {
    @InjectMocks
    private TransactionServiceImpl transactionService;
    @Mock
    private TransactionRepository transactionRepository;

    private TransactionCreateVO createVO;
    private TransactionUpdateVO updateVO;
    private TransactionDeleteVO deleteVO;

    private List<Transaction> transactions;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        this.createVO = new TransactionCreateVO();
        createVO.setUserUid("userId1");
        createVO.setAmount(new BigDecimal("100.00"));
        createVO.setDescription("Test");
        createVO.setReferenceId("txseq0001");
        createVO.setType(TransactionType.DEPOSIT);

        this.updateVO = new TransactionUpdateVO();
        this.updateVO.setUserUid("userId2");
        this.updateVO.setAmount(new BigDecimal("200.00"));
        this.updateVO.setDescription("Test2");
        this.updateVO.setReferenceId("txseq0002");

        this.deleteVO = new TransactionDeleteVO();
        this.deleteVO.setUserUid("userId3");
        this.deleteVO.setReferenceId("txseq0003");

        // 准备测试数据（共 25 条）
        this.transactions = IntStream.range(0, 25)
                .mapToObj(i -> {
                    Transaction t = new Transaction();
                    t.setId((long) i);
                    t.setReferenceId("REF-" + i);
                    t.setUserUid("userId1");
                    BigDecimal amount = new BigDecimal(100 + i)
                            .setScale(2, RoundingMode.HALF_UP);
                    t.setAmount(amount);
                    t.setUpdatedAt(LocalDateTime.now());
                    return t;
                })
                .collect(Collectors.toList());
    }

    @Test
    void testCreateTransaction_Success() {
        // 1. 模拟 Repository 行为
        when(transactionRepository.findByReferenceId(this.createVO.getReferenceId(), this.createVO.getUserUid()))
                .thenReturn(Optional.empty());
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        // 2. 执行方法
        transactionService.createTransaction(createVO);

        // 4. 验证方法调用
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testCreateTransaction_Exception() {
        // 1. 模拟 Repository 行为
        when(transactionRepository.findByReferenceId(this.createVO.getReferenceId(), this.createVO.getUserUid()))
                .thenReturn(Optional.of(new Transaction()));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        // 2. 执行方法并捕获异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            transactionService.createTransaction(createVO);
        });

        // 3. 验证异常信息
        assertEquals(ErrorCodes.TRANSACTION_ALREADY_EXISTS.getCode(), exception.getCode());

        // 4. 验证方法调用
        verify(transactionRepository, times(1)).findByReferenceId(this.createVO.getReferenceId(), this.createVO.getUserUid());
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }

    @Test
    void testUpdateTransaction_Success() {
        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(this.updateVO, transaction);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setStatus(TransactionStatus.SUCCESS);
        // 1. 模拟 Repository 行为
        when(transactionRepository.findByReferenceId(this.updateVO.getReferenceId(), this.updateVO.getUserUid()))
                .thenReturn(Optional.of(transaction));
        doNothing().when(transactionRepository).updateTransactionById(
                any(BigDecimal.class),
                any(String.class),
                any(LocalDateTime.class),
                any(String.class),
                any(String.class)
        );
        // 2. 执行方法
        transactionService.updateTransaction(this.updateVO);


        // 4. 验证方法调用
        verify(transactionRepository, times(1)).findByReferenceId(this.updateVO.getReferenceId(), this.updateVO.getUserUid());
        verify(transactionRepository, times(1)).updateTransactionById(any(BigDecimal.class),
                any(String.class),
                any(LocalDateTime.class),
                any(String.class),
                any(String.class));
    }

    @Test
    void testUpdateTransaction_Exception() {
        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(this.updateVO, transaction);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setStatus(TransactionStatus.SUCCESS);
        // 1. 模拟 Repository 行为
        when(transactionRepository.findByReferenceId(this.updateVO.getReferenceId(), this.updateVO.getUserUid()))
                .thenReturn(Optional.empty());
        doNothing().when(transactionRepository).updateTransactionById(
                any(BigDecimal.class),
                any(String.class),
                any(LocalDateTime.class),
                any(String.class),
                any(String.class)
        );
        // 2. 执行方法并捕获异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            transactionService.updateTransaction(this.updateVO);
        });

        // 3. 验证异常信息
        assertEquals(ErrorCodes.TRANSACTION_NOT_FOUND.getCode(), exception.getCode());

        // 4. 验证方法调用
        verify(transactionRepository, times(1)).findByReferenceId(this.updateVO.getReferenceId(), this.updateVO.getUserUid());
        verify(transactionRepository, times(0)).updateTransactionById(any(BigDecimal.class),
                any(String.class),
                any(LocalDateTime.class),
                any(String.class),
                any(String.class));
    }

    @Test
    void testDeleteTransaction_Success() {
        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(this.deleteVO, transaction);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setStatus(TransactionStatus.SUCCESS);
        // 1. 模拟 Repository 行为
        when(transactionRepository.findByReferenceId(this.deleteVO.getReferenceId(), this.deleteVO.getUserUid()))
                .thenReturn(Optional.of(transaction));
        doNothing().when(transactionRepository).updateTransactionStatusById(
                any(),
                any(),
                any(),
                any()
        );
        // 2. 执行方法
        transactionService.deleteTransaction(this.deleteVO);


        // 4. 验证方法调用
        verify(transactionRepository, times(1)).findByReferenceId(this.deleteVO.getReferenceId(), this.deleteVO.getUserUid());
        verify(transactionRepository, times(1)).updateTransactionStatusById(
                any(),
                any(),
                any(),
                any());
    }

    @Test
    void testDeleteTransaction_Exception() {
        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(this.deleteVO, transaction);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setStatus(TransactionStatus.SUCCESS);
        // 1. 模拟 Repository 行为
        when(transactionRepository.findByReferenceId(this.deleteVO.getReferenceId(), this.deleteVO.getUserUid()))
                .thenReturn(Optional.empty());
        doNothing().when(transactionRepository).updateTransactionStatusById(
                any(),
                any(),
                any(),
                any()
        );
        // 2. 执行方法并捕获异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            transactionService.deleteTransaction(this.deleteVO);
        });

        // 3. 验证异常信息
        assertEquals(ErrorCodes.TRANSACTION_NOT_FOUND.getCode(), exception.getCode());

        // 4. 验证方法调用
        verify(transactionRepository, times(1)).findByReferenceId(this.deleteVO.getReferenceId(),
                this.deleteVO.getUserUid());
        verify(transactionRepository, times(0)).updateTransactionStatusById(
                any(),
                any(),
                any(),
                any());
    }

    @Test
    void testPageTransactionByUser_Page1() {
        // 1. 准备分页参数
        int pageNo = 1;
        int pageSize = 10;
        String userUid = "userId1";
        // 2. 模拟 Pageable 参数（pageNo - 1 = 0）
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        // 3. 构造分页结果（第一页 10 条数据）
        Page<Transaction> page = new PageImpl<>(transactions.subList(0, 10), pageable, transactions.size());

        // 4. 模拟 Repository 行为
        when(transactionRepository.findAllWithPagination(eq(pageable), eq(userUid)))
                .thenReturn(page);

        // 5. 执行方法
        PageRes<TransactionVO> result = transactionService.pageTransactionByUser(pageNo, pageSize, userUid);

        // 6. 验证分页信息
        assertEquals(pageNo, result.getPage());
        assertEquals(pageSize, result.getPageSize());
        assertEquals(25, result.getTotal());
        assertEquals(10, result.getData().size());

        // 7. 验证 VO 数据转换
        for (int i = 0; i < 10; i++) {
            TransactionVO vo = result.getData().get(i);
            assertEquals("REF-" + i, vo.getReferenceId());
            assertEquals((100 + i) + ".00", vo.getAmount());
        }

        // 8. 验证方法调用
        verify(transactionRepository, times(1))
                .findAllWithPagination(eq(pageable), eq(userUid));
    }
}
