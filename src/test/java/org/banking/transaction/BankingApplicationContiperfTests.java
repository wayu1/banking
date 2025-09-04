package org.banking.transaction;

import org.banking.transaction.common.PageRes;
import org.banking.transaction.common.constants.TransactionStatus;
import org.banking.transaction.common.constants.TransactionType;
import org.banking.transaction.repository.entity.Transaction;
import org.banking.transaction.request.TransactionCreateVO;
import org.banking.transaction.request.TransactionDeleteVO;
import org.banking.transaction.request.TransactionUpdateVO;
import org.banking.transaction.response.TransactionVO;
import org.banking.transaction.service.TransactionService;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankingApplication.class)
public class BankingApplicationContiperfTests {
    @Rule
    public ContiPerfRule i = new ContiPerfRule();
    @Autowired
    private TransactionService transactionService;

    @Test
    @PerfTest(threads = 100, duration = 1500)
    public void pageTransactionByUser() {
            // 为每个线程分配不同的分页参数（1~5 页）
            int page = ThreadLocalRandom.current().nextInt(1, 6);
            int pageSize = 10;
            String userUid = "userId1";

            // 执行分页查询
            PageRes<TransactionVO> result = transactionService.pageTransactionByUser(page, pageSize, userUid);

            // 1. 验证分页信息
            assertEquals(page, result.getPage());
            assertEquals(pageSize, result.getPageSize());
            assertTrue(result.getTotal() >= 0);
            assertTrue(result.getData().size() <= pageSize);

            // 2. 验证数据不为空
            assertNotNull(result.getData());
    }

    @Test
    @PerfTest(threads = 100, duration = 1500)
    public void createTransaction() {
        // 为每个线程生成唯一 referenceId（例如使用线程 ID 或时间戳）
        String referenceId = "referenceId-" + Thread.currentThread().getId() + "-" + System.currentTimeMillis();

        TransactionCreateVO transaction = new TransactionCreateVO();
        transaction.setAmount(BigDecimal.valueOf(100.00));
        transaction.setUserUid("userId1");
        transaction.setDescription("desc");
        transaction.setReferenceId(referenceId);
        transaction.setType(TransactionType.DEPOSIT);
        transactionService.createTransaction(transaction);
    }

    @Test
    @PerfTest(threads = 100, duration = 1500)
    public void updateTransaction() {
        // ✅ 生成更唯一的 referenceId（线程 ID + 时间戳 + 随机数）
        String referenceId = "referenceId-" + Thread.currentThread().getId() + "-" + System.nanoTime() + "-" + new Random().nextInt(1000);

        // 1. 创建记录（确保存在）
        TransactionCreateVO createVO = new TransactionCreateVO();
        createVO.setReferenceId(referenceId);
        createVO.setAmount(BigDecimal.valueOf(100.00));
        createVO.setUserUid("userId1");
        createVO.setDescription("desc");
        createVO.setType(TransactionType.DEPOSIT);
        transactionService.createTransaction(createVO);

        // 2. 执行更新
        TransactionUpdateVO updateVO = new TransactionUpdateVO();
        updateVO.setReferenceId(referenceId);
        updateVO.setAmount(BigDecimal.valueOf(200.00));
        updateVO.setDescription("updated desc");
        updateVO.setUserUid("userId1");
        transactionService.updateTransaction(updateVO);

    }

    @Test
    @PerfTest(threads = 100, duration = 1500)
    public void deleteTransaction() {
        // ✅ 生成更唯一的 referenceId（线程 ID + 时间戳 + 随机数）
        String referenceId = "referenceId-" + Thread.currentThread().getId() + "-" + System.nanoTime() + "-" + new Random().nextInt(1000);

        // 1. 创建记录（确保存在）
        TransactionCreateVO createVO = new TransactionCreateVO();
        createVO.setReferenceId(referenceId);
        createVO.setAmount(BigDecimal.valueOf(100.00));
        createVO.setUserUid("userId1");
        createVO.setDescription("desc");
        createVO.setType(TransactionType.DEPOSIT);
        transactionService.createTransaction(createVO);

        // 2. 执行更新
        TransactionDeleteVO updateVO = new TransactionDeleteVO();
        updateVO.setReferenceId(referenceId);
        updateVO.setUserUid("userId1");
        transactionService.deleteTransaction(updateVO);
    }

}
