package com.AutoMeet.domain.meet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetListResponse {

    private String meetingId;
    private String title;
    private String content;
    private LocalDateTime meetingTime;
}

