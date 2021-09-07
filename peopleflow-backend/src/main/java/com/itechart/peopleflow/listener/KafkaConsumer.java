package com.itechart.peopleflow.listener;

import com.itechart.peopleflow.dto.EmployeeDto;
import com.itechart.peopleflow.entity.EmployeeEntity;
import com.itechart.peopleflow.repository.EmployeeRepository;
import com.itechart.peopleflow.service.EmployeeStateChangeInterceptor;
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

  public static final String EMPLOYEE_ID_HEADER = "employee_id";

  private final EmployeeRepository employeeRepository;
  private final EmployeeStateChangeInterceptor employeeStateChangeInterceptor;
  private final StateMachineFactory<EmployeeState, EmployeeEvent> stateMachineFactory;

  @KafkaListener(topics = "employee", groupId = "groupId", containerFactory = "employeeKafkaListenerContainerFactory")
  public void createEmployee(EmployeeDto employeeDto) {
    System.out.println("Consumed employee: " + employeeDto);
    EmployeeEntity employeeEntity = EmployeeEntity.builder().build();
    BeanUtils.copyProperties(employeeDto, employeeEntity);
    if (employeeEntity.getState() == null) {
      employeeEntity.setState(EmployeeState.ADDED);
      employeeRepository.save(employeeEntity);
    } else {
      updateEmployeeStatus(employeeDto.getUserId(), employeeDto.getState());
    }
  }

  @Transactional
  public EmployeeDto updateEmployeeStatus(String userId, EmployeeState status) {
    EmployeeEvent event = getEmployeeEventByEmployeeState(status);
    StateMachine<EmployeeState, EmployeeEvent> sm = build(userId);
    sendEvent(userId, sm, event);
    return null;
  }

  private StateMachine<EmployeeState, EmployeeEvent> build(String employeeId) {
    EmployeeEntity employee = employeeRepository.findByUserId(employeeId);

    StateMachine<EmployeeState, EmployeeEvent> sm = stateMachineFactory.getStateMachine(employee.getUserId());
    sm.stop();
    sm.getStateMachineAccessor()
        .doWithAllRegions(sma -> {
          sma.addStateMachineInterceptor(employeeStateChangeInterceptor);
          sma.resetStateMachine(new DefaultStateMachineContext<>(employee.getState(), null, null, null));
        });
    sm.start();
    return sm;
  }

  private void sendEvent(String employeeId, StateMachine<EmployeeState, EmployeeEvent> sm, EmployeeEvent event) {
    Message msg = MessageBuilder.withPayload(event)
        .setHeader(EMPLOYEE_ID_HEADER, employeeId)
        .build();
    sm.sendEvent(msg);
  }

  private EmployeeEvent getEmployeeEventByEmployeeState(EmployeeState state) {
    switch (state) {
      case ACTIVE:
        return EmployeeEvent.ACTIVATE;
      case APPROVED:
        return EmployeeEvent.APPROVE;
      case IN_CHECK:
        return EmployeeEvent.CHECK;
      default:
        return EmployeeEvent.ADD;
    }
  }
}
