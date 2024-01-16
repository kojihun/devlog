package com.develop.devlog.service;

import com.develop.devlog.crypto.PasswordEncoder;
import com.develop.devlog.crypto.ScryptPasswordEncoder;
import com.develop.devlog.domain.User;
import com.develop.devlog.exception.AlreadyExistsEmailException;
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
    private final PasswordEncoder encoder;

    @Transactional
    public Long signin(Login login) {
        User user = userRepository.findByEmail(login.getEmail())
                .orElseThrow(InvalidSigninInformation::new);

        boolean isLogin = encoder.matches(login.getPassword(), user.getPassword());
        if (!isLogin) {
            throw new InvalidSigninInformation();
        }

        return user.getId();
    }

    public void signup(Signup signup) {
        User checkUser = userRepository.findByEmail(signup.getEmail())
                .orElse(null);
        if (checkUser != null) {
            throw new AlreadyExistsEmailException();
        }

        String encryptedPassword = encoder.encrypt(signup.getPassword());

        User user = User.builder()
                .name(signup.getName())
                .password(encryptedPassword)
                .email(signup.getEmail())
                .build();

        userRepository.save(user);
    }
}
