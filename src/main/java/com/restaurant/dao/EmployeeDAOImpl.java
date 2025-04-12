package com.restaurant.dao;

import com.restaurant.config.DBConfig;
import com.restaurant.models.Employee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAOImpl implements EmployeeDAO {
    @Override
    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees"; // Adjust table name as needed

        try (Connection conn = DBConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmployeeId(rs.getInt("employee_id")); // Adjust field names as needed
                employee.setName(rs.getString("name")); // Adjust field names as needed
                employees.add(employee);
            }
        }
        return employees;
    }
}