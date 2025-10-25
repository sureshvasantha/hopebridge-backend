package com.dns.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private Long userId;
    private String name;
    private String email;
    private String profilePicture;
    private LocalDateTime createdAt;
    private Set<RoleDTO> roles;
}
