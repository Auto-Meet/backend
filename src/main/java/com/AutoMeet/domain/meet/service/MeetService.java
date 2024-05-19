package com.AutoMeet.domain.meet.service;

import com.AutoMeet.domain.meet.dto.request.UpdateMeetRequest;
import com.AutoMeet.domain.meet.dto.response.MeetListResponse;
import com.AutoMeet.domain.meet.dto.response.MeetingResponse;
import com.AutoMeet.domain.meet.model.Meet;

import java.util.List;

public interface MeetService {
    public String textSummarization(String recordingUrl);
    public String save(String title, String summarization, List<Long> userIds);
    public List<MeetListResponse> findMeets(Long userId);
    public MeetingResponse findOne(String meetingId, Long userId);
    public void updateMeeting(String meetingId, Long userId, UpdateMeetRequest request);
    public Meet findMeeting(String meetingId);
    public void saveMeeting(Meet meeting);
}
