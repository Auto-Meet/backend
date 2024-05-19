package com.AutoMeet.domain.user.service;

import com.AutoMeet.domain.user.dto.request.UserRequestDto;
import com.AutoMeet.domain.user.dto.request.UserResponseDto;
import com.AutoMeet.domain.user.exception.EmailExistException;
import com.AutoMeet.domain.user.exception.UserNotFoundException;
import com.AutoMeet.domain.user.model.User;
import com.AutoMeet.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserResponseDto join(UserRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailExistException(request.getEmail());
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .age(request.getAge())
                .roles("ROLE_USER")
                .build();

        userRepository.save(user);

        return new UserResponseDto(user.getName());
    }

    @Override
    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return user;
    }


    public User findById(Long id) {
        User user = userRepository.findById(id).
                orElseThrow(() -> new UserNotFoundException(id));
        return user;
    }
}
