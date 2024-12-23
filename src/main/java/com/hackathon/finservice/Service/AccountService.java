package com.hackathon.finservice.Service;

import com.hackathon.finservice.Entities.Account;
import com.hackathon.finservice.Repositories.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account createMainAccount(Long userId) {
        return accountRepository.save(new Account(0, "Main", userId));
    }

    public Optional<Account> getMainAccount(Long id) {
        List<Account> accounts = accountRepository.findByUserId(id);
        return accounts.stream()
                .filter(account -> account.getAccountType().equalsIgnoreCase("Main"))
                .findFirst();
    }

    public Account createAccount(Long userId, String accountType) {
        return accountRepository.save(new Account(0, accountType, userId));
    }

    public Optional<Account> findById(String accountNumber) {
        return accountRepository.findById(accountNumber);
    }

    public Account getAccount(Long id, Integer index) {
        List<Account> accounts = accountRepository.findByUserId(id);
        return accounts.get(index);
    }

    public void deposit(Account account, double amount) {
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }
}
