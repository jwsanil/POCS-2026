package org.example.jdbc;

import org.example.dc.record.CustomerRecord;
import org.example.dc.record.EmployeeRecord;
import org.example.dc.wrapper.BusinessContract;
import org.example.dc.wrapper.BusinessRelationship;
import org.example.dc.wrapper.RecordBundle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcExample2 {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/sample";
        String user = "postgres";
        String password = "admin";

        List<RecordBundle> bundles = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to PostgreSQL!");

            // Query employees joined with customers
            String sql = "SELECT e.employeeid, e.firstname AS e_first, e.lastname AS e_last, " +
                    "e.position, e.hiredate, e.salary, " +
                    "c.customerid, c.firstname AS c_first, c.lastname AS c_last, " +
                    "c.email, c.phone, c.employee_id AS c_employee_id " +
                    "FROM employee e " +
                    "JOIN customer c ON e.employeeid = c.employee_id";

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
                            rs.getInt("c_employee_id")
                    );

                    // Business logic: decide which bundle to create
                    if (employee.salary() > 100000) {
                        bundles.add(new BusinessContract(employee, customer,
                                "CONTRACT-" + employee.employeeId()));
                    } else {
                        bundles.add(new BusinessRelationship(employee, customer, "Standard"));
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Print results using summary()
        bundles.forEach(bundle -> System.out.println(bundle.summary()));
    }
}
