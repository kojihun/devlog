package com.develop.devlog.service;

import com.develop.devlog.domain.User;
import com.develop.devlog.exception.AlreadyExistsEmailException;
import com.develop.devlog.repository.UserRepository;
import com.develop.devlog.request.Login;
import com.develop.devlog.request.Signup;
import org.apache.juli.logging.Log;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.rmi.AlreadyBoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void test1() {
        // given
        Signup signup = Signup.builder()
                .email("devlog@dev.com")
                .password("1234")
                .name("jhko")
                .build();

        // when
        authService.signup(signup);

        // then
        Assertions.assertEquals(1, userRepository.count());

        User user = userRepository.findAll().iterator().next();
        Assertions.assertEquals("devlog@dev.com", user.getEmail());
        Assertions.assertNotEquals("1234", user.getPassword());
        Assertions.assertEquals("jhko", user.getName());
    }

    @Test
    @DisplayName("회원가입 중복된 이메일")
    void test2() {
        // given
        User user = User.builder()
                .email("devlog@dev.com")
                .password("1234")
                .name("jhko")
                .build();

        userRepository.save(user);

        Signup signup = Signup.builder()
                .email("devlog@dev.com")
                .password("1234")
                .name("jhko")
                .build();

        // when
        Assertions.assertThrows(AlreadyExistsEmailException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                authService.signup(signup);
            }
        });

        // then
    }
}