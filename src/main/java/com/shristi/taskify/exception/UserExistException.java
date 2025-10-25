package com.shristi.taskify.exception;

public class UserExistException extends RuntimeException {
  public UserExistException(String message) {
    super(message);
  }
}
