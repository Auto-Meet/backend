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
    public String createMeeting(final CreateMeetingRequest createMeetingRequest, User user) {

        ZoneId seoulZoneId = ZoneId.of("Asia/Seoul");
        ZonedDateTime seoulTime = ZonedDateTime.of(LocalDateTime.now(), seoulZoneId);

        MeetingRoom meeting = MeetingRoom.builder()
                .meetingId(createMeetingRequest.getMeetingId())
                .password(createMeetingRequest.getPassword())
                .meetingTitle(createMeetingRequest.getMeetingTitle())
                .startedTime(seoulTime.toLocalDateTime())
                .build();

        meeting.getUserIds().add(user.getId());

        MeetingRoom meetingRoom = meetingRoomRepository.save(meeting);

        return meetingRoom.get_id();
    }

    @Override
    @Transactional
    public void joinMeetingRoom(String meetingId, Long userId) {
        MeetingRoom meeting = findMeetingRoom(meetingId);

        meeting.joinUser(userId);

        meetingRoomRepository.save(meeting);
    }

    @Override
    @Transactional
    public void disconnect(String meetingId, Long userId) {
        MeetingRoom meeting = findMeetingRoom(meetingId);

        meeting.deleteUser(userId);

        meetingRoomRepository.save(meeting);
    }

    @Override
    public Integer userCnt(String meetingId) {
        MeetingRoom meeting = findMeetingRoom(meetingId);

        List<Long> userIds = meeting.getUserIds();
        return userIds.size();
    }

    @Override
    @Transactional
    public void deleteMeeting(String meetingId) {
        MeetingRoom meeting = findMeetingRoom(meetingId);

        meetingRoomRepository.delete(meeting);
    }

    @Override
    public List<Long> findUsers(String meetingId) {
        MeetingRoom meeting = findMeetingRoom(meetingId);

        return meeting.getUserIds();
    }

    @Override
    public String findMeetingTitle(String meetingId) {
        return findMeetingRoom(meetingId).getMeetingTitle();
    }

    @Override
    public MeetingRoom findMeetingRoom(String meetingId) {
        return meetingRoomRepository.findById(meetingId).orElseThrow(
                () -> new MeetingNotExistException(meetingId));
    }
}
