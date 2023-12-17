package com.example.financialtracker.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserResDto {

    private Long userId;

    private String username;

    private String fullName;

    private String email;

    public UserResDto(UserEntity userEntity) {
        this.userId = userEntity.getUserId();
        this.username = userEntity.getUsername();
        this.email = userEntity.getEmail();
        this.fullName = userEntity.getFullName();
    }
}
