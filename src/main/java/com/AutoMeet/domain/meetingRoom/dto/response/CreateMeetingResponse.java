package com.AutoMeet.domain.meetingRoom.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMeetingResponse {

    private String meetingId;
    private String password;
}