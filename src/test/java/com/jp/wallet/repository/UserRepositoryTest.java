package com.jp.wallet.repository;

import com.jp.wallet.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void fetchUser() {
        User user = new User("jay12", "Jay", true);
        userRepository.save(user);

        Optional<User> userOptional = userRepository.findByUserId("jay12");
        assertTrue(userOptional.isPresent());
    }
}
