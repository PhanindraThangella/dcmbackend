package com.dcm.backend.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.dcm.backend.enums.ItemType;
import com.dcm.backend.enums.TransactionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    name = "transaction_details"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transactions {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Column(
	        name = "employee_id",
	        nullable = false,
	        length = 20
	    )
	private String employeeId;
	@Column(
	        name = "total_grams",
	        nullable = false
	    )
	private Double totalGrams;
	@Column(
	        name = "rate_per_gram",
	        nullable = false
	    )
	private Long ratePerGram;
	@Column(
	        name = "making_charges",
	        nullable = false
	    )
	private Long makingCharges;
	@Column(
	        name = "wastage",
	        nullable = false
	    )
	private Double wastage;
	@Column(
	        name = "tag_number",
	        nullable = false
	    )
	private Long tagNumber;
	@Column(
	        name = "total_amount",
	        nullable = false
	    )
	private Double totalAmount;
	@Column(
	        name = "profit",
	        nullable = false
	    )
	private Double profit;
	@Enumerated(EnumType.STRING)
	@Column(
	        name = "item_type",
	        nullable = false,
	        length = 20
	    )
	private ItemType itemType;
	@Enumerated(EnumType.STRING)
	@Column(
	        name = "transaction_status",
	        nullable = false,
	        length = 20
	    )
	private TransactionStatus transactionStatus;
	@Column(
	        name = "stone_amount",
	        nullable = false
	    )
	private Long stoneAmount;
	@CreationTimestamp
    @Column(
        name = "created_at",
        nullable = false,
        updatable = false
    )
    private LocalDateTime createdAt;
}
