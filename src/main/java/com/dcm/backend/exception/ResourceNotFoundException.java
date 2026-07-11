package com.dcm.backend.exception;

/**
 * Thrown when a requested resource cannot be found.
 */
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}