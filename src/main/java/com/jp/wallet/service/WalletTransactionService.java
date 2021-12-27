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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/***
 * Class to handle wallet operations like debit, credit transactions
 */

@Slf4j
@Service
public class WalletTransactionService {

    @Autowired
    private WalletService walletService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    /***
     * Method to perform credit, debit transactions on wallet
     * @param stsTransaction
     * @return
     * @throws Exception
     */
    @Transactional
    public StsTransaction performWalletTransaction(StsTransaction stsTransaction) throws Exception{
        log.info("Perform Wallet Transaction");

        Optional<User> userOptional = userService.findUserDetails(stsTransaction.getUser());


        Optional<Transaction> transactionOptional = transactionService.getTransactionByTransactionId(stsTransaction.getTransactionId());

        Optional<Wallet> walletOptional = walletService.findWalletDetailsByUserId(stsTransaction.getUser());

        validateTransactonDetails(transactionOptional, userOptional, walletOptional);

        Transaction transaction = new Transaction();
        transaction.setAmount(stsTransaction.getAmount());
        transaction.setTransactionId(stsTransaction.getTransactionId());
        transaction.setUser(userOptional.get());
        transaction.setTransactionStatus(stsTransaction.getTransactionStatus());

        Wallet wallet = walletOptional.get();


        transaction.setTransactionDate(LocalDateTime.now());

        if (stsTransaction.getTransactionType().equals(TransactionType.DEBIT)) {

            transaction.setTransactionType(TransactionType.DEBIT);
            if (wallet.getAmount() == 0 || (wallet.getAmount() - transaction.getAmount()) < 0) {
                transaction.setTransactionStatus(TransactionStatus.FAIL);
                stsTransaction.setTransactionStatus(TransactionStatus.FAIL);
            } else {
                wallet.setAmount(wallet.getAmount() - transaction.getAmount());
                walletService.saveWallet(wallet);
                transaction.setTransactionStatus(TransactionStatus.SUCCESS);
                stsTransaction.setTransactionStatus(TransactionStatus.SUCCESS);
            }
            transactionService.saveTransaction(transaction);
        } else if (stsTransaction.getTransactionType().equals( TransactionType.CREDIT)) {
            transaction.setTransactionType(TransactionType.CREDIT);
            wallet.setAmount(wallet.getAmount() + transaction.getAmount());
            walletService.saveWallet(wallet);
            transaction.setTransactionStatus(TransactionStatus.SUCCESS);
            stsTransaction.setTransactionStatus(TransactionStatus.SUCCESS);

            transactionService.saveTransaction(transaction);
        }
        return stsTransaction;
    }

    /***
     * Method to validate transaction, user and wallet objects
     *
     * @param optionalTransaction
     * @param optionalUser
     * @param optionalWallet
     * @throws UserNotFoundException
     * @throws DuplicateTransactionException
     * @throws WalletException
     */
    private void validateTransactonDetails(Optional<Transaction> optionalTransaction, Optional<User> optionalUser, Optional<Wallet> optionalWallet) throws UserNotFoundException, DuplicateTransactionException, WalletException {
        log.debug("Validating user, wallet and transaction");

        if (!optionalUser.isPresent() || !optionalUser.get().getActive()) {
            throw new UserNotFoundException("Invalid User Details");
        }

        if (optionalTransaction.isPresent()) {
            throw new DuplicateTransactionException("Transaction already exist");
        }

        if(!optionalWallet.isPresent() || !optionalWallet.get().getActive()) {
            throw new WalletException("There is an issue with wallet");
        }
    }
}
