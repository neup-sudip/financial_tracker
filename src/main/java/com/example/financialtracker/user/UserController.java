package com.example.financialtracker.user;

import com.example.financialtracker.wrapper.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @PutMapping()
    public ResponseEntity<ApiResponse<UserResDto>> updateUser(@Valid @RequestBody UserRegisterDto userRegisterDto, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        UserResDto userResDto = userService.updateUser(userRegisterDto, user.getUserId());
        return ResponseEntity.status(200).body(new ApiResponse<>(true, userResDto, "User updated !"));
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<UserResDto>> getUser(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        UserResDto userResDto = userService.getUserById(user.getUserId());
        return ResponseEntity.status(200).body(new ApiResponse<>(true, userResDto, "User fetched !"));
    }
}
