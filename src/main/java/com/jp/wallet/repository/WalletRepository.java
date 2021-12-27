package com.jp.wallet.repository;

import com.jp.wallet.domain.Wallet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/***
 * Repository class to handle Wallet entity Operation
 */
@Repository
public interface WalletRepository extends CrudRepository<Wallet, String> {

    Optional<Wallet> findByUser_UserId(String userId);
}
