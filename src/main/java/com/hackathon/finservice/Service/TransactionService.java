package com.hackathon.finservice.Service;

import com.hackathon.finservice.Entities.Account;
import com.hackathon.finservice.Entities.Transaction;
import com.hackathon.finservice.Entities.TransactionStatus;
import com.hackathon.finservice.Entities.TransactionType;
import com.hackathon.finservice.Repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class TransactionService {

    AccountService accountService;
    TransactionRepository transactionRepository;

    public Transaction deposit(Account account, double amount) {
        Transaction transaction = Transaction.builder()
                .amount(applyDepositFee(amount))
                .transactionType(TransactionType.CASH_DEPOSIT)
                .transactionStatus(TransactionStatus.PENDING)
                .transactionDate(Instant.now().toEpochMilli())
                .sourceAccountNumber(account.getAccountNumber())
                .targetAccountNumber("N/A")
                .build();
        return transactionRepository.save(transaction);
    }

    @Async
    public void monitorDeposit(Transaction transaction, Account account) {
        double amount = transaction.getAmount();
        accountService.deposit(account, amount);
        approveTransaction(transaction);
    }

    private double applyDepositFee(double amount) {
        int fee = amount > 50000 ? 2 : 0;
        return amount * (1 - fee / 100.0);
    }

    private void approveTransaction(Transaction transaction) {
        transaction.setTransactionStatus(TransactionStatus.APPROVED);
        transactionRepository.save(transaction);
    }


}
