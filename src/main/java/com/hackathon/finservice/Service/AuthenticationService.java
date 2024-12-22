package com.hackathon.finservice.Service;

import com.hackathon.finservice.DTO.request.LoginRequest;
import com.hackathon.finservice.DTO.request.RegisterRequest;
import com.hackathon.finservice.DTO.response.LoginResponse;
import com.hackathon.finservice.DTO.response.UserInfoResponse;
import com.hackathon.finservice.Entities.Account;
import com.hackathon.finservice.Exception.EmailAlreadyExistsException;
import com.hackathon.finservice.Security.JwtTokenUtil;
import lombok.AllArgsConstructor;
import com.hackathon.finservice.Entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AuthenticationService {

    private final AuthenticationServiceHelper helper;
    private final UserService userService;
    private final AccountService accountService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    public UserInfoResponse register(RegisterRequest request) {
        helper.checkPasswordFormat(request.getPassword());
        log.debug("Password passed format check");
        if (userService.existsByEmail(request.getEmail())) {
            log.debug("Register failed: email already exists");
            throw new EmailAlreadyExistsException("Email already exists");
        }
        log.debug("Passed existing email check.");
        User user = userService.saveUser(new User(
                null,
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword())));
        log.debug("Registered User: {}", user);
        Account account = accountService.createAccount(user.getId());
        log.debug("Created Account: {}", account);
        return helper.mapToUserInfoResponse(user, account);
    }

    public LoginResponse login(LoginRequest request) {
        String email = request.getIdentifier();
        if (!userService.existsByEmail(email)) {
            throw new UsernameNotFoundException("User not found for the given identifier: " + email);
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Bad Credentials");
        }
        log.debug("Authenticated login request: {}", request);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        final String token = jwtTokenUtil.generateToken(userDetails);
        log.debug("Generated token for user: {}", userDetails);
        return new LoginResponse(token);
    }

    //TODO
    public String logout() {
        return "";
    }
}
