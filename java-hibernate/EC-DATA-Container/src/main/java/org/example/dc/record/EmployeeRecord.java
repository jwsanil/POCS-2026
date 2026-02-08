package org.example.dc.record;
//
//Employee record

public record EmployeeRecord
        (int employeeId, String firstName, String lastName,
         String position, java.time.LocalDate hireDate, double salary)
{
}

