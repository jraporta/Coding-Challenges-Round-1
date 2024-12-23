package com.hackathon.finservice.Service;

import com.hackathon.finservice.Entities.Account;
import com.hackathon.finservice.Repositories.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final InterestService interestService;

    public Account createMainAccount(Long userId) {
        return createAccount(userId, "Main");
    }

    public Optional<Account> getMainAccount(Long id) {
        List<Account> accounts = accountRepository.findByUserId(id);
        return accounts.stream()
                .filter(account -> account.getAccountType().equalsIgnoreCase("Main"))
                .findFirst();
    }

    public Account createAccount(Long userId, String accountType) {
        return accountRepository.save(new Account(UUID.randomUUID().toString(), 0, accountType, userId));
    }

    public Optional<Account> findByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public Account getAccount(Long id, Integer index) {
        List<Account> accounts = accountRepository.findByUserId(id);
        return accounts.get(index);
    }

    public void deposit(Account account, double amount) {
        account.setBalance(account.getBalance() + amount);
        log.debug("Made a deposit of {} in account {}. Final balance {}",
                amount, account.getAccountNumber(), account.getBalance());
        accountRepository.save(account);
        interestService.subscribe(account);
    }
}
