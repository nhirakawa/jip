package com.github.nhirakawa.models;

import com.github.nhirakawa.exceptions.UnknownOperationException;

public enum OpCodeType {
  OP_00E0("00E0", 0x00E0, 0xF0FF),
  OP_00EE("00EE", 0x00EE, 0xF0FF),
  OP_1NNN("1NNN", 0x1000, 0xF000, 0x0000, 0x0000, 0x0FFF),
  OP_2NNN("2NNN", 0x2000, 0xF000, 0x0000, 0x0000, 0x0FFF),
  OP_3XNN("3XNN", 0x3000, 0xF000, 0x0F00, 0x0000, 0x00FF),
  OP_4XNN("4XNN", 0x4000, 0xF000, 0x0F00, 0x0000, 0x00FF),
  OP_5XY0("5XY0", 0x5000, 0xF000, 0x0F00, 0x00F0),
  OP_6XNN("6XNN", 0x6000, 0xF000, 0x0F00, 0x0000, 0x00FF),
  OP_7XNN("7XNN", 0x7000, 0xF000, 0x0F00, 0x0000, 0x00FF),
  OP_8XY0("8XY0", 0x8000, 0xF00F, 0x0F00, 0x00F0),
  OP_8XY1("8XY1", 0x8001, 0xF00F, 0x0F00, 0x00F0),
  OP_8XY2("8XY2", 0x8002, 0xF00F, 0x0F00, 0x00F0),
  OP_8XY3("8XY3", 0x8003, 0xF00F, 0x0F00, 0x00F0),
  OP_8XY4("8XY4", 0x8004, 0xF00F, 0x0F00, 0x00F0),
  OP_8XY5("8XY5", 0x8005, 0xF00F, 0x0F00, 0x00F0),
  OP_8XY6("8XY6", 0x8006, 0xF00F, 0x0F00, 0x00F0),
  OP_8XY7("8XY7", 0x8007, 0xF00F, 0x0F00, 0x00F0),
  OP_8XYE("8XYE", 0x800E, 0xF00F, 0x0F00, 0x00F0),
  OP_9XY0("9XY0", 0x9000, 0xF00F, 0x0F00, 0x00F0),
  OP_ANNN("ANNN", 0xA000, 0xF000, 0x0000, 0x0000, 0x0FFF),
  OP_BNNN("BNNN", 0xB000, 0xF000, 0x0000, 0x0000, 0x0FFF),
  OP_CXNN("CXNN", 0xC000, 0xF000, 0x0F00, 0x0000, 0x00FF),
  OP_DXYN("DXYN", 0xD000, 0xF000, 0x0F00, 0x00F0, 0x000F),
  OP_EX9E("EX9E", 0xE09E, 0xF0FF, 0x0F00),
  OP_EXA1("EXA1", 0xE0A1, 0xF0FF, 0x0F00),
  OP_FX07("FX07", 0xF007, 0xF0FF, 0x0F00),
  OP_FX0A("FX0A", 0xF00A, 0xF0FF, 0x0F00),
  OP_FX15("FX15", 0xF015, 0xF0FF, 0x0F00),
  OP_FX18("FX18", 0xF018, 0xF0FF, 0x0F00),
  OP_FX1E("FX1E", 0xF01E, 0xF0FF, 0x0F00),
  OP_FX29("FX29", 0xF029, 0xF0FF, 0x0F00),
  OP_FX33("FX33", 0xF033, 0xF0FF, 0x0F00),
  OP_FX55("FX55", 0xF055, 0xF0FF, 0x0F00),
  OP_FX65("FX65", 0xF065, 0xF00F, 0x0F00);

  final String message;
  final int signature;
  final int bitmask;
  final int xMask;
  final int yMask;
  final int nMask;

  OpCodeType(String message,
             int signature,
             int mask) {
    this(message, signature, mask, 0, 0, 0);
  }

  OpCodeType(String message,
             int signature,
             int mask,
             int xMask) {
    this(message, signature, mask, xMask, 0, 0);
  }

  OpCodeType(String message,
             int signature,
             int mask,
             int xMask,
             int yMask) {
    this(message, signature, mask, xMask, yMask, 0);
  }

  OpCodeType(String message,
             int signature,
             int sigMask,
             int xMask,
             int yMask,
             int nMask) {
    this.message = message;
    this.signature = signature;
    this.bitmask = sigMask;
    this.xMask = xMask;
    this.yMask = yMask;
    this.nMask = nMask;
  }

  private int getSignature() {
    return signature;
  }

  private int getBitmask() {
    return bitmask;
  }

  public int getX(int op) {
    return (op & xMask) >> 8;
  }

  public int getY(int op) {
    return (op & yMask) >> 4;
  }

  public int getN(int op) {
    return op & nMask;
  }

  @Override
  public String toString() {
    return message;
  }

  public static OpCodeType of(int op) {
    for (OpCodeType opcode : OpCodeType.values()) {
      if ((op & opcode.getBitmask()) == opcode.getSignature()) {
        return opcode;
      }
    }
    throw new UnknownOperationException(op);
  }

}
