package com.dns.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private Long userId;
    private String name;
    private String email;
    private String password;
    private String profilePicture;
    private LocalDateTime createdAt;
    private String role;
    // private List<RoleDTO> roles;
    @JsonIgnore
    private transient MultipartFile profilePictureFile; // not persisted
}
