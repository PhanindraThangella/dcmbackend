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

@Entity
@Table(
    name = "day_purse"
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
public class DayPurse {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    @Column(
        name = "amount_added",
        nullable = false
    )
    private Long amountAdded;
    
    @Column(
        name = "current_purse",
        nullable = false
    )
    private Long currentPurse;
    
    @CreationTimestamp
    @Column(
        name = "created_at",
        nullable = false,
        updatable = false
    )
    private LocalDateTime createdAt;
}
