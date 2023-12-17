package com.example.financialtracker.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findUserByUsername(String username);

    @Query(value = "SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1", nativeQuery = true)
    Optional<UserEntity> findUserByUsernameAndPassword(String username, String password);

    @Query(value = "SELECT * FROM users WHERE user_id <> :userId AND username = :username LIMIT 1", nativeQuery = true)
    Optional<UserEntity> findByNotIdAndUsername(Long userId, String username);

    Optional<UserEntity> findUserByEmail(String email);

    @Query(value = "SELECT * FROM users WHERE user_id <> :userId AND email = :email LIMIT 1", nativeQuery = true)
    Optional<UserEntity> findByNotIdAndEmail(Long userId, String email);
}
