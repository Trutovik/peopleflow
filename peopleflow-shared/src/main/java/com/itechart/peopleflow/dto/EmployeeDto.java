package com.itechart.peopleflow.dto;

import com.itechart.peopleflow.service.state.EmployeeEvent;
import com.itechart.peopleflow.service.state.EmployeeState;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmployeeDto implements Serializable {

  private static final long serialVersionUID = 1L;
  private String id;
  private String userId;
  private String firstName;
  private String lastName;
  private Integer age;
  private String email;
  private String password;
  private EmployeeState state;
  private EmployeeEvent event;
}
