package com.jp.wallet.repository;

import com.jp.wallet.domain.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/***
 * Repository class to handle Transaction entity operations
 */
@Repository
public interface TransactionRepository extends CrudRepository<Transaction, String> {

    Optional<Transaction> findByTransactionId(String transactionId);
    List<Transaction> findByUser_UserId(String userId);
}
