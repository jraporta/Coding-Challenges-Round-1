package com.hackathon.finservice.Service;

import com.hackathon.finservice.DTO.response.UserInfoResponse;
import com.hackathon.finservice.Entities.Account;
import com.hackathon.finservice.Entities.User;
import com.hackathon.finservice.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    UserRepository userRepository;
    AccountService accountService;
    AuthenticationServiceHelper helper;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public UserInfoResponse retrieveUserDetails(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        log.debug("Retrieved User: {}", user);
        Account account = accountService.getMainAccount(user.getId()).orElseThrow();
        log.debug("Retrieved Main Account: {}", account);
        return helper.mapToUserInfoResponse(user, account);
    }
}
