package com.hackathon.finservice.Service;

import com.hackathon.finservice.DTO.DtoMapper;
import com.hackathon.finservice.DTO.response.AccountResponse;
import com.hackathon.finservice.DTO.response.UserInfoResponse;
import com.hackathon.finservice.Entities.Account;
import com.hackathon.finservice.Entities.User;
import com.hackathon.finservice.Exception.AccountNotExistsException;
import com.hackathon.finservice.Exception.NotAccountOwnerException;
import com.hackathon.finservice.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    UserRepository userRepository;
    AccountService accountService;
    AuthenticationServiceHelper helper;
    DtoMapper mapper;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    private User findByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow();
    }

    @Cacheable("dataCache")
    public UserInfoResponse retrieveUserDetails(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        log.debug("Retrieved User: {}", user);
        Account account = accountService.getMainAccount(user.getId()).orElseThrow();
        log.debug("Retrieved Main Account: {}", account);
        return helper.mapToUserInfoResponse(user, account);
    }


    public Account retrieveMainAccount(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        log.debug("Retrieved User: {}", user);
        Account account = accountService.getMainAccount(user.getId()).orElseThrow();
        log.debug("Retrieved Main Account: {}", account);
        return account;
    }

    public Long checkOwnership(String accountNumber, String email) {
        Long userId = findByEmail(email).getId();
        Account account = accountService.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotExistsException("Bad account number"));
        if (!userId.equals(account.getUserId())) {
            throw new NotAccountOwnerException("Bad request");
        }
        return userId;
    }


    public AccountResponse retrieveAccountDetails(String email, Integer index) {
        User user = userRepository.findByEmail(email).orElseThrow();
        log.debug("Retrieved User: {}", user);
        Account account = accountService.getAccount(user.getId(), index);
        log.debug("Retrieved Account with index {}: {}", index, account);
        return mapper.mapToAccountResponse(account);
    }
}
