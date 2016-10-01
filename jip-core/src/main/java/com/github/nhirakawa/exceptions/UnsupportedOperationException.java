package com.github.nhirakawa.exceptions;

import com.github.nhirakawa.models.OpCodeType;

public class UnsupportedOperationException extends RuntimeException {

  public UnsupportedOperationException(OpCodeType opCode) {
    super(String.format("%s is not supported", opCode));
  }
}
