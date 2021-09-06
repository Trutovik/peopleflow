package com.itechart.peopleflow.entity;

import com.itechart.peopleflow.service.state.EmployeeState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "EMPLOYEES")
public class EmployeeEntity implements Serializable {

  private static final long serialVersionUID = 2L;

  @Id
  @GeneratedValue
  private long id;

  private String userId;

  private String firstName;

  private String lastName;

  private Integer age;

  private String email;

  private String password;

  @Enumerated(EnumType.STRING)
  private EmployeeState state;
}