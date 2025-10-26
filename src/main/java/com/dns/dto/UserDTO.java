package com.dns.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
}
