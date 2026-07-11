package com.dcm.backend.entity;

import com.dcm.backend.enums.Role;
import com.dcm.backend.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing the authentication credentials of an employee.
 *
 * <p>
 * Stores login information and account status. Each employee has exactly one
 * credentials record.
 * </p>
 */
@Entity
@Table(
    name = "credentials",
    indexes = {
        @Index(name = "idx_username", columnList = "username")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Credentials {

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Username used for authentication.
     * (In this project it will be the employee phone number.)
     */
    @Column(
        name = "username",
        nullable = false,
        unique = true
    )
    private String username;

    /**
     * BCrypt hashed password.
     */
    @Column(
        name = "password",
        nullable = false,
        length = 255
    )
    private String password;

    /**
     * Employee role.
     */
    @Enumerated(EnumType.STRING)
    @Column(
        name = "role",
        nullable = false,
        length = 20
    )
    private Role role;

    /**
     * Current account status.
     */
    @Enumerated(EnumType.STRING)
    @Column(
        name = "status",
        nullable = false,
        length = 20
    )
    private Status status;

    /**
     * Associated employee.
     */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
        name = "employee_id",
        nullable = false,
        unique = true
    )
    private EmployeeDetails employee;
}