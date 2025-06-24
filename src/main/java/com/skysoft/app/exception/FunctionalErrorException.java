package com.skysoft.app.exception;

import java.util.List;
import lombok.Getter;

@Getter
public class FunctionalErrorException extends RuntimeException {

  private ErrorCodes errorCode;

  private List<String> errors;

  public FunctionalErrorException(String message, Throwable cause, ErrorCodes errorCode) {
    super(message, cause);
    this.errorCode = errorCode;
  }

  public FunctionalErrorException(String message, ErrorCodes errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

  public FunctionalErrorException(String message, ErrorCodes errorCode, List<String> errors) {
    super(message);
    this.errorCode = errorCode;
    this.errors = errors;
  }
}
