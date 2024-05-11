package com.AutoMeet.domain.meetingRoom.service;

import com.AutoMeet.domain.meetingRoom.dto.request.CreateMeetingRequest;
import com.AutoMeet.domain.meetingRoom.exception.MeetingNotExistException;
import com.AutoMeet.domain.meetingRoom.model.MeetingRoom;
import com.AutoMeet.domain.meetingRoom.repository.MeetingRoomRepository;
import com.AutoMeet.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingRoomServiceImpl implements MeetingRoomService{

    private final MeetingRoomRepository meetingRoomRepository;

    @Override
    @Transactional
    public void createMeeting(String meetingId, final CreateMeetingRequest createMeetingRequest, User user) {

        ZoneId seoulZoneId = ZoneId.of("Asia/Seoul");
        ZonedDateTime seoulTime = ZonedDateTime.of(LocalDateTime.now(), seoulZoneId);

        MeetingRoom meeting = MeetingRoom.builder()
                .meetingId(meetingId)
                .password(createMeetingRequest.getPassword())
                .startedTime(seoulTime.toLocalDateTime())
                .build();

        meeting.getUserIds().add(user.getId());

        meetingRoomRepository.save(meeting);
    }

    @Override
    @Transactional
    public void joinMeetingRoom(String meetingId, Long userId) {
        MeetingRoom meeting = findMeeting(meetingId);

        meeting.getUserIds().add(userId);
    }

    @Override
    @Transactional
    public void disconnect(String meetingId, User user) {
        MeetingRoom meeting = findMeeting(meetingId);

        List<Long> userIds = meeting.getUserIds();
        userIds.remove(user.getId());

        meetingRoomRepository.save(meeting);
    }

    @Override
    public Integer userCnt(String meetingId) {
        MeetingRoom meeting = findMeeting(meetingId);

        List<Long> userIds = meeting.getUserIds();
        return userIds.size();
    }

    @Override
    @Transactional
    public void deleteMeeting(String meetingId) {
        MeetingRoom meeting = findMeeting(meetingId);

        meetingRoomRepository.delete(meeting);
    }

    public MeetingRoom findMeeting(String meetingId) {
        return meetingRoomRepository.findById(meetingId).orElseThrow(
                () -> new MeetingNotExistException(meetingId));
    }
}