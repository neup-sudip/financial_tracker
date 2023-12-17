package com.example.financialtracker.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);

    @Query(value = "SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1", nativeQuery = true)
    Optional<User> findUserByUsernameAndPassword(String username, String password);

    @Query(value = "SELECT * FROM users WHERE user_id <> :userId AND username = :username LIMIT 1", nativeQuery = true)
    Optional<User> findByNotIdAndUsername(Long userId, String username);

    Optional<User> findUserByEmail(String email);

    @Query(value = "SELECT * FROM users WHERE user_id <> :userId AND email = :email LIMIT 1", nativeQuery = true)
    Optional<User> findByNotIdAndEmail(Long userId, String email);
}
