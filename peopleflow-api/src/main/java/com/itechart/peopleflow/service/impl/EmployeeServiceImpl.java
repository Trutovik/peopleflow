package com.itechart.peopleflow.service.impl;

import com.itechart.peopleflow.dto.EmployeeDto;
import com.itechart.peopleflow.exception.AddOrUpdateEmployeeException;
import com.itechart.peopleflow.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {

  private KafkaTemplate<String, EmployeeDto> kafkaTemplate;

  @Override
  public String createOrUpdateEmployee(EmployeeDto employee) {
    try {
      if (employee.getUserId() == null) {
        String userId = UUID.randomUUID().toString();
        employee.setUserId(userId);
      }
      kafkaTemplate.send("employee", employee);
      return employee.getUserId();
    } catch (Exception e) {
      throw new AddOrUpdateEmployeeException(e.getMessage());
    }
  }
}