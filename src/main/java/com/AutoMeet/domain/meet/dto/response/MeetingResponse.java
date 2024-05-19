package com.AutoMeet.domain.meet.dto.response;

import com.AutoMeet.domain.comment.dto.response.CommentListResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingResponse {

    private String meetingId;
    private String title;
    private String content;
    private List<String> userNames;
    private LocalDateTime meetingTime;
    private List<CommentListResponse> comments;
}

