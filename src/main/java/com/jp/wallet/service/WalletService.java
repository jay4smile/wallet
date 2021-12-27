package com.jp.wallet.service;


import com.jp.wallet.domain.Wallet;
import com.jp.wallet.repository.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/***
 * Class to handle wallet operations
 */
@Slf4j
@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    /***
     * Method to fetch wallet details by user id
     *
     * @param userId
     * @return
     */
    public Optional<Wallet> findWalletDetailsByUserId(String userId) {
        log.info("Get Wallet information by User Id: {}", userId);
        return walletRepository.findByUser_UserId(userId);
    }

    /***
     * Method to save wallet
     *
     * @param wallet
     * @return
     */
    public Wallet saveWallet(Wallet wallet) {
        return walletRepository.save(wallet);
    }

}
