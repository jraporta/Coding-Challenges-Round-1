package com.hackathon.finservice.Service;

import com.hackathon.finservice.Entities.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class InterestService {

    private final double INTEREST_RATE = 10;
    private final String INVEST_TYPE = "Invest";
    private final List<Account> investAccounts = new ArrayList<>();

    @Scheduled(fixedRate = 10000)
    public void applyInterest() {
        log.debug("ApplyInterest is running");
        investAccounts.forEach(this::execute);
    }

    private void execute(Account account) {
        double interest = account.getBalance() * INTEREST_RATE / 100;
        account.setBalance(account.getBalance() + interest);
        log.info("Applied interest: Interest:{} FinalBalance:{}", interest, account.getBalance());
    }

    public void subscribe(Account account){
        if (account.getAccountType().equals(INVEST_TYPE)) {
            if (!investAccounts.contains(account)) {
                investAccounts.add(account);
                execute(account);
            }
        }
    }

}
