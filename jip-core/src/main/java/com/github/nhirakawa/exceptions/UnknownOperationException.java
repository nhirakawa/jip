package com.github.nhirakawa.exceptions;

public class UnknownOperationException extends RuntimeException {

  public UnknownOperationException(int code) {
    super(String.format("%d is not a known opcode", code));
  }
}
