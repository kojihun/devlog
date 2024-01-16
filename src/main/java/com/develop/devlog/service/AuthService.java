package com.develop.devlog.service;

import com.develop.devlog.domain.Session;
import com.develop.devlog.domain.User;
import com.develop.devlog.exception.AlreadyExistsEmailException;
import com.develop.devlog.exception.InvalidRequest;
import com.develop.devlog.exception.InvalidSigninInformation;
import com.develop.devlog.repository.UserRepository;
import com.develop.devlog.request.Login;
import com.develop.devlog.request.Signup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public Long signin(Login login) {
        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(InvalidSigninInformation::new);

        Session session = user.addSession();

        return user.getId();
    }

    public void signup(Signup signup) {
        User checkUser = userRepository.findByEmail(signup.getEmail())
                .orElse(null);
        if (checkUser != null) {
            throw new AlreadyExistsEmailException();
        }

        User user = User.builder()
                .name(signup.getName())
                .password(signup.getPassword())
                .email(signup.getEmail())
                .build();

        userRepository.save(user);
    }
}
