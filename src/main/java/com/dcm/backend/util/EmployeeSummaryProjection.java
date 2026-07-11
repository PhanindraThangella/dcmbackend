package com.dcm.backend.util;

public interface EmployeeSummaryProjection {
    String getEmployeeId();
    Double getTotalAmount();
    Long getTransactionCount();
}