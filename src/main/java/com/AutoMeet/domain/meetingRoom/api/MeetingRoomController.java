package com.AutoMeet.domain.meetingRoom.api;

import com.AutoMeet.domain.meetingRoom.dto.request.ConnectMeetingRequest;
import com.AutoMeet.domain.meetingRoom.dto.request.CreateMeetingRequest;
import com.AutoMeet.domain.meetingRoom.dto.request.RecordingRequest;
import com.AutoMeet.domain.meetingRoom.dto.response.CreateMeetingResponse;
import com.AutoMeet.domain.meetingRoom.exception.MeetingNotExistException;
import com.AutoMeet.domain.meetingRoom.exception.SessionNotExistException;
import com.AutoMeet.domain.meetingRoom.service.MeetingRoomService;
import com.AutoMeet.global.auth.PrincipalDetails;
import io.openvidu.java.client.*;
import jakarta.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@CrossOrigin(origins = "*")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/sessions")
public class MeetingRoomController {

    @Value("${OPENVIDU_URL}")
    private String OPENVIDU_URL;

    @Value("${OPENVIDU_SECRET}")
    private String OPENVIDU_SECRET;

    private OpenVidu openvidu;

    private final MeetingRoomService meetingRoomService;

    private Map<String, String> meetingConnection = new HashMap<>();

    // meetingId와 Session을 매칭
    private Map<String, String> sessionRoomConvert = new HashMap<>();

    // 녹화 sessions
    private Map<String, Boolean> sessionRecordings = new ConcurrentHashMap<>();

    // 회의에 여러 개의 녹화를 저장
    private Map<String, String> sessionRecordMap = new HashMap<>();

    @PostConstruct
    public void init() {
        this.openvidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
    }

    @PostMapping("")
    public ResponseEntity<CreateMeetingResponse> initializeSession(@RequestBody CreateMeetingRequest createMeetingRequest,
                                                                   @AuthenticationPrincipal PrincipalDetails principal)
            throws OpenViduJavaClientException, OpenViduHttpException {

        meetingRoomService.createMeeting(createMeetingRequest.getMeetingId(), createMeetingRequest, principal.getUser());
        String meetingId = createMeetingRequest.getMeetingId();
        String meetingPw = createMeetingRequest.getPassword();

        // meetingRoom마다 비밀번호를 저장해서 관리
        this.meetingConnection.put(meetingId, meetingPw);

        String StringUUID = UUID.randomUUID().toString();
        String RoomUUID = StringUUID.replaceAll("[^a-zA-Z0-9]", "");

        this.sessionRoomConvert.put(meetingId, RoomUUID);

        Map<String, Object> mettingMap = new HashMap<>();
        mettingMap.put("customSessionId", this.sessionRoomConvert.get(meetingId));

        SessionProperties properties = SessionProperties.fromJson(mettingMap).build();

        Session session = openvidu.createSession(properties);

        return new ResponseEntity<>(new CreateMeetingResponse(meetingId, meetingPw), HttpStatus.OK);
    }

    @PostMapping("/connection")
    public ResponseEntity<String> createConnection(@RequestBody ConnectMeetingRequest connectMeetingRequest,
                                                   @AuthenticationPrincipal PrincipalDetails principal)
            throws OpenViduJavaClientException, OpenViduHttpException {

        String meetingId = connectMeetingRequest.getMeetingId();
        String sessionId = sessionRoomConvert.get(meetingId);

        Session session = openvidu.getActiveSession(sessionId);
        if (session == null) {
            throw new SessionNotExistException(sessionId);
        }

        // 비밀번호 검증 필요

        Long userId = principal.getUser().getId();
        meetingRoomService.joinMeetingRoom(meetingId, userId);

        Map<String, String> meetingMap = new HashMap<>();
        meetingMap.put("customSessionId", this.sessionRoomConvert.get(meetingId));

        ConnectionProperties properties = ConnectionProperties.fromJson(meetingMap).build();
        Connection connection = session.createConnection(properties); // session과의 연결

        return new ResponseEntity<>(connection.getToken(), HttpStatus.OK);
    }

    @DeleteMapping("disconnection/{encodeRoomId}/{encodePlayerNickname}")
    public ResponseEntity<String> disconnectionRoom(@PathVariable String encodeRoomId,
                                                    @AuthenticationPrincipal PrincipalDetails principal) throws UnsupportedEncodingException {
        String meetingId = URLDecoder.decode(encodeRoomId, StandardCharsets.UTF_8);

        meetingRoomService.disconnect(meetingId, principal.getUser());

        Integer userCnt = meetingRoomService.userCnt(meetingId);
        if (userCnt == 0) {
            // 모두 나가면 회의실을 삭제한다.
            meetingRoomService.deleteMeeting(meetingId); // db에서 삭제
            this.meetingConnection.remove(meetingId); // 회의실 비밀번호 map 삭제
            this.sessionRoomConvert.remove(meetingId); // meeting 세션 삭제
        }

        return new ResponseEntity<>("Leave 처리 성공", HttpStatus.OK);
    }

    @PostMapping("recording/start")
    public ResponseEntity<?> startRecording(@RequestBody RecordingRequest recordingRequest) {

        String meetingId = recordingRequest.getMeetingId();

        String sessionId = sessionRoomConvert.get(meetingId); // meetingId -> sessionId

        if (sessionId == null) {
            throw new MeetingNotExistException(meetingId);
        }

        Recording.OutputMode outputMode = Recording.OutputMode.COMPOSED;

        boolean hasAudio = true;
        boolean hasVideo = false;

        RecordingProperties properties = new RecordingProperties.Builder()
                .outputMode(outputMode)
                .hasAudio(hasAudio)
                .hasVideo(hasVideo)
                .build();

        try {
            Recording recording = this.openvidu.startRecording(sessionId, properties);
            this.sessionRecordings.put(sessionId, true);
            this.sessionRecordMap.put(sessionId, recording.getId());

            return new ResponseEntity<>("Recording Start", HttpStatus.OK);
        } catch (OpenViduJavaClientException | OpenViduHttpException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}