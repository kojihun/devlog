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
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/foo")
    public String foo(UserSession userSession) {
        log.info(userSession.name);
        return userSession.name;
    }

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request, @RequestHeader String authorization) {
        if(authorization.equals("devlog")) {
            request.validate();
            postService.write(request);
        }
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable(name = "postId") Long id) {
        return postService.get(id);
    }

    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable Long postId, @RequestBody PostEdit postEdit, @RequestHeader String authorization) {
        if (authorization.equals("devlog")) {
            postService.edit(postId, postEdit);
        }
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId, @RequestHeader String authorization) {
        if (authorization.equals("devlog")) {
            postService.delete(postId);
        }
    }
}
