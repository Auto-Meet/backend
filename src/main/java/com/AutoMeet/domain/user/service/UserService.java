package com.AutoMeet.domain.user.service;

import com.AutoMeet.domain.user.dto.request.UserRequestDto;
import com.AutoMeet.domain.user.dto.request.UserResponseDto;
import com.AutoMeet.domain.user.model.User;

public interface UserService {
    public UserResponseDto join(UserRequestDto request);
    public User findByEmail(String email);
}
