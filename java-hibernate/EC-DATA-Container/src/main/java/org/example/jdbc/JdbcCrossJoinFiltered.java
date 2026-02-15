package org.example.jdbc;

import org.example.dc.record.CustomerRecord;
import org.example.dc.record.EmployeeRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcCrossJoinFiltered {

    public static void main(String[] args) {

        String url = "jdbc:postgresql://localhost:5432/sample"; // your DB URL
        String user = "postgres";                               // your DB username
        String password = "admin";                              // your DB password

        List<String> results = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to PostgreSQL!");

            // SQL: cross join filtered by positions
            String sql = """
                    SELECT e.employeeid, e.firstname AS e_first, e.lastname AS e_last,
                           e.position, e.hiredate, e.salary,
                           c.customerid, c.firstname AS c_first, c.lastname AS c_last,
                           c.email, c.phone, c.employee_id AS c_employee_id
                    FROM employee e
                    CROSS JOIN customer c
                    WHERE e.position IN ('Sales Manager', 'Customer Success Manager')
                    """;

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    EmployeeRecord employee = new EmployeeRecord(
                            rs.getInt("employeeid"),
                            rs.getString("e_first"),
                            rs.getString("e_last"),
                            rs.getString("position"),
                            rs.getDate("hiredate").toLocalDate(),
                            rs.getDouble("salary")
                    );

                    CustomerRecord customer = new CustomerRecord(
                            rs.getInt("customerid"),
                            rs.getString("c_first"),
                            rs.getString("c_last"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getObject("c_employee_id") != null ? rs.getInt("c_employee_id") : null
                    );

                    results.add("Employee: " + employee.fullName() +
                            " | Customer: " + customer.firstName() + " " + customer.lastName() +
                            " | Position: " + employee.position());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Print filtered results
        results.forEach(System.out::println);
        System.out.println("Total filtered combinations: " + results.size());
    }
}
