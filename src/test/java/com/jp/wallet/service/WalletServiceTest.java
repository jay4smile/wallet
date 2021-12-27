package com.jp.wallet.service;

import com.jp.wallet.domain.User;
import com.jp.wallet.domain.Wallet;
import com.jp.wallet.repository.WalletRepository;
import com.jp.wallet.repository.WalletRepositoryTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class WalletServiceTest {

    @InjectMocks
    @Autowired
    WalletService walletService;

    @MockBean
    WalletRepository walletRepositoryTest;


    @Test
    public void testFindByUserId() {
        User user = new User("jay123", "Jay", true);
        Wallet wallet = new Wallet("abf122", 3000.0, true, user);
        when(walletRepositoryTest.findByUser_UserId(anyString())).thenReturn(Optional.of(wallet));

        Optional<Wallet> optionalWallet = walletService.findWalletDetailsByUserId("jay123");

        assertTrue(optionalWallet.isPresent());

    }


    @Test
    public void testSave() {
        User user = new User("jay123", "Jay", true);
        Wallet wallet = new Wallet("abf122", 3000.0, true, user);
        when(walletRepositoryTest.save(wallet)).thenReturn(wallet);

        Wallet wallet1 = walletService.saveWallet(wallet);

        assertEquals(3000.0,wallet1.getAmount());

    }

}
