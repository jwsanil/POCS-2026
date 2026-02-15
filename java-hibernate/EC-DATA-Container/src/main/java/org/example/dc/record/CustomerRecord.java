package org.example.dc.record;

// Customer record
public record CustomerRecord(Integer customerId, String firstName, String lastName,
                             String email, String phone,
                             Integer employeeId )// foreign key reference
 { }
