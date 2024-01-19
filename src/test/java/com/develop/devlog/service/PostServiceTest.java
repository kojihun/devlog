package com.develop.devlog.service;

import com.develop.devlog.domain.Post;
import com.develop.devlog.exception.PostNotFound;
import com.develop.devlog.repository.PostRepository;
import com.develop.devlog.request.PostCreate;
import com.develop.devlog.request.PostEdit;
import com.develop.devlog.request.PostSearch;
import com.develop.devlog.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;


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
    @DisplayName("게시물을 등록합니다.")
    void createPost() {
        // Given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        // When
        postService.createPost(postCreate);

        // Then
        Assertions.assertEquals(1L, postRepository.count());
    }

    @Test
    @DisplayName("게시물 id를 사용하여 1건을 조회합니다.")
    void getPostByIdTest() {
        // Given
        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        postRepository.save(post);

        // When
        PostResponse postResponse = postService.getPostById(post.getId());

        // Then
        Assertions.assertNotNull(postResponse);
        Assertions.assertEquals(post.getTitle(), postResponse.getTitle());
        Assertions.assertEquals(post.getContent(), postResponse.getContent());
    }

    @Test
    @DisplayName("모든 게시물을 조회합니다. (최신글 순서대로 정렬)")
    void getAllPostsTest() {
        // Given
        List<Post> posts = IntStream.range(0, 30)
                .mapToObj(index -> Post.builder()
                        .title("제목: " + index)
                        .content("내용: " + index)
                        .build()).toList();

        postRepository.saveAll(posts);

        // When
        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(30)
                .build();

        List<PostResponse> createdPosts = postService.getAllPosts(postSearch);

        // Then
        Assertions.assertEquals(30L, posts.size());
        Assertions.assertEquals("제목: 29", createdPosts.get(0).getTitle());
        Assertions.assertEquals("내용: 29", createdPosts.get(0).getContent());
    }

    @Test
    @DisplayName("게시물 제목을 수정합니다.")
    void editPostTitleTest() {
        // Given
        Post post = Post.builder()
                .title("변경 전 제목입니다.")
                .content("변경 전 제목입니다.")
                .build();

        postRepository.save(post);

        // When
        PostEdit postEdit = PostEdit.builder()
                .title("변경 후 제목입니다.")
                .content("변경 전 제목입니다.")
                .build();

        postService.editPost(post.getId(), postEdit);

        // Then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("게시물이 존재하지 않습니다. id = " + post.getId()));

        Assertions.assertNotEquals(post.getTitle(), changedPost.getTitle());
        Assertions.assertEquals("변경 후 제목입니다.", changedPost.getTitle());
    }

    @Test
    @DisplayName("게시물 정보를 수정합니다.")
    void editPostTest() {
        // Given
        Post post = Post.builder()
                .title("변경 전 제목입니다.")
                .content("변경 전 내용입니다.")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("변경 후 제목입니다.")
                .content("변경 후 내용입니다.")
                .build();

        // When
        postService.editPost(post.getId(), postEdit);

        // Then
        Post updatedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("게시물이 존재하지 않습니다. id = " + post.getId()));
        Assertions.assertEquals("변경 후 제목입니다.", updatedPost.getTitle());
        Assertions.assertEquals("변경 후 내용입니다.", updatedPost.getContent());
    }

    @Test
    @DisplayName("게시물 단건을 삭제합니다.")
    void deletePostTest() {
        // Given
        Post post = Post.builder()
                .title("삭제가 될 게시물입니다.")
                .content("삭제가 될 게시물입니다.")
                .build();

        postRepository.save(post);

        // When
        postService.deletePost(post.getId());

        // Then
        Assertions.assertEquals(0, postRepository.count());
    }

    @Test
    @DisplayName("게시물 id로 조회시, 존재하지 않는 게시물일 경우 PostNotFound 예외가 발생해야 합니다.")
    void getPostByIdWhenNotExistTest() {
        // Given
        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        postRepository.save(post);

        // Then
        Assertions.assertThrows(PostNotFound.class, () -> {
            postService.getPostById(post.getId() + 1L);
        });
    }

    @Test
    @DisplayName("게시물 삭제시, 존재하지 않는 게시물일 경우 PostNotFound 예외가 발생해야 합니다.")
    void deletePostByIdWhenNotExistTest() {
        // Given
        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        postRepository.save(post);

        // Then
        Assertions.assertThrows(PostNotFound.class, () -> {
            postService.deletePost(post.getId() + 1L);
        });
    }

    @Test
    @DisplayName("게시물 수정시, 존재하지 않는 게시물일 경우 PostNotFound 예외가 발생해야 합니다.")
    void editPostByIdWhenNotExistTest() {
        // Given
        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("제목")
                .content("수정된 내용")
                .build();

        // Then
        Assertions.assertThrows(PostNotFound.class, () -> {
            postService.editPost(post.getId() + 1L, postEdit);
        });
    }
}