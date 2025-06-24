package com.skysoft.app.exception;

import lombok.Getter;

@Getter
public class TechnicalErrorException extends Exception {
  private ErrorCodes errorCode;

  public TechnicalErrorException(String message) {
    super(message);
  }

  public TechnicalErrorException(String message, Throwable cause, ErrorCodes errorCode) {
    super(message, cause);
    this.errorCode = errorCode;
  }

  public TechnicalErrorException(String message, ErrorCodes errorCode) {
    super(message);
    this.errorCode = errorCode;
  }
}
