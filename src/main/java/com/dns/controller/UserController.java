package com.dns.controller;

import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dns.dto.UserDTO;
import com.dns.repository.UserRepository;
import com.dns.repository.entity.User;
import com.dns.service.impl.JwtService;
import com.dns.service.impl.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    UserService userService;
    UserRepository userRepository;
    JwtService jwtService;

    AuthenticationManager authenticationManager;
    ModelMapper mapper;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody UserDTO userDto) {
        try {
            User user = mapper.map(userDto, User.class);
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword()));

            if (authentication.isAuthenticated()) {
                log.info("User {} is authenticated", user.getName());
                User userEntity = userRepository.findByName(user.getName()).get();
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("user", userEntity);
                responseData.put("token", jwtService.generateToken(user.getName()));
                log.info("Token generated for {}", user.getName());
                return new ResponseEntity<>(responseData, HttpStatus.OK);
            }
        } catch (AuthenticationException e) {
            log.error("Login failed for user {}: {}", userDto.getName(), e.getMessage());
            log.error("{} StackTrace: {}", e.toString(), e.getStackTrace());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Invalid username or password.");
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }

        Map<String, Object> errorResponse = new HashMap<>();
        log.warn("Authentication failed for unknown reason for {}", userDto.getName());
        errorResponse.put("message", "Authentication failed for unknown reason.");
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDto) {
        log.info("Registration endpoint hit for email={}", userDto.getEmail());
        UserDTO registeredUser = userService.registerUser(userDto);
        log.info("Registration successful for username={}", registeredUser.getName());
        return ResponseEntity.ok(registeredUser);

    }
}
