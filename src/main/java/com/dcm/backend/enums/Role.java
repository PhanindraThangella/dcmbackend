package com.dcm.backend.enums;

/**
 * Represents the role assigned to an employee.
 *
 * <p>
 * Roles are used by Spring Security to authorize access to
 * protected resources.
 * </p>
 */
public enum Role {

    /**
     * System administrator with full access.
     */
    ADMIN,

    /**
     * Regular employee with limited access.
     */
    EMPLOYEE
}