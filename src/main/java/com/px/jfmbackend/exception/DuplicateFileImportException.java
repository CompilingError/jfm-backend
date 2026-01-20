package com.px.jfmbackend.exception;

public class DuplicateFileImportException extends RuntimeException {
  public DuplicateFileImportException(String message) {
    super(message);
  }
}
