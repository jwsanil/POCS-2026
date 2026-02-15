package org.example.dc.wrapper;

import org.example.dc.record.EmployeeRecord;
import org.example.dc.record.CustomerRecord;

// Sealed base class: only BusinessContract and BusinessRelationship can extend it
public sealed class RecordBundle
        permits BusinessContract, BusinessRelationship {

    private final EmployeeRecord employee;
    private final CustomerRecord customer;

    public RecordBundle(EmployeeRecord employee, CustomerRecord customer) {
        this.employee = employee;
        this.customer = customer;
    }

    public EmployeeRecord employee() { return employee; }
    public CustomerRecord customer() { return customer; }

    // Extra info: convenience method
    // Extra info: convenience method with safe null handling
    public String summary() {
        String employeeName = employee != null
                ? employee.firstName() + " " + employee.lastName()
                : "Unassigned";

        String customerName = customer != null
                ? customer.firstName() + " " + customer.lastName()
                : "Unknown Customer";

        return "Employee: " + employeeName + " | Customer: " + customerName;
    }
}
