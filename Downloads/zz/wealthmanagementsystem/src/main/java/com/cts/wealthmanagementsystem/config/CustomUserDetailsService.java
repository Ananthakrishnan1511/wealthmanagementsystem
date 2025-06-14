package com.cts.wealthmanagementsystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cts.wealthmanagementsystem.entity.Client;
import com.cts.wealthmanagementsystem.repository.ClientRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final ClientRepository userRepository;

    private static final String ADMIN_EMAIL = "admin@wealthy.com";
    private static final String ADMIN_PASSWORD_RAW = "Admin@123"; 

    @Autowired
    public CustomUserDetailsService(PasswordEncoder passwordEncoder, ClientRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (ADMIN_EMAIL.equalsIgnoreCase(email)) {
            return createAdminUser();
        }

        Client user = userRepository.findByEmailAddress(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        if (!user.isApproved()) {
            throw new UsernameNotFoundException("User not approved by admin.");
        }

        return new CustomUserDetails(user);
    }

    private UserDetails createAdminUser() {
        Client admin = new Client();
        admin.setFullName("Admin Wealthy");
        admin.setEmailAddress(ADMIN_EMAIL);
        admin.setUserName("Admin");
        admin.setPassWord(passwordEncoder.encode(ADMIN_PASSWORD_RAW));
        admin.setApproved(true);

        System.out.println("Loading hardcoded admin user: " + ADMIN_EMAIL);
        System.out.println("Admin encoded password: " + admin.getPassWord());
        return new CustomUserDetails(admin);
    }
}