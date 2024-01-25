package com.develop.devlog.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SubscribeObserver implements Observer{
    private String name;

    @Builder
    public SubscribeObserver(String name) {
        this.name = name;
    }

    /**
     * Subscriber객체를 SubscribeObserver객체로 변환
     *
     * @param subscriber 구독자 객체
     * @return SubscribeObserver 객체
     */
    public static SubscribeObserver of(Subscriber subscriber) {
        return SubscribeObserver.builder()
                .name(subscriber.getName())
                .build();
    }

    /**
     * 구독자에게 알람을 전송합니다.
     * 
     * @param message 전송할 메세지
     */
    @Override
    public void notifyToSubscribers(String message) {
        System.out.println(message);
    }
}