package com.hackathon.finservice.Service;

import com.hackathon.finservice.Entities.Account;
import com.hackathon.finservice.Entities.Transaction;
import com.hackathon.finservice.Entities.TransactionStatus;
import com.hackathon.finservice.Entities.TransactionType;
import com.hackathon.finservice.Exception.InsufficientBalanceException;
import com.hackathon.finservice.Repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

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


    public Transaction withdraw(Account account, double amount) {
        double finalAmount = applyWithdrawFee(amount);
        if (finalAmount > account.getBalance()) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
        Transaction transaction = Transaction.builder()
                .amount(finalAmount)
                .transactionType(TransactionType.CASH_WITHDRAWAL)
                .transactionStatus(TransactionStatus.PENDING)
                .transactionDate(Instant.now().toEpochMilli())
                .sourceAccountNumber(account.getAccountNumber())
                .targetAccountNumber("N/A")
                .build();
        return transactionRepository.save(transaction);
    }

    private double applyWithdrawFee(double amount) {
        int fee = amount > 10000 ? 1 : 0;
        return amount * (1 + fee / 100.0);
    }

    @Async
    public void monitorWithdraw(Transaction transaction, Account account) {
        accountService.deposit(account, -transaction.getAmount());
        approveTransaction(transaction);
    }

    public Transaction transfer(Account account, String targetAccountNumber, double amount) {
        if (amount > account.getBalance()) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
        Transaction transaction = Transaction.builder()
                .amount(amount)
                .transactionType(TransactionType.CASH_TRANSFER)
                .transactionStatus(TransactionStatus.PENDING)
                .transactionDate(Instant.now().toEpochMilli())
                .sourceAccountNumber(account.getAccountNumber())
                .targetAccountNumber(targetAccountNumber)
                .build();
        return transactionRepository.save(transaction);
    }

    @Async
    public void monitorTransfer(Transaction transaction, Account account) {
        Account targetAccount = accountService.findById(transaction.getTargetAccountNumber())
                .orElseThrow();
        accountService.deposit(account, -transaction.getAmount());
        accountService.deposit(targetAccount, transaction.getAmount());
        checkForFraud(transaction);
    }

    private void checkForFraud(Transaction transaction) {
        TransactionStatus status = TransactionStatus.APPROVED;
        if (transaction.getAmount() > 80000) {
            status = TransactionStatus.FRAUD;
        }
        transactionRepository.save(transaction);

        List<Transaction> last5 = transactionRepository.findTop5ByOrderByTransactionDateDesc();

        if (last5.size() == 5) {
            Long first = last5.getFirst().getTransactionDate();
            Long last = last5.getLast().getTransactionDate();

            if ((first - last) < 5000) {
                last5.forEach(tr -> tr.setTransactionStatus(TransactionStatus.FRAUD));
                transactionRepository.saveAll(last5);
            }
        }


    }
}
