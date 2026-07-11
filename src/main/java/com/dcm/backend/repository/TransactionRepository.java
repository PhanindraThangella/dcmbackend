package com.dcm.backend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.dcm.backend.entity.Transactions;
import com.dcm.backend.util.EmployeeSummaryProjection;

public interface TransactionRepository extends JpaRepository<Transactions, Long>{
	Transactions findFirstByOrderByIdDesc();
	Transactions findByTagNumber(Long tagNumber);
	@Transactional
	int deleteByTagNumber(Long tagNumber);
	List<Transactions> findByEmployeeIdAndCreatedAtBetween(String employeeId,LocalDateTime start,LocalDateTime end);
	@Query(value = "SELECT EXISTS(SELECT 1 FROM transaction_details "
			+"WHERE created_at >= :startOfDay "
			+"AND created_at <= :endOfDay LIMIT 1)"
			, nativeQuery = true)
	boolean existsByDay(
			@Param("startOfDay") LocalDateTime startOfDay, 
			@Param("endOfDay") LocalDateTime endOfDay
			);
	@Query(value = "SELECT * FROM transaction_details "
			+"WHERE transaction_status = 'PENDING' ORDER BY created_at DESC"
			, nativeQuery = true)
	List<Transactions> fetchPendingTransaction();
	@Query(value = "SELECT SUM(cast(total_amount as double precision)) FROM transaction_details "
			+ "WHERE created_at >= :startOfDay"
			+" AND created_at <= :endOfDay"
			, nativeQuery = true)
    Double getTotalSales(
    		@Param("startOfDay") LocalDateTime startOfDay,
    		@Param("endOfDay") LocalDateTime endOfDay
    		);
	@Query(value = "SELECT SUM(cast(profit as double precision)) FROM transaction_details "
			+ "WHERE created_at >= :startOfDay"
			+" AND created_at <= :endOfDay"
			, nativeQuery = true)
    Double getTotalProfits(
    		@Param("startOfDay") LocalDateTime startOfDay,
    		@Param("endOfDay") LocalDateTime endOfDay
    		);
	@Query(value = "SELECT SUM(cast(total_grams as double precision)) FROM transaction_details "
			+ "WHERE created_at >= :startOfDay"
			+" AND created_at <= :endOfDay"
			+" AND item_type = :itemType"
			, nativeQuery = true)
    Double getTotalMetalSold(
    		@Param("startOfDay") LocalDateTime startOfDay,
    		@Param("endOfDay") LocalDateTime endOfDay,
    		@Param("itemType") String itemType
    		);
	@Query(value = "SELECT SUM(cast(total_amount as double precision)) FROM transaction_details "
			+ "WHERE created_at >= :startOfDay"
			+" AND created_at <= :endOfDay"
			+" AND item_type = :itemType"
			, nativeQuery = true)
    Double getTotalMetalSales(
    		@Param("startOfDay") LocalDateTime startOfDay,
    		@Param("endOfDay") LocalDateTime endOfDay,
    		@Param("itemType") String itemType
    		);
	@Query(value = "SELECT COUNT(DISTINCT employee_id) FROM transaction_details "
			+ "WHERE created_at >= :startOfDay"
			+" AND created_at <= :endOfDay"
			, nativeQuery = true)
    Double getTotalEmployeesActive(
    		@Param("startOfDay") LocalDateTime startOfDay,
    		@Param("endOfDay") LocalDateTime endOfDay
    		);
	@Query(value = "SELECT COUNT(id) FROM transaction_details "
			+ "WHERE created_at >= :startOfDay"
			+" AND created_at <= :endOfDay"
			, nativeQuery = true)
    Double getTotalOrders(
    		@Param("startOfDay") LocalDateTime startOfDay,
    		@Param("endOfDay") LocalDateTime endOfDay
    		);
	@Query(value = "SELECT * "
            + "FROM transaction_details "
            + "WHERE created_at >= :startOfDay AND created_at <= :endOfDay "
            + "ORDER BY created_at DESC "
            + "LIMIT 5", 
	       nativeQuery = true)
	List<Transactions> getRecentTransactions(
	        @Param("startOfDay") LocalDateTime startOfDay,
	        @Param("endOfDay") LocalDateTime endOfDay
	);
	@Query(value = "SELECT employee_id AS employeeId, SUM(cast(total_amount as double precision)) AS totalAmount, COUNT(id) AS transactionCount "
            + "FROM transaction_details "
            + "WHERE created_at >= :startOfDay AND created_at <= :endOfDay "
            + "GROUP BY employee_id "
            + "ORDER BY totalAmount DESC "
            + "LIMIT 5", 
	       nativeQuery = true)
	List<EmployeeSummaryProjection> getEmployeeTransactionSummary(
	        @Param("startOfDay") LocalDateTime startOfDay,
	        @Param("endOfDay") LocalDateTime endOfDay
	);
	
}
