package com.AutoMeet.domain.comment.controller;

import com.AutoMeet.domain.comment.dto.request.DeleteCommentRequest;
import com.AutoMeet.domain.comment.dto.request.UpdateCommentRequest;
import com.AutoMeet.domain.comment.dto.request.CreateCommentRequest;
import com.AutoMeet.domain.comment.service.CommentService;
import com.AutoMeet.global.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
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

    @PatchMapping("/{commentId}")
    public void updateComment(@RequestBody UpdateCommentRequest request,
                              @PathVariable String commentId,
                              @AuthenticationPrincipal PrincipalDetails principal) {
        commentService.updateComment(request.getMeetingId(),
                commentId, request.getNewContent(), principal.getUser().getId());
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@RequestBody DeleteCommentRequest request,
                              @PathVariable String commentId,
                              @AuthenticationPrincipal PrincipalDetails principal) {
        commentService.deleteComment(request.getMeetingId(), commentId, principal.getUser().getId());
    }
}
