package com.jp.wallet.repository;

import com.jp.wallet.domain.Transaction;
import com.jp.wallet.domain.User;
import com.jp.wallet.util.TransactionStatus;
import com.jp.wallet.util.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TransactionRepositoryTest {

    @Autowired
    TransactionRepository transactionRepository;


    @Test
    public void testTransactionByTransactionId() {
        Transaction transaction = new Transaction();
        User user = new User("jay123", "Jay", true);
        transaction.setTransactionId("12sd");
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        transaction.setTransactionType(TransactionType.CREDIT);
        transaction.setAmount(3.0);
        transaction.setUser(user);
        transaction.setTransactionDate(LocalDateTime.now());


        transactionRepository.save(transaction);

        Optional<Transaction> transactionOptional =  transactionRepository.findByTransactionId("12sd");
        assertTrue(transactionOptional.isPresent());
    }

    @Test
    public void testTransactionByUser() {
        Transaction transaction = new Transaction();
        User user = new User("jay123", "Jay", true);
        transaction.setTransactionId("12sd");
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        transaction.setTransactionType(TransactionType.CREDIT);
        transaction.setAmount(3.0);
        transaction.setUser(user);
        transaction.setTransactionDate(LocalDateTime.now());


        transactionRepository.save(transaction);

        List<Transaction> transactionList =  transactionRepository.findByUser_UserId("jay123");
        assertEquals(1,transactionList.size());
    }
}
