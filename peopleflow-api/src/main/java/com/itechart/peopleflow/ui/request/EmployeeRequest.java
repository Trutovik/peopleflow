package com.itechart.peopleflow.ui.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequest {

  private String firstName;
  private String lastName;
  private Integer age;
  private String email;
  private String password;
}