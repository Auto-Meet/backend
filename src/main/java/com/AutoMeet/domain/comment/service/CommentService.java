package com.AutoMeet.domain.comment.service;

public interface CommentService {
    public void createComment(String meetingId, String content, Long userId);
}
