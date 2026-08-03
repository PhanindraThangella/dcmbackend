package com.dcm.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dcm.backend.entity.EmployeeDetails;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeDetails, Long> {

    Optional<EmployeeDetails> findByEmployeeId(String employeeId);

    Optional<EmployeeDetails> findByEmployeePhoneNumber(Long employeePhoneNumber);

    Optional<EmployeeDetails> findByEmployeeEmail(String employeeEmail);

    boolean existsByEmployeePhoneNumber(Long employeePhoneNumber);

    boolean existsByEmployeeEmail(String employeeEmail);

    boolean existsByEmployeeId(String employeeId);

    Optional<EmployeeDetails> findTopByOrderByIdDesc();
    
    @Query("SELECT e.employeeName FROM EmployeeDetails e")
    List<String> findAllEmployeeNames();
}