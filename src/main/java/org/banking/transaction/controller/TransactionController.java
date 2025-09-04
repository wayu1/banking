package org.banking.transaction.controller;

import jakarta.validation.Valid;
import org.banking.transaction.common.PageRes;
import org.banking.transaction.request.TransactionCreateVO;
import org.banking.transaction.request.TransactionDeleteVO;
import org.banking.transaction.request.TransactionUpdateVO;
import org.banking.transaction.response.TransactionVO;
import org.banking.transaction.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bank/transaction/")
public class TransactionController {
    private TransactionService transactionService;

    private TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/createTransaction")
    public ResponseEntity<Boolean> createTransaction(@Valid @RequestBody TransactionCreateVO request) {
        this.transactionService.createTransaction(request);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/updateTransaction")
    public ResponseEntity<Boolean> updateTransaction(@Valid @RequestBody TransactionUpdateVO request) {
        this.transactionService.updateTransaction(request);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/deleteTransaction")
    public ResponseEntity<Boolean> deleteTransaction(@Valid @RequestBody TransactionDeleteVO request) {
        this.transactionService.deleteTransaction(request);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/pageTransaction")
    public ResponseEntity<PageRes<TransactionVO>> pageTransaction(@RequestParam Integer pageNo,
                                                                  @RequestParam Integer pageSize,
                                                                  @RequestParam String userUid) {
        PageRes<TransactionVO> pageResult = this.transactionService.pageTransactionByUser(pageNo,pageSize,userUid);
        return ResponseEntity.ok(pageResult);
    }
}
