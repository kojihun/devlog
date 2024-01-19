package com.develop.devlog.service;

import com.develop.devlog.domain.Post;
import com.develop.devlog.domain.PostEditor;
import com.develop.devlog.exception.PostNotFound;
import com.develop.devlog.repository.PostRepository;
import com.develop.devlog.request.PostCreate;
import com.develop.devlog.request.PostEdit;
import com.develop.devlog.request.PostSearch;
import com.develop.devlog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;

    /**
     * 검색 조건에 부합하는 게시물 목록을 리턴합니다.
     *
     * @param postSearch 검색 조건을 정의한 객체
     * @return 게시물 목록을 담은 List<PostResponse> 객체
     */
    public List<PostResponse> getAllPosts(PostSearch postSearch) {
        return postRepository.getAllPosts(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 게시물 목록 중 id와 일치하는 게시물을 리턴합니다.
     * 만약 일치하는 정보가 없다면 Not Found Exception을 호출합니다.
     *
     * @param id 단건 조회를 위한 게시물의 고유한 PK
     * @throws PostNotFound 예외 처리
     * @return 게시물 단건 정보를 담은 PostResponse 객체
     */
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    /**
     * 신규 게시물을 작성합니다.
     *
     * @param postCreate 신규 게시물 작성시 필요한 변수를 정의한 객체
     */
    public void createPost(PostCreate postCreate) {
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        postRepository.save(post);
    }

    /**
     * 게시물을 수정합니다.
     * 만약, 게시물 id로 조회를 했을때 일치하는 정보가 없다면 Not Found Exception을 호출합니다.
     *
     * @param postId 수정할 게시물의 고유한 PK
     * @param postEdit 게시물 수정시 필요한 변수를 정의한 객체
     */
    @Transactional
    public void editPost(Long postId, PostEdit postEdit) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        PostEditor.PostEditorBuilder postEditorBuilder = post.toEditor();

        PostEditor postEditor = postEditorBuilder
                .title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();

        post.edit(postEditor);
    }

    /**
     * 게시물을 삭제합니다.
     * 만약, 게시물 id로 조회를 했을때 일치하는 정보가 없다면 Not Found Exception을 호출합니다.
     *
     * @param postId 삭제할 게시물의 고유한 PK
     * @throws PostNotFound 예외 처리
     */
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        postRepository.delete(post);
    }
}