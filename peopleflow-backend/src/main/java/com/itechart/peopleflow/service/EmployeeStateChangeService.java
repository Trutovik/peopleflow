package com.itechart.peopleflow.service;

import com.itechart.peopleflow.dto.EmployeeDto;
import com.itechart.peopleflow.service.state.EmployeeEvent;

public interface EmployeeStateChangeService {

  String EMPLOYEE_ID_HEADER = "employee_id";

  EmployeeDto updateEmployeeStatus(String userId, EmployeeEvent event);
}
