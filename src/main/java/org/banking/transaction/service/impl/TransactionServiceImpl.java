package org.banking.transaction.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.banking.transaction.common.PageRes;
import org.banking.transaction.common.constants.TransactionStatus;
import org.banking.transaction.common.exception.BusinessException;
import org.banking.transaction.common.exception.ErrorCodes;
import org.banking.transaction.repository.TransactionRepository;
import org.banking.transaction.repository.entity.Transaction;
import org.banking.transaction.request.TransactionCreateVO;
import org.banking.transaction.request.TransactionDeleteVO;
import org.banking.transaction.request.TransactionUpdateVO;
import org.banking.transaction.response.TransactionVO;
import org.banking.transaction.service.TransactionService;
import org.banking.transaction.service.convert.TransactionMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {
    private TransactionRepository transactionRepository;

    private static final Set<TransactionStatus> ALLOWED_UPDATE_STATUSES =
            Set.of(TransactionStatus.PENDING);

    private TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void createTransaction(TransactionCreateVO request) {
        Optional<Transaction> byReferenceId = this.transactionRepository.
                findByReferenceId(request.getReferenceId(), request.getUserUid());
        if (byReferenceId.isPresent()) {
            throw new BusinessException(ErrorCodes.TRANSACTION_ALREADY_EXISTS);
        }
        Transaction voToEntity = TransactionMapper.INSTANCE.createVoToEntity(request, TransactionStatus.SUCCESS);
        try {
            this.transactionRepository.save(voToEntity);
        } catch (DataIntegrityViolationException e) {
            // 捕获数据库唯一约束冲突异常
            throw new BusinessException(ErrorCodes.TRANSACTION_ALREADY_EXISTS);
        }
    }

    @Override
    public void updateTransaction(TransactionUpdateVO request) {
        Optional<Transaction> byReferenceId = this.transactionRepository.
                findByReferenceId(request.getReferenceId(), request.getUserUid());
        if (byReferenceId.isEmpty()) {
            throw new BusinessException(ErrorCodes.TRANSACTION_NOT_FOUND);
        }
        // ✅ 状态校验：仅允许 PENDING 状态的交易被修改
        if (!ALLOWED_UPDATE_STATUSES.contains(byReferenceId.get().getStatus())) {
            throw new BusinessException(ErrorCodes.TRANSACTION_STATUS_NOT_ALLOWED);
        }
        this.transactionRepository.updateTransactionById(request.getAmount(),
                request.getDescription(), LocalDateTime.now(),
                byReferenceId.get().getReferenceId(), byReferenceId.get().getUserUid());
    }

    @Override
    public void deleteTransaction(TransactionDeleteVO request) {
        Optional<Transaction> byReferenceId = this.transactionRepository.
                findByReferenceId(request.getReferenceId(), request.getUserUid());
        if (byReferenceId.isEmpty()) {
            throw new BusinessException(ErrorCodes.TRANSACTION_NOT_FOUND);
        }
        this.transactionRepository.updateTransactionStatusById(TransactionStatus.DELETED, LocalDateTime.now(),
                byReferenceId.get().getReferenceId(), byReferenceId.get().getUserUid());
    }

    @Override
    public PageRes<TransactionVO> pageTransactionByUser(Integer pageNo, Integer pageSize, String userUid) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        // 定义格式：保留 1 位小数
        DecimalFormat df = new DecimalFormat("0.00");
        Page<Transaction> allWithPagination = transactionRepository.findAllWithPagination(pageable, userUid);
        int total = (int) allWithPagination.getTotalElements();
        PageRes<TransactionVO> result = new PageRes<>(pageNo,pageSize,total);
        List<TransactionVO> content = allWithPagination.getContent().stream().map(t->{
            String amountStr = df.format(t.getAmount());
            return TransactionMapper.INSTANCE.entityToVo(t,amountStr);
        }).collect(Collectors.toList());
        result.setData(content);
        return result;
    }
}
