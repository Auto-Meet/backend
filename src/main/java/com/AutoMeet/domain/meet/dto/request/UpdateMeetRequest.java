package com.AutoMeet.domain.meet.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMeetRequest {

    private String title;
    private String content;
}

