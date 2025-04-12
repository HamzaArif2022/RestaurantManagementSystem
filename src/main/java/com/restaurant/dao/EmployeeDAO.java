package com.restaurant.dao;

import com.restaurant.models.Employee;
import java.sql.SQLException;
import java.util.List;

public interface EmployeeDAO {
    List<Employee> getAllEmployees() throws SQLException;
}