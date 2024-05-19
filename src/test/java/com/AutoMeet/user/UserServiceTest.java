package com.AutoMeet.user;

import com.AutoMeet.domain.user.dto.request.UserRequestDto;
import com.AutoMeet.domain.user.model.User;
import com.AutoMeet.domain.user.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @Transactional
    @DisplayName("회원가입")
    void join() {
        UserRequestDto request = new UserRequestDto("email@naver.com", "password", "kim", 20);
        userService.join(request);
        User user = userService.findByEmail(request.getEmail());

        assertThat(user.getEmail()).isEqualTo(request.getEmail());
        assertThat(user.getName()).isEqualTo(request.getName());
        assertThat(user.getAge()).isEqualTo(request.getAge());
    }
}