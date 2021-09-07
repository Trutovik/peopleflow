package com.itechart.peopleflow.service;

import com.itechart.peopleflow.PeopleflowBackendApplication;
import com.itechart.peopleflow.dto.EmployeeDto;
import com.itechart.peopleflow.entity.EmployeeEntity;
import com.itechart.peopleflow.listener.KafkaConsumer;
import com.itechart.peopleflow.repository.EmployeeRepository;
import com.itechart.peopleflow.service.state.EmployeeState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = PeopleflowBackendApplication.class)
class EmployeeServiceTest {

  @Autowired
  EmployeeStateChangeService employeeStateChangeService;

  @Autowired
  KafkaConsumer employeeService;

  @Autowired
  EmployeeRepository employeeRepository;

  EmployeeDto employee;

  @BeforeEach
  void setUp() {
    employee = EmployeeDto.builder()
        .firstName("Alex")
        .lastName("Sabalevich")
        .age(30)
        .email("alex@sabal.com")
        .userId("6e63a045-94b5-44ac-a3d4-b802e97fd3e3")
        .password("1234567")
        .build();
  }

  @Transactional
  @Test
  void testUpdateEmployeeState() {
    employeeService.createEmployee(employee);
    EmployeeEntity entity = employeeRepository.findByUserId(employee.getUserId());
    assert(entity.getState().equals(EmployeeState.ADDED));
    employeeStateChangeService.updateEmployeeStatus(entity.getUserId(), EmployeeState.APPROVED);
    assert(entity.getState().equals(EmployeeState.ADDED));
    assert(!entity.getState().equals(EmployeeState.APPROVED));
    employeeStateChangeService.updateEmployeeStatus(entity.getUserId(), EmployeeState.IN_CHECK);
    assert(entity.getState().equals(EmployeeState.IN_CHECK));
    assert(!entity.getState().equals(EmployeeState.ADDED));
    employeeStateChangeService.updateEmployeeStatus(entity.getUserId(), EmployeeState.ACTIVE);
    assert(entity.getState().equals(EmployeeState.IN_CHECK));
    assert(!entity.getState().equals(EmployeeState.ACTIVE));
    employeeStateChangeService.updateEmployeeStatus(entity.getUserId(), EmployeeState.APPROVED);
    assert(entity.getState().equals(EmployeeState.APPROVED));
    assert(!entity.getState().equals(EmployeeState.IN_CHECK));
    employeeStateChangeService.updateEmployeeStatus(entity.getUserId(), EmployeeState.ADDED);
    assert(entity.getState().equals(EmployeeState.APPROVED));
    assert(!entity.getState().equals(EmployeeState.IN_CHECK));
    employeeStateChangeService.updateEmployeeStatus(entity.getUserId(), EmployeeState.ACTIVE);
    assert(entity.getState().equals(EmployeeState.ACTIVE));
    assert(!entity.getState().equals(EmployeeState.APPROVED));
  }
}
