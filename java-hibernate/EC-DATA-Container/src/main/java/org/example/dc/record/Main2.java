package org.example.dc.record;



import java.time.LocalDate;
import java.util.List;

public class Main2 {
    public static void main(String[] args) {
        // List of employees
        List<EmployeeRecord> employees = List.of(
                new EmployeeRecord(1, "Alice", "Johnson", "Sales Manager",
                        LocalDate.of(2020, 1, 15), 60000),
                new EmployeeRecord(2, "David", "Lee", "Account Executive",
                        LocalDate.of(2021, 6, 1), 45000)
        );

        // List of customers
        List<CustomerRecord> customers = List.of(
                new CustomerRecord(1, "Bob", "Smith", "bob.smith@email.com",
                        "123-456-7890", 1),
                new CustomerRecord(2, "Carol", "Brown", "carol.brown@email.com",
                        "987-654-3210", 2)
        );

        // Print them
        employees.forEach(System.out::println);
        customers.forEach(System.out::println);

        // "Updating" a record means creating a new one
        EmployeeRecord updatedAlice = new EmployeeRecord(
                employees.get(0).employeeId(),
                employees.get(0).firstName(),
                employees.get(0).lastName(),
                employees.get(0).position(),
                employees.get(0).hireDate(),
                65000 // new salary
        );

        System.out.println("Updated Alice: " + updatedAlice);
    }
}

