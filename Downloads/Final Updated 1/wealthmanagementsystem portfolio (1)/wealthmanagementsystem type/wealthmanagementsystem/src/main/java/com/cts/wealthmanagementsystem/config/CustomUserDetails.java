package com.cts.wealthmanagementsystem.config;

import org.springframework.security.core.userdetails.UserDetails;
import com.cts.wealthmanagementsystem.entity.Client;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final Client user;

    public CustomUserDetails(Client user) {
        this.user = user;
    }

    public Client getClient() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Assign role based on email, assuming 'admin@wealthy.com' is the admin email
        String assignedRole = user.getEmailAddress().equalsIgnoreCase("admin@wealthy.com") ? "ROLE_ADMIN" : "ROLE_USER";
        System.out.println("Assigned Role for " + user.getEmailAddress() + ": " + assignedRole); // Added for debugging
        return List.of(new SimpleGrantedAuthority(assignedRole));
    }

    @Override
    public String getPassword() {
        return user.getPassWord();
    }

    @Override
    public String getUsername() {
        return user.getEmailAddress();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return user.isApproved(); }
}