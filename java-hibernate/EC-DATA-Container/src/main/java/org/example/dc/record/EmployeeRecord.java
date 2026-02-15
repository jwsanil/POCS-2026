package org.example.dc.record;

import java.time.LocalDate;

/**
 * EmployeeRecord represents an employee entity.
 * Matches the 'employee' table schema from your database.
 */
public record EmployeeRecord(
        Integer employeeId,
        String firstName,
        String lastName,
        String position,
        LocalDate hireDate,
        double salary
) {
    public String fullName() {
        return firstName + " " + lastName;
    }
}
