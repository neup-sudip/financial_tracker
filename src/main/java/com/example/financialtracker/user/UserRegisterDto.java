package com.example.financialtracker.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRegisterDto {
    @NotEmpty(message = "Username is required !")
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9_]{7,29}$", message = "username should be 8-30 char long, only include letter, number and _")
    private String username;

    @NotEmpty(message = "Password is required !")
    @Size(min = 8, message = "password should have at least 8 characters")
    private String password;

    @NotEmpty(message = "Full name is required !")
    @Size(min = 8, message = "full name should have at least 8 characters")
    private String fullName;

    @NotEmpty(message = "Email is required !")
    @Email(message = "Invalid email type !")
    private String email;
}
