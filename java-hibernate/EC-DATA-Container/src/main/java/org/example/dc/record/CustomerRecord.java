package org.example.dc.record;

// Customer record
public record CustomerRecord(int customerId, String firstName, String lastName,
                             String email, String phone,
                             int employeeId )// foreign key reference
 { }
