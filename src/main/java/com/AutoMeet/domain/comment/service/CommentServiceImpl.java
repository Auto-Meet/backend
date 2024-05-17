package com.AutoMeet.domain.comment.service;

import com.AutoMeet.domain.comment.Comment;
import com.AutoMeet.domain.comment.repository.CommentRepository;
import com.AutoMeet.domain.meet.exception.NotYourMeetingException;
import com.AutoMeet.domain.meet.model.Meet;
import com.AutoMeet.domain.meet.service.MeetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final MeetService meetService;

    @Override
    @Transactional
    public void createComment(String meetingId, String content, Long userId) {

        if (!IsMeetingUser(userId, meetingId)) {
            throw new NotYourMeetingException(meetingId);
        }

        ZoneId seoulZoneId = ZoneId.of("Asia/Seoul");
        ZonedDateTime seoulTime = ZonedDateTime.of(LocalDateTime.now(), seoulZoneId);

        Comment comment = Comment.builder()
                .userId(userId)
                .content(content)
                .meetingId(meetingId)
                .createdAt(seoulTime.toLocalDateTime())
                .build();

        commentRepository.save(comment);
    }

    public Boolean IsMeetingUser(Long userId, String meetingId) {
        Meet meeting = meetService.findMeeting(meetingId);

        return meeting.getUserIds().contains(userId);
    }

}