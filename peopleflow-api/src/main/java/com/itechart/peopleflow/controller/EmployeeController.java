package com.itechart.peopleflow.controller;

import com.itechart.peopleflow.dto.EmployeeDto;
import com.itechart.peopleflow.service.EmployeeService;
import com.itechart.peopleflow.service.state.EmployeeEvent;
import com.itechart.peopleflow.service.state.EmployeeState;
import com.itechart.peopleflow.ui.request.EmployeeRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("employees")
public class EmployeeController {

  private final EmployeeService employeeService;

  public EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @PostMapping
  public String createEmployee(@RequestBody EmployeeRequest employeeRequest) {
    EmployeeDto employeeDto = EmployeeDto.builder().build();
    BeanUtils.copyProperties(employeeRequest, employeeDto);
    String createdEmployee = employeeService.createOrUpdateEmployee(employeeDto);
    return createdEmployee;
  }

  @PostMapping("/{id}/{event}")
  public String updateEmployeeState(@PathVariable("id") String id, @PathVariable("event") String event) {
    EmployeeEvent employeeEvent = EmployeeEvent.valueOf(event.toUpperCase(Locale.ROOT));
    EmployeeDto employee = EmployeeDto.builder()
      .userId(id)
      .event(employeeEvent)
      .build();
    return employeeService.createOrUpdateEmployee(employee);
  }
}

