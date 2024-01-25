package com.develop.devlog.service;

import com.develop.devlog.domain.SubscribeObserver;
import com.develop.devlog.domain.Subscriber;
import com.develop.devlog.repository.SubscriberRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubscribeService {
    private final SubscriberRepository subscriberRepository;
    private final List<SubscribeObserver> subscribeObservers;

    public SubscribeService(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
        this.subscribeObservers = loadSubscribers();
    }

    private List<SubscribeObserver> loadSubscribers() {
        List<SubscribeObserver> observers = new ArrayList<>();

        List<Subscriber> subscribers = subscriberRepository.findAll();
        for (Subscriber subscriber : subscribers) {
            observers.add(SubscribeObserver.of(subscriber));
        }

        return observers;
    }

    public List<Subscriber> getAllSubscribers() {
        return subscriberRepository.findAll();
    }


    public void registerSubscriber(Subscriber subscriber) {
        SubscribeObserver subscribeObserver = SubscribeObserver.of(subscriber);
        if (!subscribeObservers.contains(subscribeObserver)) {
            subscribeObservers.add(subscribeObserver);
            subscriberRepository.save(subscriber);
        }
    }


    public void unregister(Subscriber subscriber) {
        SubscribeObserver subscribeObserver = SubscribeObserver.of(subscriber);
        if (!subscribeObservers.contains(subscribeObserver)) {
            subscribeObservers.remove(subscribeObserver);
            subscriberRepository.delete(subscriber);
        }
    }

    /**
     * 등록된 구독자에게 알림을 전송합니다.
     *
     * @param message 보낼 메세지
     */
    public void notifySubscribers(String message) {
        for (SubscribeObserver subscribeObserver : subscribeObservers) {
            subscribeObserver.notifyToSubscribers(message);
        }
    }

    /**
     * 신규 글을 등록하면, 알람을 전송합니다.
     *
     * @param content 알람 내용
     */
    public void writePost(String content) {
        System.out.println("New post: " + content);
        notifySubscribers("새로운 글이 발행되었습니다: " + content);
    }
}