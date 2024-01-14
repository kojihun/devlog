package com.develop.devlog.controller;

import com.develop.devlog.domain.User;
import com.develop.devlog.exception.InvalidRequest;
import com.develop.devlog.exception.InvalidSigninInformation;
import com.develop.devlog.repository.UserRepository;
import com.develop.devlog.request.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    @PostMapping("/auth/login")
    public User login(@RequestBody Login login) {
        log.info(">>> login={}", login);

        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(() -> new InvalidSigninInformation());

        return user;
    }
}
