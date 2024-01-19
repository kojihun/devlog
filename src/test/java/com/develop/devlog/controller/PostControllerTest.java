package com.develop.devlog.controller;

import com.develop.devlog.domain.Post;
import com.develop.devlog.repository.PostRepository;
import com.develop.devlog.request.PostCreate;
import com.develop.devlog.request.PostEdit;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("게시물을 등록합니다.")
    void createPostTest() throws Exception {
        // Given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String postCreateJson = objectMapper.writeValueAsString(postCreate);

        // Then
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "devlog")
                        .content(postCreateJson))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        Assertions.assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        Assertions.assertEquals("제목입니다.", post.getTitle());
        Assertions.assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("게시물 등록시 제목을 입력하지 않을 경우 400에러가 발생합니다.")
    void createPostWhenNullTitleTest() throws Exception {
        // Given
        PostCreate postCreate = PostCreate.builder()
                .content("내용입니다.")
                .build();

        String postCreateJson = objectMapper.writeValueAsString(postCreate);

        // Then
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "devlog")
                        .content(postCreateJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("게시물 id를 사용하여 1건을 조회합니다.")
    void getPostByIdTest() throws Exception {
        // Given
        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        postRepository.save(post);

        // Then
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value(post.getTitle()))
                .andExpect(jsonPath("$.content").value(post.getContent()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("모든 게시물을 조회합니다. (최신글 순서대로 정렬)")
    void getAllPostsTest() throws Exception {
        // Given
        List<Post> posts = IntStream.range(0, 30)
                .mapToObj(index -> Post.builder()
                        .title("제목: " + index)
                        .content("내용: " + index)
                        .build()).toList();

        postRepository.saveAll(posts);

        // Then
        mockMvc.perform(get("/posts?page=1&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(10)))
                .andExpect(jsonPath("$[0].title").value("제목: 29"))
                .andExpect(jsonPath("$[0].content").value("내용: 29"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("게시물 정보를 수정합니다.")
    void editPostTest() throws Exception{
        // Given
        Post post = Post.builder()
                .title("변경 전 제목입니다..")
                .content("변경 전 내용입니다.")
                .build();

        postRepository.save(post);

        // When
        PostEdit postEdit = PostEdit.builder()
                .title("변경 후 제목입니다.")
                .content("변경 후 내용입니다.")
                .build();

        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "devlog")
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // Then
        Post updatedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("게시물이 존재하지 않습니다. id = " + post.getId()));

        Assertions.assertEquals("변경 후 제목입니다.", updatedPost.getTitle());
        Assertions.assertEquals("변경 후 내용입니다.", updatedPost.getContent());
    }

    @Test
    @DisplayName("게시물 단건을 삭제합니다.")
    void deletePostTest() throws Exception {
        // Given
        Post post = Post.builder()
                .title("삭제가 될 게시물입니다.")
                .content("삭제가 될 게시물입니다.")
                .build();

        postRepository.save(post);

        // Then
        mockMvc.perform(delete("/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "devlog"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("게시물 id로 조회시, 존재하지 않는 게시물일 경우 예외가 발생해야 합니다.")
    void getPostByIdWhenNotExistTest() throws Exception {
        // Then
        mockMvc.perform(delete("/posts/{postId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "devlog"))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("게시물 수정시, 존재하지 않는 게시물일 경우 예외가 발생해야 합니다.")
    void editPostByIdWhenNotExistTest() throws Exception {
        // Given
        PostEdit postEdit = PostEdit.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        // Then
        mockMvc.perform(patch("/posts/{postId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "devlog")
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("게시물 등록시 제목에 비속어 포함시 예외가 발생합니다.")
    void createPostsWhenBadWordsInTitleTest() throws Exception {
        // Given
        PostCreate request = PostCreate.builder()
                .title("비속어 제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // Then
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "devlog")
                        .content(json))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }
}