package org.example.jdbc;
import org.example.dc.record.CustomerRecord;
import org.example.dc.record.EmployeeRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class JdbcExample {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/sample";
        String user = "postgres";
        String password = "admin";

        List<EmployeeRecord> employees = new ArrayList<>();
        List<CustomerRecord> customers = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to PostgreSQL!");

            // Query employees
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM employee")) {
                while (rs.next()) {
                    employees.add(new EmployeeRecord(
                            rs.getInt("employee_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("position"),
                            rs.getDate("hire_date").toLocalDate(),
                            rs.getDouble("salary")
                    ));
                }
            }

            // Query customers
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM customer")) {
                while (rs.next()) {
                    customers.add(new CustomerRecord(
                            rs.getInt("customer_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getInt("employee_id")
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Print results
        employees.forEach(System.out::println);
        customers.forEach(System.out::println);
    }
}
