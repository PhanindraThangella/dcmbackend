package com.dcm.backend.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dcm.backend.entity.Credentials;
import com.dcm.backend.repository.CredentialsRepository;

import lombok.RequiredArgsConstructor;

/**
 * Custom implementation of {@link UserDetailsService}.
 *
 * <p>
 * Loads user credentials from the database during authentication.
 * This service is automatically used by Spring Security's
 * AuthenticationManager.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    private final CredentialsRepository credentialsRepository;

    /**
     * Loads a user by username.
     *
     * @param username Username used for authentication.
     * @return Authenticated user details.
     * @throws UsernameNotFoundException if user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Credentials credentials = credentialsRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Invalid username or password."
                        ));

        return new CustomUserDetails(credentials);
    }

}