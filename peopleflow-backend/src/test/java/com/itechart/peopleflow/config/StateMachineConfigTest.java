package com.itechart.peopleflow.config;

import com.itechart.peopleflow.service.state.EmployeeEvent;
import com.itechart.peopleflow.service.state.EmployeeState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import java.util.UUID;

@SpringBootTest
class StateMachineConfigTest {

  @Autowired
  StateMachineFactory<EmployeeState, EmployeeEvent> factory;

  @Test
  void testNewStateMachine() {
    StateMachine<EmployeeState, EmployeeEvent> sm = factory.getStateMachine(UUID.randomUUID());
    sm.start();
    sm.sendEvent(EmployeeEvent.ADD);
    assert(sm.getState().getId().equals(EmployeeState.ADDED));
    sm.sendEvent(EmployeeEvent.CHECK);
    assert(sm.getState().getId().equals(EmployeeState.IN_CHECK));
    sm.sendEvent(EmployeeEvent.ADD);
    assert(!sm.getState().getId().equals(EmployeeState.ADDED));
    assert(sm.getState().getId().equals(EmployeeState.IN_CHECK));
    sm.sendEvent(EmployeeEvent.APPROVE);
    assert(!sm.getState().getId().equals(EmployeeState.IN_CHECK));
    assert(sm.getState().getId().equals(EmployeeState.APPROVED));
    sm.sendEvent(EmployeeEvent.ACTIVATE);
    assert(!sm.getState().getId().equals(EmployeeState.APPROVED));
    assert(sm.getState().getId().equals(EmployeeState.ACTIVE));

  }
}