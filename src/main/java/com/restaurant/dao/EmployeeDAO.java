package com.restaurant.dao;

import com.restaurant.models.Employee;
import java.sql.SQLException;
import java.util.List;

public interface EmployeeDAO {
    List<Employee> getAllEmployees() throws SQLException;
    void addEmployee(Employee employee) throws SQLException; // Add method
    void updateEmployee(Employee employee) throws SQLException; // Update method
    void deleteEmployee(int employeeId) throws SQLException; // Delete method
}