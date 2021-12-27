package com.jp.wallet.service;

import com.jp.wallet.domain.User;
import com.jp.wallet.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @InjectMocks
    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @BeforeEach
    public void setup() {
        User user = new User("jay123", "Jay", true);

        when(userRepository.findByUserId("jay123")).thenReturn(Optional.of(user));

    }

    @Test
    public void testUserSaveOperation() {
        User user = new User("jay1234", "Jay", true);
        when(userRepository.save(user)).thenReturn(user);
        User user1 = userService.saveUser(user);
        Assertions.assertTrue(user1.getActive());
        Assertions.assertEquals("Jay", user1.getName());
    }

    @Test
    public void testFindUser() {

        Optional<User> user1 = userService.findUserDetails("jay123");
        Assertions.assertTrue(user1.isPresent());
        Assertions.assertEquals("Jay", user1.get().getName());
    }
}
