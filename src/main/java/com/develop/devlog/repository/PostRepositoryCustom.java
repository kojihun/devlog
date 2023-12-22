package com.develop.devlog.repository;

import com.develop.devlog.domain.Post;
import com.develop.devlog.request.PostSearch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
