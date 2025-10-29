package com.dns.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dns.dto.UserDTO;
import com.dns.exception.FileStorageException;
import com.dns.exception.InvalidRoleException;
import com.dns.exception.ResourceNotFoundException;
import com.dns.exception.UserInfoAlreadyExistException;
import com.dns.repository.RoleRepository;
import com.dns.repository.UserRepository;
import com.dns.repository.entity.Role;
import com.dns.repository.entity.User;
import com.dns.service.S3Service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final S3Service s3Service;
    PasswordEncoder passwordEncoder;
    ModelMapper mapper;

    public UserDTO registerUser(UserDTO userDto) {
        log.info("Register request received for username={}, email={}", userDto.getName(), userDto.getEmail());

        userRepository.findByName(userDto.getName()).ifPresent(u -> {
            log.error("Registration failed: Username {} already exists", userDto.getName());
            throw new UserInfoAlreadyExistException(
                    String.format("Username %s already exists", userDto.getName()));
        });

        userRepository.findByEmail(userDto.getEmail()).ifPresent(u -> {
            log.error("Registration failed: Email {} already exists", userDto.getEmail());
            throw new UserInfoAlreadyExistException(
                    String.format("Email %s already exists", userDto.getEmail()));
        });

        String roleName = userDto.getRole().toUpperCase();
        if (!List.of("ADMIN", "DONOR").contains(roleName)) {
            log.error("Registration failed: Invalid Role '{}'. Available roles: ADMIN, DONOR", roleName);
            throw new InvalidRoleException(
                    String.format("Invalid Role %s mentioned. Available roles: ADMIN, DONOR.", roleName));
        }

        Role role = roleRepository.findByRoleName(roleName);
        if (role == null) {
            log.error("Role '{}' not found in DB", roleName);
            throw new ResourceNotFoundException("Role not found: " + roleName);
        }

        User user = mapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setRoles(List.of(role));

        // --- Upload profile picture if provided ---
        if (userDto.getProfilePictureFile() != null) {
            try {
                log.info("Uploading profile picture for user {}", userDto.getName());
                // Assuming controller converts file to MultipartFile and sends via DTO
                String uploadedUrl = s3Service.uploadFile(
                        userDto.getProfilePictureFile(), "profiles");
                user.setProfilePicture(uploadedUrl);
                log.info("Profile picture uploaded to S3: {}", uploadedUrl);
            } catch (IOException e) {
                log.error("Failed to upload profile picture for {}: {}", userDto.getName(), e.getMessage());
                throw new FileStorageException(
                        "Could not upload profile picture for " + userDto.getName() + " : " + e.getMessage());
            }
        }

        User savedUser = userRepository.save(user);
        log.info("User '{}' registered successfully with role '{}'", savedUser.getName(), roleName);

        return mapper.map(savedUser, UserDTO.class);
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
