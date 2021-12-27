package com.jp.wallet.service;


import com.jp.wallet.domain.Transaction;
import com.jp.wallet.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/***
 * Class to handle Transaction operations
 */
@Slf4j
@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;


    /***
     * Method to fetch transactions by transaction id
     * @param transactionId
     * @return
     */
    public Optional<Transaction>  getTransactionByTransactionId(String transactionId) {
        log.info("Getting transaction by transaction: {}", transactionId);
       return transactionRepository.findByTransactionId(transactionId);
    }

    /***
     * Method to save transactions details
     *
     * @param transaction
     * @return
     */
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    /***
     * Method to fetch all transactions by user
     * @param userId
     * @return
     */
    public List<Transaction> getAllTransactionByUserId(String userId) {
        log.info("Getting transactions by user: {}", userId);
        List<Transaction> transactionList = transactionRepository.findByUser_UserId(userId);
        transactionList.stream().forEach(transaction -> transaction.setUser(null));

        return transactionList;
    }
}
