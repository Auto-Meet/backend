package com.AutoMeet.domain.comment.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Comment {

    @Id
    private String id;
    private Long userId; // 작성자
    private String content;

    private LocalDateTime createdAt;

    @Builder
    public Comment(Long userId, String content, LocalDateTime createdAt) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
    }
}
