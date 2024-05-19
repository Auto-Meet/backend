package com.AutoMeet.domain.meetingRoom.service;

import com.AutoMeet.domain.meetingRoom.dto.request.CreateMeetingRequest;
import com.AutoMeet.domain.meetingRoom.model.MeetingRoom;
import com.AutoMeet.domain.user.model.User;

import java.util.List;

public interface MeetingRoomService {
    public String createMeeting(final CreateMeetingRequest createMeetingRequest, User user);
    public void joinMeetingRoom(String meetingId, Long userId);
    public void disconnect(String meetingId, Long userId);
    public Integer userCnt(String meetingId);
    public void deleteMeeting(String meetingId);
    public List<Long> findUsers(String meetingId);
    public String findMeetingTitle(String meetingId);
    public MeetingRoom findMeetingRoom(String meetingId);
}
