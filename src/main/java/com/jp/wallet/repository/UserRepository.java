package com.jp.wallet.repository;

import com.jp.wallet.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/***
 * Repository class to handle User entity operation
 */
@Repository
public interface UserRepository extends CrudRepository<User, String> {
    Optional<User> findByUserId(String userId);
}
