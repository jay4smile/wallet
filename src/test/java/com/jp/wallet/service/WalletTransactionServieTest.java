package com.jp.wallet.service;

import com.jp.wallet.domain.StsTransaction;
import com.jp.wallet.domain.Transaction;
import com.jp.wallet.domain.User;
import com.jp.wallet.domain.Wallet;
import com.jp.wallet.exception.DuplicateTransactionException;
import com.jp.wallet.exception.UserNotFoundException;
import com.jp.wallet.exception.WalletException;
import com.jp.wallet.util.TransactionStatus;
import com.jp.wallet.util.TransactionType;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class WalletTransactionServieTest {

    @InjectMocks
    @Autowired
    WalletTransactionService walletTransactionService;

    @MockBean
    WalletService walletService;

    @MockBean
    UserService userService;

    @MockBean
    TransactionService transactionService;

    @Test
    public void testUserNotFoundException() {
        User user = new User("jay123", "Jay", true);
        Wallet wallet = new Wallet("abc", 300.0, true, user);
        Transaction transaction = new Transaction("trans123", TransactionType.CREDIT, 300.0, user, TransactionStatus.SUCCESS, LocalDateTime.now());
        StsTransaction stsTransaction = new StsTransaction();
        stsTransaction.setAmount(2.0);
        stsTransaction.setTransactionId("1212");
        stsTransaction.setUser("jay123");

        when(userService.findUserDetails(anyString())).thenReturn(Optional.empty());
        when(walletService.findWalletDetailsByUserId(anyString())).thenReturn(Optional.of(wallet));
        when(transactionService.getTransactionByTransactionId(anyString())).thenReturn(Optional.of(transaction));

        Exception exception = assertThrows(UserNotFoundException.class, () ->{
            walletTransactionService.performWalletTransaction(stsTransaction);
        });
        assertEquals("Invalid User Details", exception.getMessage());
    }


    @Test
    public void testUserNotFoundExceptionWithInActiveUser() {
        User user = new User("jay123", "Jay", false);
        Wallet wallet = new Wallet("abc", 300.0, true, user);
        Transaction transaction = new Transaction("trans123", TransactionType.CREDIT, 300.0, user, TransactionStatus.SUCCESS, LocalDateTime.now());
        StsTransaction stsTransaction = new StsTransaction();
        stsTransaction.setAmount(2.0);
        stsTransaction.setTransactionId("1212");
        stsTransaction.setUser("jay123");

        when(userService.findUserDetails(anyString())).thenReturn(Optional.of(user));
        when(walletService.findWalletDetailsByUserId(anyString())).thenReturn(Optional.of(wallet));
        when(transactionService.getTransactionByTransactionId(anyString())).thenReturn(Optional.of(transaction));

        Exception exception = assertThrows(UserNotFoundException.class, () ->{
            walletTransactionService.performWalletTransaction(stsTransaction);
        });
        assertEquals("Invalid User Details", exception.getMessage());
    }


    @Test
    public void testDuplicateTransactionException() {
        User user = new User("jay123", "Jay", true);
        Wallet wallet = new Wallet("abc", 300.0, true, user);
        Transaction transaction = new Transaction("trans123", TransactionType.CREDIT, 300.0, user, TransactionStatus.SUCCESS, LocalDateTime.now());
        StsTransaction stsTransaction = new StsTransaction();
        stsTransaction.setAmount(2.0);
        stsTransaction.setTransactionId("1212");
        stsTransaction.setUser("jay123");

        when(userService.findUserDetails(anyString())).thenReturn(Optional.of(user));
        when(walletService.findWalletDetailsByUserId(anyString())).thenReturn(Optional.of(wallet));
        when(transactionService.getTransactionByTransactionId(anyString())).thenReturn(Optional.of(transaction));

        Exception exception = assertThrows(DuplicateTransactionException.class, () ->{
            walletTransactionService.performWalletTransaction(stsTransaction);
        });
        assertEquals("Transaction already exist", exception.getMessage());
    }

    @Test
    public void testWalletException() {
        User user = new User("jay123", "Jay", true);
        Wallet wallet = new Wallet("abc", 300.0, true, user);
        Transaction transaction = new Transaction("trans123", TransactionType.CREDIT, 300.0, user, TransactionStatus.SUCCESS, LocalDateTime.now());
        StsTransaction stsTransaction = new StsTransaction();
        stsTransaction.setAmount(2.0);
        stsTransaction.setTransactionId("1212");
        stsTransaction.setUser("jay123");

        when(userService.findUserDetails(anyString())).thenReturn(Optional.of(user));
        when(walletService.findWalletDetailsByUserId(anyString())).thenReturn(Optional.empty());
        when(transactionService.getTransactionByTransactionId(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(WalletException.class, () ->{
            walletTransactionService.performWalletTransaction(stsTransaction);
        });
        assertEquals("There is an issue with wallet", exception.getMessage());
    }

    @Test
    public void testDebitTransaction() throws Exception{
        User user = new User("jay123", "Jay", true);
        Wallet wallet = new Wallet("abc", 300.0, true, user);

        StsTransaction stsTransaction = new StsTransaction();
        stsTransaction.setAmount(2.0);
        stsTransaction.setTransactionId("1212");
        stsTransaction.setUser("jay123");
        stsTransaction.setTransactionType(TransactionType.DEBIT);

        when(userService.findUserDetails(anyString())).thenReturn(Optional.of(user));
        when(walletService.findWalletDetailsByUserId(anyString())).thenReturn(Optional.of(wallet));
        when(transactionService.getTransactionByTransactionId(anyString())).thenReturn(Optional.empty());

        StsTransaction stsTransaction1 = walletTransactionService.performWalletTransaction(stsTransaction);
        assertEquals(TransactionStatus.SUCCESS,stsTransaction1.getTransactionStatus());
        verify(transactionService,times(1)).saveTransaction(any());
    }


    @Test
    public void testDebitTransactionWithZeroBalance() throws Exception{
        User user = new User("jay123", "Jay", true);
        Wallet wallet = new Wallet("abc", 0.0, true, user);

        StsTransaction stsTransaction = new StsTransaction();
        stsTransaction.setAmount(2.0);
        stsTransaction.setTransactionId("1212");
        stsTransaction.setUser("jay123");
        stsTransaction.setTransactionType(TransactionType.DEBIT);

        when(userService.findUserDetails(anyString())).thenReturn(Optional.of(user));
        when(walletService.findWalletDetailsByUserId(anyString())).thenReturn(Optional.of(wallet));
        when(transactionService.getTransactionByTransactionId(anyString())).thenReturn(Optional.empty());

        StsTransaction stsTransaction1 = walletTransactionService.performWalletTransaction(stsTransaction);
        assertEquals(TransactionStatus.FAIL,stsTransaction1.getTransactionStatus());
        verify(transactionService,times(1)).saveTransaction(any());
    }

    @Test
    public void testDebitTransactionWithLowBalance() throws Exception{
        User user = new User("jay123", "Jay", true);
        Wallet wallet = new Wallet("abc", 1.0, true, user);

        StsTransaction stsTransaction = new StsTransaction();
        stsTransaction.setAmount(2.0);
        stsTransaction.setTransactionId("1212");
        stsTransaction.setUser("jay123");
        stsTransaction.setTransactionType(TransactionType.DEBIT);

        when(userService.findUserDetails(anyString())).thenReturn(Optional.of(user));
        when(walletService.findWalletDetailsByUserId(anyString())).thenReturn(Optional.of(wallet));
        when(transactionService.getTransactionByTransactionId(anyString())).thenReturn(Optional.empty());

        StsTransaction stsTransaction1 = walletTransactionService.performWalletTransaction(stsTransaction);
        assertEquals(TransactionStatus.FAIL,stsTransaction1.getTransactionStatus());
        verify(transactionService,times(1)).saveTransaction(any());
    }

    @Test
    public void testCreditTransaction() throws Exception{
        User user = new User("jay123", "Jay", true);
        Wallet wallet = new Wallet("abc", 1.0, true, user);

        StsTransaction stsTransaction = new StsTransaction();
        stsTransaction.setAmount(2.0);
        stsTransaction.setTransactionId("1212");
        stsTransaction.setUser("jay123");
        stsTransaction.setTransactionType(TransactionType.CREDIT);

        when(userService.findUserDetails(anyString())).thenReturn(Optional.of(user));
        when(walletService.findWalletDetailsByUserId(anyString())).thenReturn(Optional.of(wallet));
        when(transactionService.getTransactionByTransactionId(anyString())).thenReturn(Optional.empty());

        StsTransaction stsTransaction1 = walletTransactionService.performWalletTransaction(stsTransaction);
        assertEquals(TransactionStatus.SUCCESS,stsTransaction1.getTransactionStatus());

        verify(transactionService,times(1)).saveTransaction(any());
    }

}
