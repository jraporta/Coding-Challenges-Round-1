package com.hackathon.finservice.Repositories;

import com.hackathon.finservice.Entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistTokenRepository extends JpaRepository<Token, Long> {

    boolean existsByToken(String token);

}
