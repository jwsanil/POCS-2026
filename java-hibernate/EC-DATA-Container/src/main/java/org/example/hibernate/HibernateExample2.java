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
import java.util.ArrayList;
import java.util.List;

public class HibernateExample2 {

    private static SessionFactory factory;

    public static void main(String[] args) {
        factory = new Configuration()
                .configure("hibernate-entity.cfg.xml")
                .addAnnotatedClass(Employee.class)
                .addAnnotatedClass(Customer.class)
                .buildSessionFactory();

        try {
            // Uncomment one of these depending on what you want to do:
            // insertData();   // Run once to insert sample data
            selectData();      // Run anytime to query and print data
        } finally {
            factory.close();
        }
    }

    private static void insertData() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            // Create employees
            Employee e1 = new Employee();
            e1.setFirstName("Alice");
            e1.setLastName("Johnson");
            e1.setPosition("Sales Manager");
            e1.setHireDate(LocalDate.of(2020, 1, 15));
            e1.setSalary(60000);

            Employee e2 = new Employee();
            e2.setFirstName("David");
            e2.setLastName("Lee");
            e2.setPosition("Account Executive");
            e2.setHireDate(LocalDate.of(2021, 6, 1));
            e2.setSalary(45000);

            // Save employees
            session.persist(e1);
            session.persist(e2);

            // Create customers linked to employees
            Customer c1 = new Customer();
            c1.setFirstName("Bob");
            c1.setLastName("Smith");
            c1.setEmail("bob.smith@email.com");
            c1.setPhone("123-456-7890");
            c1.setEmployee(e1);

            Customer c2 = new Customer();
            c2.setFirstName("Carol");
            c2.setLastName("Brown");
            c2.setEmail("carol.brown@email.com");
            c2.setPhone("987-654-3210");
            c2.setEmployee(e2);

            // Save customers
            session.persist(c1);
            session.persist(c2);

            session.getTransaction().commit();
            System.out.println("Data inserted successfully!");
        }
    }

    private static void selectData() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            // Fetch all employees and customers
            List<Employee> employees = session.createQuery("from Employee", Employee.class).getResultList();
            List<Customer> customers = session.createQuery("from Customer", Customer.class).getResultList();

            // Build bundles
            List<RecordBundle> bundles = new ArrayList<>();

            for (Customer c : customers) {
                Employee e = c.getEmployee();

                EmployeeRecord employeeRecord = new EmployeeRecord(
                        e.getEmployeeId(),
                        e.getFirstName(),
                        e.getLastName(),
                        e.getPosition(),
                        e.getHireDate(),
                        e.getSalary()
                );

                CustomerRecord customerRecord = new CustomerRecord(
                        c.getCustomerId(),
                        c.getFirstName(),
                        c.getLastName(),
                        c.getEmail(),
                        c.getPhone(),
                        e.getEmployeeId()
                );

                // Business logic: decide bundle type
                if (employeeRecord.salary() > 100000) {
                    bundles.add(new BusinessContract(employeeRecord, customerRecord,
                            "CONTRACT-" + employeeRecord.employeeId()));
                } else {
                    bundles.add(new BusinessRelationship(employeeRecord, customerRecord, "Standard"));
                }
            }

            // Print results
            System.out.println("=== Bundles (Business DTOs) ===");
            bundles.forEach(bundle -> System.out.println(bundle.summary()));

            session.getTransaction().commit();
        }
    }
}
