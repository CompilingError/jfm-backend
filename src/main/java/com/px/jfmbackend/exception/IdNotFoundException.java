package com.px.jfmbackend.exception;

public class IdNotFoundException extends RuntimeException {
  public IdNotFoundException(String message) {
    super(message);
  }
}
