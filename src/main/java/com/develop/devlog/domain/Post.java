package com.develop.devlog.domain;

import com.develop.devlog.request.PostCreate;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    protected Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static Post of(PostCreate postCreate) {
        return new Post(postCreate.getTitle(), postCreate.getContent());
    }
}