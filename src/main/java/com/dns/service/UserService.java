package com.dns.service;

import com.dns.repository.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User registerUser(User user);

    Optional<User> findByEmail(String email);

    List<User> getAllUsers();

    void deleteUser(Long userId);
}
