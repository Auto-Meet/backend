package com.AutoMeet.domain.user.repository;

import com.AutoMeet.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
    String findNameById(Long userId);
}