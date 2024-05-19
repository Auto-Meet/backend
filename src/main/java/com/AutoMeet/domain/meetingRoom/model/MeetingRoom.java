package com.AutoMeet.domain.meetingRoom.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "meetingRoom")
@Getter
public class MeetingRoom {

    @Id
    private String _id;
    private String meetingId;
    private String meetingTitle;
    private String password;
    private List<Long> userIds;

    private LocalDateTime startedTime;

    @Builder
    public MeetingRoom(String meetingId, String password, String meetingTitle, LocalDateTime startedTime) {
        this.meetingId = meetingId;
        this.password = password;
        this.meetingTitle = meetingTitle;
        this.userIds = new ArrayList<>();
        this.startedTime = startedTime;
    }

    public void joinUser(Long userId) {
        userIds.add(userId);
    }

    public void deleteUser(Long userId) {
        userIds.remove(userId);
    }
}
