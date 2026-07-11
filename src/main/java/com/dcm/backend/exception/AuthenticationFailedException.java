package com.dcm.backend.exception;

/**
 * Thrown when user authentication fails.
 */
public class AuthenticationFailedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AuthenticationFailedException(String message) {
        super(message);
    }
}