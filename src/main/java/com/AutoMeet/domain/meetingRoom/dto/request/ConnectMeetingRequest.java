package com.AutoMeet.domain.meetingRoom.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectMeetingRequest {

    private String meetingId;
    private String password;
}
