package com.jp.wallet.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jp.wallet.util.TransactionStatus;
import com.jp.wallet.util.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.time.LocalDateTime;

/***
 * Class to handle Transaction entity
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "USER_TRANSACTION" )
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transaction implements Serializable {

    @Id
    @Column(name = "TRANS_ID")
    private String transactionId;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "AMOUNT")
    @Min(value = 0L, message = "Amount should be greater than 0")
    private Double amount;

    @ManyToOne(cascade = CascadeType.ALL, fetch= FetchType.EAGER, targetEntity = User.class)
    @JoinColumn(name ="USER_ID")
    private User user;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;


    @Column(name = "TRANSACTION_DATE")
    private LocalDateTime transactionDate;

}
