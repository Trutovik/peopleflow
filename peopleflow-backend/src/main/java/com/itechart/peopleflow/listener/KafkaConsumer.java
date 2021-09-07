package com.itechart.peopleflow.listener;

import com.itechart.peopleflow.dto.EmployeeDto;
import com.itechart.peopleflow.entity.EmployeeEntity;
import com.itechart.peopleflow.repository.EmployeeRepository;
import com.itechart.peopleflow.service.EmployeeStateChangeInterceptor;
import com.itechart.peopleflow.service.EmployeeStateChangeService;
import com.itechart.peopleflow.service.state.EmployeeEvent;
import com.itechart.peopleflow.service.state.EmployeeState;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class KafkaConsumer {

  private final EmployeeRepository employeeRepository;
  private final EmployeeStateChangeService employeeStateChangeService;

  @KafkaListener(topics = "employee", groupId = "groupId", containerFactory = "employeeKafkaListenerContainerFactory")
  public void createEmployee(EmployeeDto employeeDto) {
    System.out.println("Consumed employee: " + employeeDto);
    EmployeeEntity employeeEntity = EmployeeEntity.builder().build();
    BeanUtils.copyProperties(employeeDto, employeeEntity);
    if (employeeEntity.getState() == null) {
      employeeEntity.setState(EmployeeState.ADDED);
      employeeRepository.save(employeeEntity);
    } else {
      employeeStateChangeService.updateEmployeeStatus(employeeDto.getUserId(), employeeDto.getState());
    }
  }
}
