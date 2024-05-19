package com.AutoMeet.domain.user.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User {

    @Id @GeneratedValue
    private Long id;

    private String email;

    private String password;

    private String name;

    private Integer age;

    private String roles;

    @Builder
    public User(String email, String password, String name, Integer age, String roles) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.age = age;
        this.roles = roles;
    }

    public List<String> getRoleList() {
        if (this.roles.length() > 0) {
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }
}
