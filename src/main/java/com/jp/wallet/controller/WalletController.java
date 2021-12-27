package com.jp.wallet.controller;


import com.jp.wallet.domain.StsTransaction;
import com.jp.wallet.domain.Transaction;
import com.jp.wallet.domain.User;
import com.jp.wallet.domain.Wallet;
import com.jp.wallet.exception.UserNotFoundException;
import com.jp.wallet.service.TransactionService;
import com.jp.wallet.service.UserService;
import com.jp.wallet.service.WalletService;
import com.jp.wallet.service.WalletTransactionService;
import com.jp.wallet.util.TransactionStatus;
import com.jp.wallet.util.TransactionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/***
 *  This class is rest controller class,
 *  to handle wallet transactions
 */


@Slf4j
@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletTransactionService walletTransactionService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    /***
     * Method to handle transactions on wallet. Based on transaction type, it will perform operations on wallet
     *
     * @param transaction
     * @return
     * @throws Exception
     */

    @PostMapping(path = "/transaction", produces = { "application/json"})
    public StsTransaction executeTransaction(@RequestBody StsTransaction transaction) throws Exception {
        log.info("Perform transactions {}", transaction.toString());
        return walletTransactionService.performWalletTransaction(transaction);
    }

    /***
     * This method will be used to fetch wallet balance or all transactions history by user.
     * Transaction type could be BALANCE or TRANSACTIONS
     *
     * @param userId
     * @param transactionType
     * @return
     * @throws UserNotFoundException
     */
    @GetMapping(path = "/{userId}/{transactionType}", produces = {"application/json"})
    public StsTransaction getWalletBalance(@PathVariable String userId, @PathVariable TransactionType transactionType) throws UserNotFoundException {
        log.info("Fetch wallet details or transactions: {}", transactionType.toString());
        StsTransaction stsTransaction = new StsTransaction();

        validateUserDetails(userId);

        if (TransactionType.BALANCE.equals(transactionType)) {
            getWalletBalance(stsTransaction, userId);
        } else if (TransactionType.TRANSACTIONS.equals(transactionType)) {
            getAllTransaction(stsTransaction, userId);
        }


        return stsTransaction;
    }

    /**
     * Method to validate user details. It should not be null and it should be active
     *
     * @param userId
     * @throws UserNotFoundException
     */
    private void validateUserDetails(String userId) throws UserNotFoundException{
        log.debug("Validating user details");
        if (!StringUtils.hasText(userId)) {
            throw new UserNotFoundException("Invalid user details");
        }
        Optional<User> optionalUser = userService.findUserDetails(userId);

        if (!optionalUser.isPresent() || !optionalUser.get().getActive()) {
            throw new UserNotFoundException("Invalid user details");
        }

    }

    /**
     * Method to fetch wallet balance
     *
     * @param stsTransaction
     * @param userId
     */
    private void getWalletBalance(StsTransaction stsTransaction, String userId) {
        log.debug("getting wallet balance");
        Optional<Wallet> wallet = walletService.findWalletDetailsByUserId(userId);

        if (wallet.isPresent()) {
            stsTransaction.setAmount(wallet.get().getAmount());
            stsTransaction.setTransactionStatus(TransactionStatus.SUCCESS);
            stsTransaction.setTransactionType(TransactionType.BALANCE);
        } else {
            stsTransaction.setTransactionStatus(TransactionStatus.FAIL);
            stsTransaction.setTransactionType(TransactionType.BALANCE);
        }
    }

    /**
     * Method to fetch all transactions by active users
     *
     * @param stsTransaction
     * @param userId
     */
    private void getAllTransaction(StsTransaction stsTransaction, String userId) {
        log.debug("Fetch all transactions for user");
        List<Transaction> transactionList = transactionService.getAllTransactionByUserId(userId);
        stsTransaction.setTransactions(transactionList);
        stsTransaction.setTransactionStatus(TransactionStatus.SUCCESS);
    }
}
