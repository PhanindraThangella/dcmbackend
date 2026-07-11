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
    name = "items_details"
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
public class JewelleryItems {

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "tag_number",
        nullable = false,
        unique = true
    )
    private Long tagNumber;

    @Column(
        name = "main_product",
        nullable = false,
        length = 20
    )
    private String mainProduct;

    @Column(
        name = "product_name",
        nullable = false,
        length = 50
    )
    private String productName;

    @Column(
        name = "peices",
        nullable = false
    )
    private Long noOfPeices;

    @Column(
        name = "gross_weight",
        nullable = false
    )
    private Double grossWeight;

    @Column(
        name = "net_weight",
        nullable = false
    )
    private Double netWeight;

    @Column(
        name = "lot_no",
        unique = true,
        nullable=false
    )
    private Long lotNo;
    @Builder.Default
    @Column(
            name = "stone_weight",
            nullable=false
        )
        private Double stoneWeight=0.0;
    @Builder.Default
    @Column(
            name = "stone_amount",
            nullable=false
        )
        private Long stoneAmount=0L;

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