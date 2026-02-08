package org.example.dc.wrapper;

import org.example.dc.record.EmployeeRecord;
import org.example.dc.record.CustomerRecord;

// Non-sealed subclass: represents a business relationship, can be extended further
public non-sealed class BusinessRelationship extends RecordBundle {
    private final String relationshipType;

    public BusinessRelationship(EmployeeRecord employee, CustomerRecord customer, String relationshipType) {
        super(employee, customer);
        this.relationshipType = relationshipType;
    }

    public String relationshipType() { return relationshipType; }

    @Override
    public String summary() {
        return super.summary() + " | Relationship Type: " + relationshipType;
    }
}
