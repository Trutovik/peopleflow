package com.itechart.peopleflow.service;

import com.itechart.peopleflow.dto.EmployeeDto;

public interface EmployeeService {

  String createOrUpdateEmployee(EmployeeDto employeeDto);
  //EmployeeDto updateEmployeeStatus(Long employeeId, EmployeeEvent event);
}
