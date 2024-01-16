package com.develop.devlog.controller;

import com.develop.devlog.config.AppConfig;
import com.develop.devlog.domain.Session;
import com.develop.devlog.domain.User;
import com.develop.devlog.exception.InvalidRequest;
import com.develop.devlog.exception.InvalidSigninInformation;
import com.develop.devlog.repository.UserRepository;
import com.develop.devlog.request.Login;
import com.develop.devlog.request.Signup;
import com.develop.devlog.response.SessionResponse;
import com.develop.devlog.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AppConfig appConfig;

    @PostMapping("/auth/login/cookie")
    public ResponseEntity<Object> loginCookie(@RequestBody Login login) {
//        String accessToken = authService.signin(login);
//        ResponseCookie cookie = ResponseCookie.from("SESSION", accessToken)
//                .domain("localhost") // todo 서버 환경에 따른 분리 필요
//                .path("/")
//                .httpOnly(true)
//                .secure(false)
//                .maxAge(Duration.ofDays(30))
//                .sameSite("Strict")
//                .build();
//
//        log.info(">>>> cookie ={}", cookie.toString());
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.SET_COOKIE, cookie.toString())
//                .build();
        return null;
    }

    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login) {
        Long userId = authService.signin(login);

        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(appConfig.getJwtKey()));

        // todo 암호화관련 변환 필요
        String jws = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .signWith(key)
                .setIssuedAt(new Date())
                .compact();

        return new SessionResponse(jws);
    }

    @PostMapping("/auth/signup")
    public void signup(@RequestBody Signup signup) {
        authService.signup(signup);
    }
}
