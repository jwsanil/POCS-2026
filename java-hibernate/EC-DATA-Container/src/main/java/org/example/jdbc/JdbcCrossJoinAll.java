package org.example.jdbc;

import org.example.dc.record.CustomerRecord;
import org.example.dc.record.EmployeeRecord;
import org.example.dc.wrapper.BusinessRelationship;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcCrossJoinAll {

    public static void main(String[] args) {

        String url = "jdbc:postgresql://localhost:5432/sample";
        String user = "postgres";
        String password = "admin";

        List<BusinessRelationship> simulations = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to PostgreSQL!");

            // CROSS JOIN query: every employee paired with every customer
            String sql = "SELECT e.employeeid, e.firstname AS e_first, e.lastname AS e_last, " +
                    "e.position, e.hiredate, e.salary, " +
                    "c.customerid, c.firstname AS c_first, c.lastname AS c_last, " +
                    "c.email, c.phone, c.employee_id AS c_employee_id " +
                    "FROM employee e " +
                    "CROSS JOIN customer c";

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    // Employee record
                    EmployeeRecord employee = new EmployeeRecord(
                            rs.getInt("employeeid"),
                            rs.getString("e_first"),
                            rs.getString("e_last"),
                            rs.getString("position"),
                            rs.getDate("hiredate").toLocalDate(),
                            rs.getDouble("salary")
                    );

                    // Customer record (nullable employee_id handled)
                    Integer empId = (Integer) rs.getObject("c_employee_id");
                    CustomerRecord customer = new CustomerRecord(
                            rs.getInt("customerid"),
                            rs.getString("c_first"),
                            rs.getString("c_last"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            empId
                    );

                    // Simulation: every employee can potentially manage every customer
                    BusinessRelationship simulation = new BusinessRelationship(employee, customer, "SIMULATION");
                    simulations.add(simulation);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Print all cross join combinations
        System.out.println("===== Cross Join Simulation: Every employee with every customer =====");
        simulations.forEach(sim -> System.out.println(sim.summary()));
        System.out.println("Total combinations: " + simulations.size());
    }
}
