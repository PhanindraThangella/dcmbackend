package com.dcm.backend.enums;

/**
 * Represents the current status of an employee's account.
 *
 * <p>
 * This status determines whether the employee is allowed
 * to authenticate and access secured resources.
 * </p>
 */
public enum Status {

    /**
     * Account is active and authentication is allowed.
     */
    ACTIVE,

    /**
     * Account is disabled and authentication is denied.
     */
    INACTIVE
}