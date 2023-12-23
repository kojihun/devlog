package com.develop.devlog.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostEdit {

    @NotBlank(message = "title을 입력해주세요.")
    private String title;

    @NotBlank(message = "content를 입력해주세요.")
    private String content;

    @Builder
    public PostEdit(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
