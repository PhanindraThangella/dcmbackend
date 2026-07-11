package com.dcm.backend.exception;

/**
 * Thrown when attempting to create a resource that already exists.
 */
public class ResourceAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}