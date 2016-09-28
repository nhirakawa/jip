package com.github.nhirakawa.exceptions;

public class UnknownOperationException extends RuntimeException {

  public UnknownOperationException(int code) {
    super(String.format("0x%s is not a known opcode", hexString(code)));
  }

  private static String hexString(int i) {
    return String.format("%04x", i).toUpperCase();
  }

}
