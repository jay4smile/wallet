package com.jp.wallet.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/***
 * Class to handle wallet entity
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "WALLET")
public class Wallet implements Serializable {

    @Id
    @Column(name = "WALLET_ID")
    private String walletId;


    @Column(name = "BALANCE")
    private Double amount;

    @Column(name="ACTIVE")
    private Boolean active;

    @OneToOne(targetEntity = User.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    private User user;
}
