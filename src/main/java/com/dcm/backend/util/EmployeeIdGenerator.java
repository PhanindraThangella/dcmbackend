package com.dcm.backend.util;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.dcm.backend.entity.EmployeeDetails;
import com.dcm.backend.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

/**
 * Generates business employee IDs.
 *
 * Example:
 * JDJ_EMP_000001
 */
@Component
@RequiredArgsConstructor
public class EmployeeIdGenerator {

    private static final String PREFIX = "JDJ_EMP_";

    private final EmployeeRepository employeeRepository;

    public String generateEmployeeId() {

        Optional<EmployeeDetails> employee =
                employeeRepository.findTopByOrderByIdDesc();

        if (employee.isEmpty()) {
            return PREFIX + "000001";
        }

        String employeeId = employee.get().getEmployeeId();

        int nextNumber =
                Integer.parseInt(employeeId.substring(PREFIX.length())) + 1;

        return PREFIX + String.format("%06d", nextNumber);
    }

}