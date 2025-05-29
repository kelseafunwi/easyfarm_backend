package com.easyfarm.easyfarm.service;

import com.easyfarm.easyfarm.model.Employee;
import com.easyfarm.easyfarm.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getEmployeesByRole(String role) {
        // could have more logic here later
        return employeeRepository.findByRole(role);
    }

    public void payAllSalaries() {
        List<Employee> employees = employeeRepository.findAll();
        for (Employee e : employees) {
            // Send to payroll system, log transaction, etc.
        }
    }
}
