package com.jp.wallet.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jp.wallet.util.TransactionStatus;
import com.jp.wallet.util.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/***
 * Class to handle API request and response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StsTransaction implements Serializable {

    private String transactionId;
    private Double amount;
    private String user;
    private TransactionType transactionType;
    private TransactionStatus transactionStatus;
    private List<Transaction> transactions;

}
