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

    public UserResDto getUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return new UserResDto(user.get());
        } else {
            throw new CustomException(NOT_FOUND, 404);
        }
    }

    public UserResDto getUserByUsernameAndPassword(String username, String password) {
        Optional<User> user = userRepository.findUserByUsernameAndPassword(username, password);
        if (user.isPresent()) {
            return new UserResDto(user.get());
        } else {
            throw new CustomException(NOT_FOUND, 404);
        }
    }

    public UserResDto createUser(UserRegisterDto userRegisterDto) {
        Optional<User> userEmail = userRepository.findUserByEmail(userRegisterDto.getEmail());
        if (userEmail.isPresent()) {
            throw new CustomException("Email already exist !", 404);
        }

        Optional<User> userUsername = userRepository.findUserByUsername(userRegisterDto.getUsername());
        if (userUsername.isPresent()) {
            throw new CustomException("Username already exist !", 404);
        }

        User user = new User(userRegisterDto);
        User newUser = userRepository.save(user);
        return new UserResDto(newUser);
    }

    public UserResDto updateUser(UserRegisterDto userRegisterDto, Long userId) {
        Optional<User> userEmail = userRepository.findByNotIdAndEmail(userId, userRegisterDto.getEmail());
        if (userEmail.isPresent()) {
            throw new CustomException("Email already exist !", 404);
        }

        Optional<User> userUsername = userRepository.findByNotIdAndUsername(userId, userRegisterDto.getUsername());
        if (userUsername.isPresent()) {
            throw new CustomException("Username already exist !", 404);
        }

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new CustomException(NOT_FOUND, 404);
        }

        User prevUser = optionalUser.get();
        prevUser.setEmail(userRegisterDto.getEmail());
        prevUser.setPassword(userRegisterDto.getPassword());
        prevUser.setUsername(userRegisterDto.getUsername());
        prevUser.setFullName(userRegisterDto.getFullName());
        User newUser = userRepository.save(prevUser);

        return new UserResDto(newUser);
    }
}
