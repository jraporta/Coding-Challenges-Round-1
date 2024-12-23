package com.hackathon.finservice.Service;

import com.hackathon.finservice.Entities.Token;
import com.hackathon.finservice.Repositories.BlacklistTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TokenBlacklistService {

    BlacklistTokenRepository blacklistTokenRepository;

    public void addToken(String token) {
        blacklistTokenRepository.save(new Token(null, token));
    }

    public boolean isInBlacklist(String token) {
        return blacklistTokenRepository.existsByToken(token);
    }

}
