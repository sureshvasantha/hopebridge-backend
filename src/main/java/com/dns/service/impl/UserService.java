package com.dns.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dns.dto.UserDTO;
import com.dns.exception.InvalidRoleException;
import com.dns.exception.UserInfoAlreadyExistException;
import com.dns.repository.RoleRepository;
import com.dns.repository.UserRepository;
import com.dns.repository.entity.Role;
import com.dns.repository.entity.User;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    ModelMapper mapper;

    public User registerUser(UserDTO userDto) {
        log.info("Register request received for username={}, email={}", userDto.getName(), userDto.getEmail());
        if (userRepository.findByName(userDto.getName()).isPresent()) {
            log.error("Registration failed: Username {} already exists", userDto.getName());
            throw new UserInfoAlreadyExistException(
                    String.format("Username %s already exists", userDto.getName()));
        }
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            log.error("Registration failed: Email {} already exists", userDto.getEmail());
            throw new UserInfoAlreadyExistException(
                    String.format("Email %s already exists", userDto.getEmail()));
        }
        if (!userDto.getRole().matches("(ADMIN|DONOR)")) {
            log.error(
                    "Registration failed: Invalid Role mentioned. Available roles ADMIN, DONOR. Provided: {}",
                    userDto.getRole());
            throw new InvalidRoleException(
                    String.format("Invalid Role %s mentioned. Available roles ADMIN, DONOR.",
                            userDto.getRole()));
        }

        User user = mapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setCreatedAt(LocalDateTime.now());

        Role role = roleRepository.findByRoleName(userDto.getRole());
        user.setRoles((List.of(role)));
        log.info("User {} object created successfully", user.getName());
        return user;
    }

    public UserDTO registerAdmin(UserDTO userDto) {
        // TODO: Same as registerUser but call only when curr user is ADMIN
        return null;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
