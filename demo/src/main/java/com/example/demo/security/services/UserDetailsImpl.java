package com.example.demo.security.services;

import com.example.demo.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * This class is a custom implementation of Spring Security's UserDetails interface.
 * It wraps our User entity and provides the necessary information for authentication
 * and authorization.
 */
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String username, String email, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * A static factory method to create a UserDetailsImpl instance from a User entity.
     * @param user The User entity from the database.
     * @return A UserDetailsImpl object.
     */
    public static UserDetailsImpl build(User user) {
        // We convert our single Role enum into a list of GrantedAuthority objects.
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities);
    }

    // --- GETTERS ---
    // This is the method that was missing, causing errors in your ProfileController.
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    // --- Overridden methods from UserDetails interface ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // You can add logic here for account expiration
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // You can add logic here for account locking
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // You can add logic here for password expiration
    }

    @Override
    public boolean isEnabled() {
        return true; // You can add logic here for disabling users
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}