package com.develop.devlog.repository;

import com.develop.devlog.domain.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {

}