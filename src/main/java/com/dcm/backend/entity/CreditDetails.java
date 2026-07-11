package com.dcm.backend.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    name = "credit_details"
//    indexes = {
//        @Index(name = "idx_employee_id", columnList = "employee_id"),
//        @Index(name = "idx_employee_phone", columnList = "employee_phone_number"),
//        @Index(name = "idx_employee_email", columnList = "employee_email")
//    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditDetails {

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "customer_name",
        nullable = false,
        length=100
    )
    private String customerName;

    @Column(
        name = "contact_number",
        nullable = false
    )
    private Long contactNumber;

    @Column(
        name = "total_amount",
        nullable = false
    )
    private Long totalAmount;
    
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
}