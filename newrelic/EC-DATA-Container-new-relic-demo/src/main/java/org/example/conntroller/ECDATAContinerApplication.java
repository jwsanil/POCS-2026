package org.example.conntroller;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import org.example.dc.record.CustomerRecord;
import org.example.dc.record.EmployeeRecord;
import org.example.dc.wrapper.BusinessContract;
import org.example.dc.wrapper.BusinessRelationship;
import org.example.dc.wrapper.RecordBundle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@SpringBootApplication
public class ECDATAContinerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ECDATAContinerApplication.class, args);
    }
}
@RestController
 class ECDATAContainerController {
        @Trace(dispatcher = true)
        @GetMapping("/employees")
        public List<String> getEmployees() {

            String url = "jdbc:postgresql://localhost:5432/sample";
            String user = "postgres";
            String password = "admin";

            NewRelic.setTransactionName("Custom", "EmployeeCustomerQuery");

            List<RecordBundle> bundles = new ArrayList<>();
            List<String> result = new ArrayList<>();

            try (Connection conn = DriverManager.getConnection(url, user, password)) {

                System.out.println("Connected to PostgreSQL!");

                String sql =
                        "SELECT e.employeeid, e.firstname AS e_first, e.lastname AS e_last, " +
                                "e.position, e.hiredate, e.salary, " +
                                "c.customerid, c.firstname AS c_first, c.lastname AS c_last, " +
                                "c.email, c.phone, c.employee_id AS c_employee_id " +
                                "FROM employee e JOIN customer c ON e.employeeid = c.employee_id";

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

            bundles.forEach(b -> result.add(b.summary()));

            return result;
        }


    }
