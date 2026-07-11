package com.dcm.backend.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing an employee in the organization.
 *
 * <p>
 * This entity stores the employee's personal information.
 * Authentication-related information is maintained separately
 * in the {@link Credentials} entity.
 * </p>
 */
@Entity
@Table(
    name = "employee_details",
    indexes = {
        @Index(name = "idx_employee_id", columnList = "employee_id"),
        @Index(name = "idx_employee_phone", columnList = "employee_phone_number"),
        @Index(name = "idx_employee_email", columnList = "employee_email")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDetails {

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Business employee ID.
     * Example: JDJ_EMP_000001
     */
    @Column(
        name = "employee_id",
        nullable = false,
        unique = true,
        length = 20
    )
    private String employeeId;

    /**
     * Full name of the employee.
     */
    @Column(
        name = "employee_name",
        nullable = false,
        length = 100
    )
    private String employeeName;

    /**
     * Preferred name or nickname.
     */
    @Column(
        name = "nickname",
        nullable = false,
        length = 50
    )
    private String nickname;

    /**
     * Employee date of birth.
     */
    @Column(
        name = "employee_dob",
        nullable = false
    )
    private LocalDate employeeDOB;

    /**
     * Mobile number.
     */
    @Column(
        name = "employee_phone_number",
        nullable = false,
        unique = true
    )
    private Long employeePhoneNumber;

    /**
     * Residential address.
     */
    @Column(
        name = "employee_address",
        nullable = false,
        length = 500
    )
    private String employeeAddress;

    /**
     * Email address.
     */
    @Column(
        name = "employee_email",
        unique = true,
        length = 100
    )
    private String employeeEmail;

    /**
     * Timestamp automatically populated when the record is created.
     */
    @CreationTimestamp
    @Column(
        name = "created_at",
        nullable = false,
        updatable = false
    )
    private LocalDateTime createdAt;

    /**
     * Authentication credentials associated with this employee.
     */
    @OneToOne(
        mappedBy = "employee",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY,
        orphanRemoval = true
    )
    private Credentials credentials;
}