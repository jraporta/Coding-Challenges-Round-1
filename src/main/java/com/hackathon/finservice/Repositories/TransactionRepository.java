package com.hackathon.finservice.Repositories;

import com.hackathon.finservice.Entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findTop5ByOrderByTransactionDateDesc();
    List<Transaction> findAllBySourceAccountNumber(String sourceAccountNumber);
}
