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
    name = "payment_details"
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
public class PaymentDetails {

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @Column(
        name = "tag_number",
        nullable = false
    )
    private Long tagNumber=0L;
    @Builder.Default
    @Column(
        name = "total_cash",
        nullable = false
    )
    private Long totalCash=0L;
    @Builder.Default
    @Column(
        name = "total_upi_amount",
        nullable = false
    )
    private Long totalUpiAmount=0L;
    @Builder.Default
    @Column(
        name = "old_gold_grams",
        nullable = false
    )
    private Double oldGoldGrams=0.0;
    @Builder.Default
    @Column(
        name = "old_gold_amount",
        nullable = false
    )
    private Long oldGoldAmount=0L;
    @Builder.Default
    @Column(
        name = "old_silver_grams",
        nullable = false
    )
    private Double oldSilverGrams=0.0;
    @Builder.Default
    @Column(
        name = "old_silver_amount",
        nullable = false
    )
    private Long oldSilverAmount=0L;
    @Builder.Default
    @Column(
        name = "pure_gold_grams",
        nullable = false
    )
    private Double pureGoldGrams=0.0;
    @Builder.Default
    @Column(
        name = "pure_gold_amount",
        nullable = false
    )
    private Long pureGoldAmount=0L;
    @Builder.Default
    @Column(
        name = "pure_silver_grams",
        nullable = false
    )
    private Double pureSilverGrams=0.0;
    @Builder.Default
    @Column(
        name = "pure_silver_amount",
        nullable = false
    )
    private Long pureSilverAmount=0L;
    @Builder.Default
    @Column(
        name = "employee_name"
    )
    private String employeeName="no";
    @Builder.Default
    @Column(
        name = "credit_id"
    )
    private Long creditId=0L;
    @Builder.Default
    @Column(
        name = "order_id"
    )
    private Long orderId=0L;
    
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