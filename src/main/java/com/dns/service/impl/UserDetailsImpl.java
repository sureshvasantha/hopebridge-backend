package com.dns.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.dns.repository.entity.Role;

import lombok.Getter;

@Getter
public class UserDetailsImpl implements UserDetails {
    private Long userId;
    private String name;
    private String password;
    private List<SimpleGrantedAuthority> allRoles;

    public UserDetailsImpl(Long userId, String name, String password, List<Role> allRoles) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.allRoles = allRoles.stream().map((role) -> new SimpleGrantedAuthority(role.getRoleName())).toList();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return allRoles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }
}