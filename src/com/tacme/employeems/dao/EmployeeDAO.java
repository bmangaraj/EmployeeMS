package com.tacme.employeems.dao;

import java.util.List;

import com.tacme.employeems.model.Employee;

public interface EmployeeDAO {

	public void addEmployee(Employee emp);
	public void updateEmployee(Employee emp);
	public List<Employee> listEmployees();
	public Employee getEmployeeById(int id);
	public void removeEmployee(int id);
}