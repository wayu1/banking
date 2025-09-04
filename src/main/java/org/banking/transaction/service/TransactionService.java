package org.banking.transaction.service;

import org.banking.transaction.common.PageRes;
import org.banking.transaction.request.TransactionCreateVO;
import org.banking.transaction.request.TransactionDeleteVO;
import org.banking.transaction.request.TransactionUpdateVO;
import org.banking.transaction.response.TransactionVO;

public interface TransactionService {
    void createTransaction(TransactionCreateVO request);
    void updateTransaction(TransactionUpdateVO request);
    void deleteTransaction(TransactionDeleteVO request);

    // 根据交易类型筛选（可选）
    PageRes<TransactionVO> pageTransactionByUser(Integer pageNo, Integer pageSize, String userUid);
}
