package com.jp.wallet.repository;

import com.jp.wallet.domain.User;
import com.jp.wallet.domain.Wallet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class WalletRepositoryTest {

    @Autowired
    WalletRepository walletRepository;

    @Test
    public void testFetchWallet() {
        User user = new User("jay123", "Jay", true);
        Wallet wallet = new Wallet("12aad", 3.0, true, user);
        walletRepository.save(wallet);
        Optional<Wallet> optionalWallet = walletRepository.findByUser_UserId("jay123");
        assertTrue(optionalWallet.isPresent());
    }
}
