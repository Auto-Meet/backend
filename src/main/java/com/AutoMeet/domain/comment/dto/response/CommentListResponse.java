package com.AutoMeet.domain.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentListResponse {

    private String id;
    private String username;
    private String content;
    private LocalDateTime createdTime;
}
