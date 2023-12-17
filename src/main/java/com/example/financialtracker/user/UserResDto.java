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

    public UserResDto(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
    }
}
