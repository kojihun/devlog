package com.develop.devlog.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Subscriber{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Subscriber(String name) {
        this.name = name;
    }

    public static Subscriber of(SubscribeObserver subscribeObserver) {
        return Subscriber.builder()
                .name(subscribeObserver.getName())
                .build();
    }
}
