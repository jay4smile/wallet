package com.jp.wallet.service;

import com.jp.wallet.domain.User;
import com.jp.wallet.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/***
 * Class to handle User operations
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /***
     * Method to  find user details by userId
     *
     * @param userId
     * @return
     */
    public Optional<User> findUserDetails(String userId) {
        log.info("Fetch user details for user id: {}", userId);
        return this.userRepository.findByUserId(userId);
    }

    public User saveUser(User user) {
        return this.userRepository.save(user);
    }
}
