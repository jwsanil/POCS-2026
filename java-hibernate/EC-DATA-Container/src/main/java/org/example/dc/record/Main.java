package org.example.dc.record;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // List of employees
        List<EmployeeRecord> employees = List.of(
                new EmployeeRecord(1, "Alice", "Johnson", "Sales Manager", LocalDate.of(2020, 1, 15), 60000),
                new EmployeeRecord(2, "David", "Lee", "Account Executive", LocalDate.of(2021, 6, 1), 45000)
        );

        // List of customers
        List<CustomerRecord> customers = List.of(
                new CustomerRecord(1, "Bob", "Smith", "bob.smith@email.com", "123-456-7890", 1),
                new CustomerRecord(2, "Carol", "Brown", "carol.brown@email.com", "987-654-3210", 2)
        );

        // Print them
        employees.forEach(System.out::println);
        customers.forEach(System.out::println);
    }
}
