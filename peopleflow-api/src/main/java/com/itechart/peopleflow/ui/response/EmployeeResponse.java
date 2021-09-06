package com.itechart.peopleflow.ui.response;

import com.itechart.peopleflow.service.state.EmployeeState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse {

  private String firstName;
  private String lastName;
  private Integer age;
  private String email;
  private EmployeeState state;
}
