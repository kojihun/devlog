package com.develop.devlog.repository;

import com.develop.devlog.domain.Session;
import com.develop.devlog.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SessionRepository extends CrudRepository<Session, Long> {

    Optional<Session> findByAccessToken(String accessToken);
}
