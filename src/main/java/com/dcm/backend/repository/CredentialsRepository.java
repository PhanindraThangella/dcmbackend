package com.dcm.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dcm.backend.entity.Credentials;

/**
 * Repository for performing CRUD operations on Credentials.
 */
@Repository
public interface CredentialsRepository extends JpaRepository<Credentials, Long> {

    /**
     * Finds credentials by username.
     *
     * @param username Username used for authentication.
     * @return Optional containing credentials if found.
     */
    Optional<Credentials> findByUsername(String username);

    /**
     * Checks whether the username already exists.
     *
     * @param username Username.
     * @return true if username exists.
     */
    boolean existsByUsername(String username);
}