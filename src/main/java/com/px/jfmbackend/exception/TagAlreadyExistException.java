package com.px.jfmbackend.exception;

public class TagAlreadyExistException extends RuntimeException {
  public TagAlreadyExistException(String message) {
    super(message);
  }
}
