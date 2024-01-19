package com.develop.devlog.request;

import com.develop.devlog.exception.InvalidRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
public class PostCreate {
    @NotBlank(message = "제목을 입력해주세요.")
    private final String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private final String content;

    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void validate() {
        if (title.contains("비속어")) {
            throw new InvalidRequest("title", "제목에 비속어를 포함할 수 없습니다.");
        }
    }
}
