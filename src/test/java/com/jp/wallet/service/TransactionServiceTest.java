package com.jp.wallet.service;

import com.jp.wallet.domain.Transaction;
import com.jp.wallet.domain.User;
import com.jp.wallet.repository.TransactionRepository;
import com.jp.wallet.util.TransactionStatus;
import com.jp.wallet.util.TransactionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TransactionServiceTest {

    @InjectMocks
    @Autowired
    TransactionService transactionService;

    @MockBean
    TransactionRepository transactionRepository;

    @BeforeEach
    public void setup() {

    }

    @Test
    public void getTransactionByTransactionId() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId("1212");
        transaction.setUser(new User("jay123", "Jay", true));

        when(transactionRepository.findByTransactionId(anyString())).thenReturn(Optional.of(transaction));

        Optional<Transaction> transactionOptional = transactionService.getTransactionByTransactionId("1212");
        Assertions.assertTrue(transactionOptional.isPresent());
        Assertions.assertEquals("1212", transactionOptional.get().getTransactionId());
    }

    @Test
    public void saveTransaction() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId("1212");
        transaction.setUser(new User("jay123", "Jay", true));
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        Transaction transaction1 = transactionService.saveTransaction(transaction);

        Assertions.assertNotNull(transaction1);
    }

    @Test
    public void getTransactionByUser() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId("1212");

        User user = new User("jay123", "Jay", true);
        transaction.setUser(user);
        transaction.setAmount(0.0);
        transaction.setTransactionType(TransactionType.CREDIT);
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        List<Transaction> transactions = new ArrayList<Transaction>();
                transactions.add(transaction);
        when(transactionRepository.findByUser_UserId("jay123")).thenReturn(transactions);

        List<Transaction> transactionList = transactionService.getAllTransactionByUserId("jay123");
        Assertions.assertNotNull(transactionList);
        Assertions.assertEquals(1,transactionList.size());
    }
}
