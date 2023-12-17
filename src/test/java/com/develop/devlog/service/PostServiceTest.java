package com.develop.devlog.service;

import com.develop.devlog.domain.Post;
import com.develop.devlog.repository.PostRepository;
import com.develop.devlog.request.PostCreate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() {
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        postService.write(postCreate);

        Assertions.assertEquals(1L, postRepository.count());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test2() {
        Post requestPost = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        postRepository.save(requestPost);

        Post post = postService.get(requestPost.getId());

        Assertions.assertNotNull(post);
    }
}