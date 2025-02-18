package com.remopark.service;

import com.remopark.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user);
    User getUserById(Long id);
    Optional<User> getUserByUsername(String username);
    List<User> getAllUsers();
    User updateUser(User user);
    void deleteUser(Long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    long getUserCount();
    Page<User> getAllUsersPageable(Pageable pageable);
    Page<User> searchUsers(String search, Pageable pageable);
} 