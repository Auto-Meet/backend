package com.AutoMeet.domain.comment.controller;

import com.AutoMeet.domain.comment.dto.request.CreateCommentRequest;
import com.AutoMeet.domain.comment.service.CommentService;
import com.AutoMeet.global.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("")
    public void createComment(@RequestBody CreateCommentRequest request,
                              @AuthenticationPrincipal PrincipalDetails principal) {
        commentService.createComment(request.getMeetingId(), request.getContent(), principal.getUser().getId());
    }
}
