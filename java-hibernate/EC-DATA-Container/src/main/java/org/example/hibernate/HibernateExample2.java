package org.example.hibernate;

import org.example.dc.domain.entity.Customer;
import org.example.dc.domain.entity.Employee;
import org.example.dc.record.CustomerRecord;
import org.example.dc.record.EmployeeRecord;
import org.example.dc.wrapper.BusinessContract;
import org.example.dc.wrapper.BusinessRelationship;
import org.example.dc.wrapper.RecordBundle;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HibernateExample2 {

    private static SessionFactory factory;

    public static void main(String[] args) {
        factory = new Configuration()
                .configure("hibernate-entity.cfg.xml")
                .addAnnotatedClass(Employee.class)
                .addAnnotatedClass(Customer.class)
                .buildSessionFactory();

        try {
            insertData();   // Run once to insert sample data
            selectData();   // Run anytime to query and print data
        } finally {
            factory.close();
        }
    }

    private static void insertData() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            // Create employees
            Employee e1 = createEmployee("Alice", "Johnson", "Sales Manager", LocalDate.of(2020, 1, 15), 60000);
            Employee e2 = createEmployee("David", "Lee", "Account Executive", LocalDate.of(2021, 6, 1), 45000);
            Employee e3 = createEmployee("Ava", "Hall", "Customer Success Manager", LocalDate.of(2023, 2, 14), 47000);

            session.persist(e1);
            session.persist(e2);
            session.persist(e3);

            // Create customers linked to employees
            Customer c1 = createCustomer("Bob", "Smith", "bob.smith@email.com", "123-456-7890", e1);
            Customer c2 = createCustomer("Carol", "Brown", "carol.brown@email.com", "987-654-3210", e3);
            Customer c3 = createCustomer("Lucas", "Roberts", "lucas.roberts@email.com", "555-333-4444", null); // Unassigned

            session.persist(c1);
            session.persist(c2);
            session.persist(c3);

            session.getTransaction().commit();
            System.out.println("Data inserted successfully!");
        }
    }

    private static Employee createEmployee(String firstName, String lastName, String position, LocalDate hireDate, double salary) {
        Employee e = new Employee();
        e.setFirstName(firstName);
        e.setLastName(lastName);
        e.setPosition(position);
        e.setHireDate(hireDate);
        e.setSalary(salary);
        return e;
    }

    private static Customer createCustomer(String firstName, String lastName, String email, String phone, Employee employee) {
        Customer c = new Customer();
        c.setFirstName(firstName);
        c.setLastName(lastName);
        c.setEmail(email);
        c.setPhone(phone);
        c.setEmployee(employee); // Can be null
        return c;
    }

    private static void selectData() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            // Fetch all employees and customers
            List<Employee> employees = session.createQuery("from Employee", Employee.class).getResultList();
            List<Customer> customers = session.createQuery("from Customer", Customer.class).getResultList();

            // Build bundles using Java 8 streams and Optional
            List<RecordBundle> bundles = customers.stream()
                    .map(c -> {
                        Optional<Employee> empOpt = Optional.ofNullable(c.getEmployee());

                        EmployeeRecord employeeRecord = empOpt
                                .map(e -> new EmployeeRecord(
                                        e.getEmployeeId(),
                                        e.getFirstName(),
                                        e.getLastName(),
                                        e.getPosition(),
                                        e.getHireDate(),
                                        e.getSalary()))
                                .orElse(null);

                        CustomerRecord customerRecord = new CustomerRecord(
                                c.getCustomerId(),
                                c.getFirstName(),
                                c.getLastName(),
                                c.getEmail(),
                                c.getPhone(),
                                empOpt.map(Employee::getEmployeeId).orElse(null)
                        );

                        // Business logic
                        if (employeeRecord != null && employeeRecord.salary() > 100000) {
                            return new BusinessContract(employeeRecord, customerRecord,
                                    "CONTRACT-" + employeeRecord.employeeId());
                        } else {
                            return new BusinessRelationship(employeeRecord, customerRecord, "Standard");
                        }
                    })
                    .collect(Collectors.toList());

            // Print results
            System.out.println("=== Bundles (Business DTOs) ===");
            bundles.forEach(bundle -> System.out.println(bundle.summary()));

            session.getTransaction().commit();
        }
    }
}
