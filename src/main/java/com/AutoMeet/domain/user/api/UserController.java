package com.AutoMeet.domain.user.api;

import com.AutoMeet.domain.user.dto.request.UserRequestDto;
import com.AutoMeet.domain.user.dto.request.UserResponseDto;
import com.AutoMeet.domain.user.service.UserService;
import com.AutoMeet.domain.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<UserResponseDto> join(@RequestBody UserRequestDto request) {
        UserResponseDto response = userService.join(request);
        return ResponseEntity.ok(response);
    }
}