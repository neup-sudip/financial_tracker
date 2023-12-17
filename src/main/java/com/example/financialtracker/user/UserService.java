package com.example.financialtracker.user;

import com.example.financialtracker.exception.CustomException;
import com.example.financialtracker.wrapper.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private static final String NOT_FOUND = "User not found !";

    public ResponseEntity<ApiResponse<UserResDto>> getUserById(Long userId) {
        Optional<UserEntity> user = userRepository.findById(userId);
        if (user.isPresent()) {
            UserResDto userResDto = new UserResDto(user.get());
            return ResponseEntity.status(200).body(new ApiResponse<>(true, userResDto, "User fetched !"));
        } else {
            throw new CustomException(NOT_FOUND, 404);
        }
    }

    public UserResDto getUserByUsernameAndPassword(String username, String password) {
        Optional<UserEntity> user = userRepository.findUserByUsernameAndPassword(username, password);
        if (user.isPresent()) {
            return new UserResDto(user.get());
        } else {
            throw new CustomException(NOT_FOUND, 404);
        }
    }

    public UserResDto createUser(UserRegisterDto userRegisterDto) {
        Optional<UserEntity> userEmail = userRepository.findUserByEmail(userRegisterDto.getEmail());
        if (userEmail.isPresent()) {
            throw new CustomException("Email already exist !", 404);
        }

        Optional<UserEntity> userUsername = userRepository.findUserByUsername(userRegisterDto.getUsername());
        if (userUsername.isPresent()) {
            throw new CustomException("Username already exist !", 404);
        }

        UserEntity userEntity = new UserEntity(userRegisterDto);
        UserEntity newUser = userRepository.save(userEntity);
        return new UserResDto(newUser);
    }

    public ResponseEntity<ApiResponse<UserResDto>> updateUser(UserRegisterDto userRegisterDto, Long userId) {
        Optional<UserEntity> userEmail = userRepository.findByNotIdAndEmail(userId, userRegisterDto.getEmail());
        if (userEmail.isPresent()) {
            throw new CustomException("Email already exist !", 404);
        }

        Optional<UserEntity> userUsername = userRepository.findByNotIdAndUsername(userId, userRegisterDto.getUsername());
        if (userUsername.isPresent()) {
            throw new CustomException("Username already exist !", 404);
        }

        Optional<UserEntity> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new CustomException(NOT_FOUND, 404);
        }

        UserEntity prevUser = optionalUser.get();
        prevUser.setEmail(userRegisterDto.getEmail());
        prevUser.setPassword(userRegisterDto.getPassword());
        prevUser.setUsername(userRegisterDto.getUsername());
        prevUser.setFullName(userRegisterDto.getFullName());
        UserEntity newUser = userRepository.save(prevUser);

        UserResDto userResDto = new UserResDto(newUser);
        return ResponseEntity.status(200).body(new ApiResponse<>(true, userResDto, "User created !"));
    }
}
