package com.example.financialtracker.auth;

import com.example.financialtracker.security.JwtService;
import com.example.financialtracker.user.UserLoginDto;
import com.example.financialtracker.user.UserRegisterDto;
import com.example.financialtracker.user.UserResDto;
import com.example.financialtracker.user.UserService;
import com.example.financialtracker.wrapper.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${cookie.expire.date}")
    private int COOKIE_EXPIRE;

    private final JwtService jwtService;

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResDto>> registerUser(@Valid @RequestBody UserRegisterDto userRegisterDto, HttpServletResponse response){
        UserResDto user = userService.createUser(userRegisterDto);
        return ResponseEntity.status(200).body(new ApiResponse<>(true, user, "User created !"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResDto>> loginUser(@Valid @RequestBody UserLoginDto userLoginDto, HttpServletResponse response) {
        UserResDto user = userService.getUserByUsernameAndPassword(userLoginDto.getUsername(), userLoginDto.getPassword());

        String token = jwtService.generateToken(user);
        final Cookie cookie = new Cookie("auth", token);
        cookie.setSecure(false);
        cookie.setHttpOnly(false);
        cookie.setMaxAge(COOKIE_EXPIRE);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.status(200).body(new ApiResponse<>(true, user, "Login success !"));
    }
}
