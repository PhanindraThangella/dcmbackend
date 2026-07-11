package com.dcm.backend.entity;

import java.time.LocalDate;
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

@Entity
@Table(
    name = "vendor_details"
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
public class VendorLotsDetails {
	/**
     * Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "vendor_name",
        nullable = false,
        length = 50
    )
    private String vendorName;

    @Column(
        name = "product_name",
        nullable = false,
        length = 50
    )
    private String productName;

    @Column(
        name = "metal_type",
        nullable = false,
        length = 50
    )
    private String metalType;

    @Column(
        name = "time_for_payment",
        nullable = false
    )
    private LocalDate timeForPayment;

    @Column(
        name = "peices",
        nullable = false
	)
    private Long peices;
    
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
        name = "fine_weight",
        nullable = false
    )
    private Double fineWeight;

    @Column(
        name = "stone_amount",
        nullable=false
    )
    private Long stoneAmount;
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
