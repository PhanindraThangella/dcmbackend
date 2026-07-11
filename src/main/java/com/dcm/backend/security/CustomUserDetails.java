package com.dcm.backend.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.dcm.backend.entity.Credentials;
import com.dcm.backend.enums.Status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Custom implementation of {@link UserDetails}.
 *
 * <p>
 * This class represents the authenticated user stored in the
 * Spring Security Context after successful authentication.
 * </p>
 */
@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;

    /**
     * Credentials entity loaded from the database.
     */
    private final Credentials credentials;

    /**
     * Returns the business employee ID.
     */
    public String getEmployeeId() {
        return credentials.getEmployee().getEmployeeId();
    }

    /**
     * Returns the employee name.
     */
    public String getEmployeeName() {
        return credentials.getEmployee().getEmployeeName();
    }

    /**
     * Returns the employee nickname.
     */
    public String getNickname() {
        return credentials.getEmployee().getNickname();
    }

    /**
     * Returns the employee role.
     */
    public String getRole() {
        return credentials.getRole().name();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(
                new SimpleGrantedAuthority(
                        "ROLE_" + credentials.getRole().name()
                )
        );
    }

    @Override
    public String getPassword() {
        return credentials.getPassword();
    }

    @Override
    public String getUsername() {
        return credentials.getUsername();
    }

    /**
     * Indicates whether the account has expired.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the account has been locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the credentials have expired.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the account is enabled.
     */
    @Override
    public boolean isEnabled() {
        return credentials.getStatus() == Status.ACTIVE;
    }

}