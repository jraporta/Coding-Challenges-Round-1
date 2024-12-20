package com.hackathon.finservice.Service;

import com.hackathon.finservice.Entities.User;
import com.hackathon.finservice.Exception.EmailAlreadyExistsException;
import com.hackathon.finservice.Repositories.UserRepository;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    UserRepository userRepository;

    public User createUser(@NotEmpty String name, @NotEmpty String email, @NotEmpty String hashedPassword) {
        if (userRepository.existsByEmail(email)) throw new EmailAlreadyExistsException("Email already exists");
        return userRepository.save(new User(name, email, hashedPassword));
    }
}
