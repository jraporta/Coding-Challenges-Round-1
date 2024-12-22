package com.hackathon.finservice.Service;

import com.hackathon.finservice.Entities.User;
import com.hackathon.finservice.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
}
