package com.develop.devlog.service;

import com.develop.devlog.domain.Subscriber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SubscribeServiceTest {
    @Autowired
    SubscribeService subscribeService;

    @Test
    @DisplayName("신규 구독자를 등록합니다.")
    void registerSubscriber() {
        // Given
        Subscriber andy = Subscriber.builder()
                .name("Andy")
                .build();
        Subscriber ethan = Subscriber.builder()
                .name("Ethan")
                .build();
        Subscriber ann = Subscriber.builder()
                .name("Ann")
                .build();

        // When
        subscribeService.registerSubscriber(andy);
        subscribeService.registerSubscriber(ethan);
        subscribeService.registerSubscriber(ann);

        // Then
        Assertions.assertEquals(3, subscribeService.getAllSubscribers().size());
    }

    @Test
    @DisplayName("구독자에게 알람을 전송합니다.")
    void notifySubscriber() {
        // Given
        Subscriber andy = Subscriber.builder()
                .name("Andy")
                .build();
        Subscriber ethan = Subscriber.builder()
                .name("Ethan")
                .build();
        Subscriber ann = Subscriber.builder()
                .name("Ann")
                .build();

        // When
        subscribeService.registerSubscriber(andy);
        subscribeService.registerSubscriber(ethan);
        subscribeService.registerSubscriber(ann);

        // Then
        subscribeService.writePost("안녕하세요");
    }

}