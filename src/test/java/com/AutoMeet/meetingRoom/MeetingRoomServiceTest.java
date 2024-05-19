package com.AutoMeet.meetingRoom;

import com.AutoMeet.domain.meetingRoom.dto.request.CreateMeetingRequest;
import com.AutoMeet.domain.meetingRoom.model.MeetingRoom;
import com.AutoMeet.domain.meetingRoom.service.MeetingRoomService;
import com.AutoMeet.domain.user.dto.request.UserRequestDto;
import com.AutoMeet.domain.user.model.User;
import com.AutoMeet.domain.user.service.UserService;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class MeetingRoomServiceTest {

    @Autowired
    private MeetingRoomService meetingRoomService;

    @Autowired
    private UserService userService;

    @Test
    @Transactional
    @DisplayName("회의방 생성")
    void createMeetingRoom() {
        UserRequestDto userDto = new UserRequestDto("email@naver.com", "password", "kim", 20);
        userService.join(userDto);
        User user = userService.findByEmail(userDto.getEmail());

        CreateMeetingRequest request =
                new CreateMeetingRequest("meetingId", "password", "meetingTitle");

        String meetingId = meetingRoomService.createMeeting(request, user);

        MeetingRoom meetingRoom = meetingRoomService.findMeetingRoom(meetingId);

        assertThat(meetingRoom.getMeetingId()).isEqualTo(request.getMeetingId());
        assertThat(meetingRoom.getMeetingTitle()).isEqualTo(request.getMeetingTitle());
        assertThat(meetingRoom.getPassword()).isEqualTo(request.getPassword());
    }

    @Test
    @Transactional
    @DisplayName("회의방 참여 및 나가기")
    void joinMeetingRoom() {
        UserRequestDto userDto = new UserRequestDto("email@naver.com", "password", "kim", 20);
        userService.join(userDto);
        User user = userService.findByEmail(userDto.getEmail());

        CreateMeetingRequest request =
                new CreateMeetingRequest("meetingId", "password", "meetingTitle");

        String meetingId = meetingRoomService.createMeeting(request, user);

        UserRequestDto userDto2 = new UserRequestDto("email2@naver.com", "password2", "lee", 30);
        userService.join(userDto2);
        User user2 = userService.findByEmail(userDto2.getEmail());

        UserRequestDto userDto3 = new UserRequestDto("email3@naver.com", "password3", "park", 27);
        userService.join(userDto3);
        User user3 = userService.findByEmail(userDto3.getEmail());

        MeetingRoom meetingRoom = meetingRoomService.findMeetingRoom(meetingId);

        meetingRoomService.joinMeetingRoom(meetingRoom.get_id(), user2.getId());
        meetingRoomService.joinMeetingRoom(meetingRoom.get_id(), user3.getId());

        MeetingRoom findMeetingRoom = meetingRoomService.findMeetingRoom(meetingId);

        Assertions.assertThat(findMeetingRoom.getUserIds().contains(user.getId())).isTrue();
        assertTrue(findMeetingRoom.getUserIds().contains(user2.getId())); // user2가 meetingRoom에 join
        assertTrue(findMeetingRoom.getUserIds().contains(user3.getId())); // user3가 meetingRoom에 join

        meetingRoomService.disconnect(meetingRoom.get_id(), user3.getId());

        MeetingRoom disconnectRoom = meetingRoomService.findMeetingRoom(meetingId);

        assertFalse(disconnectRoom.getUserIds().contains(user3.getId())); // user3가 나가기
        assertTrue(disconnectRoom.getUserIds().contains(user2.getId()));
    }
}