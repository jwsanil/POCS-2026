package org.example.hibernate;

import org.example.dc.domain.entity.Customer;
import org.example.dc.domain.entity.Employee;
import org.example.dc.record.CustomerRecord;
import org.example.dc.record.EmployeeRecord;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;
import java.util.List;

public class HibernateExample {

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

            // Fetch all employees as entities
            List<Employee> employees = session.createQuery("from Employee", Employee.class).getResultList();

            // Convert entities to immutable EmployeeRecord DTOs
            List<EmployeeRecord> employeeRecords = employees.stream()
                    .map(e -> new EmployeeRecord(
                            e.getEmployeeId(),
                            e.getFirstName(),
                            e.getLastName(),
                            e.getPosition(),
                            e.getHireDate(),
                            e.getSalary()
                    ))
                    .toList();

            System.out.println("=== Employees (DTOs) ===");
            employeeRecords.forEach(System.out::println);

            // Fetch all customers as entities
            List<Customer> customers = session.createQuery("from Customer", Customer.class).getResultList();

            // Convert entities to immutable CustomerRecord DTOs
            List<CustomerRecord> customerRecords = customers.stream()
                    .map(c -> new CustomerRecord(
                            c.getCustomerId(),
                            c.getFirstName(),
                            c.getLastName(),
                            c.getEmail(),
                            c.getPhone(),
                            c.getEmployee().getEmployeeId()
                    ))
                    .toList();

            System.out.println("=== Customers (DTOs) ===");
            customerRecords.forEach(System.out::println);

            session.getTransaction().commit();
        }
    }

    }

