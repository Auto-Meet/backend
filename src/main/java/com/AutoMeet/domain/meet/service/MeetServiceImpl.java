package com.AutoMeet.domain.meet.service;

import com.AutoMeet.domain.comment.domain.Comment;
import com.AutoMeet.domain.comment.dto.response.CommentListResponse;
import com.AutoMeet.domain.meet.dto.request.UpdateMeetRequest;
import com.AutoMeet.domain.meet.dto.response.MeetListResponse;
import com.AutoMeet.domain.meet.dto.response.MeetingResponse;
import com.AutoMeet.domain.meet.exception.NotYourMeetingException;
import com.AutoMeet.domain.meet.model.Meet;
import com.AutoMeet.domain.meet.repository.MeetRepository;
import com.AutoMeet.domain.meetingRoom.exception.MeetingNotExistException;
import com.AutoMeet.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetServiceImpl implements MeetService {

    private final MeetRepository meetRepository;
    private final UserRepository userRepository;

    // @Value("${flask_url}")
    private String flask_url;

    @Override
    public String textSummarization(String recordingUrl) {
        RestTemplate restTemplate = new RestTemplate();
        String url = flask_url;

        HttpEntity<String> entity = new HttpEntity<>(recordingUrl);

        // flask 서버에 POST 요청 보내기
        // flask 서버에서 stt로 변환한 다음에 summarization 모델을 통한 요약본을 보내줌
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }

    @Override
    @Transactional
    public String save(String title, String summarization, List<Long> userIds) {

        ZoneId seoulZoneId = ZoneId.of("Asia/Seoul");
        ZonedDateTime seoulTime = ZonedDateTime.of(LocalDateTime.now(), seoulZoneId);

        Meet meet = Meet.builder()
                .title(title)
                .content(summarization)
                .userIds(userIds)
                .finishedTime(seoulTime.toLocalDateTime())
                .build();

        Meet meeting = meetRepository.save(meet);

        return meeting.get_id();
    }

    @Override
    public List<MeetListResponse> findMeets(Long userId) {
        List<Meet> meets = meetRepository.findByUserIdsContains(userId);
        List<MeetListResponse> meetList = meets.stream().map((meet) ->
                        new MeetListResponse(meet.get_id(), meet.getTitle(), meet.getContent(), meet.getFinishedTime()))
                .collect(Collectors.toList());
        return meetList;
    }

    @Override
    public MeetingResponse findOne(String meetingId, Long userId) {
        Meet meeting = findMeeting(meetingId);
        if (!meeting.getUserIds().contains(userId)) {
            throw new NotYourMeetingException(meetingId);
        }

        List<String> userNames = meeting.getUserIds().stream()
                .map(id -> userRepository.findNameById(id))
                .collect(Collectors.toList());

        List<CommentListResponse> comments = meeting.getComments().stream()
                .map(comment -> new CommentListResponse(comment.getId(),
                        userRepository.findNameById(comment.getUserId()),
                        comment.getContent() ,comment.getCreatedAt()))
                .collect(Collectors.toList());

        return new MeetingResponse(meeting.get_id(), meeting.getTitle(), meeting.getContent(),
                userNames, meeting.getFinishedTime(), comments);
    }

    @Override
    @Transactional
    public void updateMeeting(String meetingId, Long userId, UpdateMeetRequest request) {
        Meet meeting = findMeeting(meetingId);
        if (!meeting.getUserIds().contains(userId)) {
            throw new NotYourMeetingException(meetingId);
        }

        meeting.updateMeeting(request.getTitle(), request.getContent());

        meetRepository.save(meeting);
    }

    @Override
    public Meet findMeeting(String meetingId) {
        return meetRepository.findById(meetingId).orElseThrow(
                () -> new MeetingNotExistException(meetingId));
    }

    @Override
    public void saveMeeting(Meet meeting) {
        meetRepository.save(meeting);
    }
}
