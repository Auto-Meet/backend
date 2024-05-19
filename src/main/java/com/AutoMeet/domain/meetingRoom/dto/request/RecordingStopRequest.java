package com.AutoMeet.domain.meetingRoom.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordingStopRequest {
    private String meetingId;
}
