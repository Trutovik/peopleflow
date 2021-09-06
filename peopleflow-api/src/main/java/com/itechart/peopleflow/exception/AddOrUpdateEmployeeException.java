package com.itechart.peopleflow.exception;

public class AddOrUpdateEmployeeException extends RuntimeException{

  private static final long serialVersionUID = 3L;

  public AddOrUpdateEmployeeException(String message) {
    super(message);
  }
}
