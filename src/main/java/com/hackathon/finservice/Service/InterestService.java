package com.hackathon.finservice.Service;

import com.hackathon.finservice.Entities.Account;
import com.hackathon.finservice.Repositories.AccountRepository;
import com.hackathon.finservice.Util.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class InterestService {

    private final AccountRepository accountRepository;

    private final double INTEREST_RATE = 10;
    private final String INVEST_TYPE = "Invest";
    private final String LOG_PATH = "interest.log";
    private final List<String> investAccounts = new ArrayList<>();

    @Scheduled(fixedRate = 10000)
    public void applyInterest() {
        log.debug("ApplyInterest is running");
        investAccounts.forEach(this::applyInterest);
    }

    private void applyInterest(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow();
        double interest = account.getBalance() * INTEREST_RATE / 100;
        account.setBalance(account.getBalance() + interest);
        account = accountRepository.save(account);
        log.info("Applied interest: Interest:{} FinalBalance:{}", interest, account.getBalance());
        JsonUtil.logJsonToFile(LOG_PATH, account);
    }

    public void subscribe(Account account){
        if (account.getAccountType().equalsIgnoreCase(INVEST_TYPE)) {
            if (!investAccounts.contains(account.getAccountNumber())) {
                investAccounts.add(account.getAccountNumber());
            }
        }
    }

}
