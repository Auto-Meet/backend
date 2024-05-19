package com.AutoMeet.comment;

import com.AutoMeet.domain.comment.domain.Comment;
import com.AutoMeet.domain.comment.service.CommentService;
import com.AutoMeet.domain.meet.model.Meet;
import com.AutoMeet.domain.meet.service.MeetService;
import com.AutoMeet.domain.user.dto.request.UserRequestDto;
import com.AutoMeet.domain.user.model.User;
import com.AutoMeet.domain.user.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@SpringBootTest
public class CommentServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private MeetService meetService;

    @Autowired
    private CommentService commentService;

    @Test
    @Transactional
    @DisplayName("회의 보고서에 댓글 작성")
    void createComment() {
        UserRequestDto userDto = new UserRequestDto("email@naver.com", "password", "kim", 20);
        userService.join(userDto);
        User user = userService.findByEmail(userDto.getEmail());

        List<Long> userIds = new ArrayList<>();
        userIds.add(user.getId());

        UserRequestDto userDto2 = new UserRequestDto("email2@naver.com", "password2", "lee", 30);
        userService.join(userDto2);
        User user2 = userService.findByEmail(userDto2.getEmail());

        UserRequestDto userDto3 = new UserRequestDto("email3@naver.com", "password3", "park", 27);
        userService.join(userDto3);
        User user3 = userService.findByEmail(userDto3.getEmail());

        userIds.add(user2.getId());

        String meetId = meetService.save("title", "content", userIds);

        commentService.createComment(meetId, "댓글", user.getId());

        commentService.createComment(meetId, "댓글1", user.getId());
        commentService.createComment(meetId, "댓글2", user.getId());
        commentService.createComment(meetId, "댓글3", user.getId());

        Meet meeting = meetService.findMeeting(meetId);

        List<Comment> comments = meeting.getComments();

        List<String> commentList = comments.stream().map(comment -> comment.getContent()).collect(Collectors.toList());
        Assertions.assertTrue(commentList.contains("댓글"));
        Assertions.assertTrue(commentList.contains("댓글1"));
    }
}
