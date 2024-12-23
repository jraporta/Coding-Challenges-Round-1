package com.hackathon.finservice.DTO;

import com.hackathon.finservice.DTO.response.AccountResponse;
import com.hackathon.finservice.Entities.Account;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {

    public AccountResponse mapToAccountResponse(Account account) {
        return new AccountResponse(
                account.getAccountNumber(),
                account.getBalance(),
                account.getAccountType()
        );
    }

}
