package org.example.dc.wrapper;

import org.example.dc.record.EmployeeRecord;
import org.example.dc.record.CustomerRecord;

// Final subclass: represents a business contract, cannot be extended further
public final class BusinessContract extends RecordBundle {
    private final String contractId;

    public BusinessContract(EmployeeRecord employee, CustomerRecord customer, String contractId) {
        super(employee, customer);
        this.contractId = contractId;
    }

    public String contractId() { return contractId; }

    @Override
    public String summary() {
        return super.summary() + " | Contract ID: " + contractId;
    }
}
