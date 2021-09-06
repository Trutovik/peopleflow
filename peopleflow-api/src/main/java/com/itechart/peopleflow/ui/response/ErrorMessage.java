package com.itechart.peopleflow.ui.response;

public enum ErrorMessage {
  ADD_OR_UPDATE_EMPLOYEE_EXCEPTION("Add or update employee failed"),
  INTERNAL_SERVER_ERROR("Internal server error");

  private String errorMessage;

  ErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}
