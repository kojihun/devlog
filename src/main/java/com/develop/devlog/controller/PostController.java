package com.develop.devlog.controller;

import com.develop.devlog.config.data.UserSession;
import com.develop.devlog.request.PostCreate;
import com.develop.devlog.request.PostEdit;
import com.develop.devlog.request.PostSearch;
import com.develop.devlog.response.PostResponse;
import com.develop.devlog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostService postService;

    @GetMapping("/foo")
    public Long foo(UserSession userSession) {
        log.info(String.valueOf(userSession.id));
        return userSession.id;
    }

    /**
     * 검색 조건에 부합하는 게시물 목록을 리턴합니다.
     *
     * @param postSearch 검색 조건을 정의한 객체
     * @return 게시물 목록을 담은 List<PostResponse> 객체
     */
    @GetMapping("/posts")
    public List<PostResponse> getAllPosts(@ModelAttribute PostSearch postSearch) {
        return postService.getAllPosts(postSearch);
    }

    /**
     * 게시물 목록 중 id와 일치하는 게시물을 리턴합니다.
     *
     * @param id 단건 조회를 위한 게시물의 고유한 PK
     * @return 게시물 단건 정보를 담은 PostResponse 객체
     */
    @GetMapping("/posts/{postId}")
    public PostResponse getPostById(@PathVariable(name = "postId") Long id) {
        return postService.getPostById(id);
    }

    /**
     * 신규 게시물을 작성합니다.
     *
     * @param request 신규 게시물 작성시 필요한 변수를 정의한 객체
     * @param authorization 인증된 사용자의 여부를 확인하기 위한 값
     */
    @PostMapping("/posts")
    public void createPost(@RequestBody @Valid PostCreate request, @RequestHeader String authorization) {
        if(authorization.equals("devlog")) {
            request.validate();
            postService.createPost(request);
        }
    }

    /**
     * 게시물을 수정합니다.
     * 
     * @param postId 수정할 게시물의 고유한 PK
     * @param postEdit 게시물 수정시 필요한 변수를 정의한 객체
     * @param authorization 인증된 사용자의 여부를 확인하기 위한 값
     */
    @PatchMapping("/posts/{postId}")
    public void editPost(@PathVariable Long postId, @RequestBody PostEdit postEdit, @RequestHeader String authorization) {
        if (authorization.equals("devlog")) {
            postService.editPost(postId, postEdit);
        }
    }

    /**
     * 게시물을 삭제합니다.
     * 
     * @param postId 삭제할 게시물의 고유한 PK
     * @param authorization 인증된 사용자의 여부를 확인하기 위한 값
     */
    @DeleteMapping("/posts/{postId}")
    public void deletePost(@PathVariable Long postId, @RequestHeader String authorization) {
        if (authorization.equals("devlog")) {
            postService.delete(postId);
        }
    }
}