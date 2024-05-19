package com.AutoMeet.domain.userImg.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "userImg")
@Getter
public class UserImg {

    @Id
    private String _id;
    private Long userId;
    private String imgUrl;
    private String imgName;

    private LocalDateTime createdAt;

    @Builder
    public UserImg(Long userId, String imgUrl, String imgName) {
        this.userId = userId;
        this.imgUrl = imgUrl;
        this.imgName = imgName;
        this.createdAt = LocalDateTime.now();
    }
}
