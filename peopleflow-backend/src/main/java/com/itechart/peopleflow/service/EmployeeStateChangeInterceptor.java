package com.itechart.peopleflow.service;

import com.itechart.peopleflow.entity.EmployeeEntity;
import com.itechart.peopleflow.listener.KafkaConsumer;
import com.itechart.peopleflow.repository.EmployeeRepository;
import com.itechart.peopleflow.service.state.EmployeeEvent;
import com.itechart.peopleflow.service.state.EmployeeState;
import lombok.AllArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class EmployeeStateChangeInterceptor extends StateMachineInterceptorAdapter<EmployeeState, EmployeeEvent> {

  private final EmployeeRepository employeeRepository;

  @Override
  public void preStateChange(State<EmployeeState, EmployeeEvent> state, Message<EmployeeEvent> message, Transition<EmployeeState, EmployeeEvent> transition, StateMachine<EmployeeState, EmployeeEvent> stateMachine, StateMachine<EmployeeState, EmployeeEvent> rootStateMachine) {
    Optional.ofNullable(message).ifPresent(msg -> {
      Optional.ofNullable(String.class.cast(msg.getHeaders().getOrDefault(EmployeeStateChangeService.EMPLOYEE_ID_HEADER, "")))
          .ifPresent(employeeId -> {
            EmployeeEntity employee = employeeRepository.findByUserId(employeeId);
            employee.setState(state.getId());
            employeeRepository.save(employee);
          });
    });
  }
}