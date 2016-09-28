package com.github.nhirakawa.models;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum OpCode {
  OP_00E0("00E0", 0x0),
  OP_00EE("00EE", 0x0),
  OP_1NNN("1NNN", 0x1),
  OP_2NNN("2NNN", 0x2),
  OP_3XNN("3XNN", 0x3),
  OP_4XNN("4XNN", 0x4),
  OP_5XY0("5XY0", 0x5),
  OP_6XNN("6XNN", 0x6),
  OP_7XNN("7XNN", 0x7),
  OP_8XY0("8XY0", 0x8),
  OP_8XY1("8XY0", 0x8),
  OP_8XY2("8XY2", 0x8),
  OP_8XY3("8XY3", 0x8),
  OP_8XY4("8XY4", 0x8),
  OP_8XY5("8XY5", 0x8),
  OP_8XY6("8XY6", 0x8),
  OP_8XY7("8XY7", 0x8),
  OP_8XYE("8XYE", 0x8),
  OP_9XY0("9XY0", 0x9),
  OP_ANNN("ANNN", 0xA),
  OP_BNNN("BNNN", 0xB),
  OP_CXNN("CXNN", 0xC),
  OP_DXYN("DXYN", 0xD),
  OP_EX9E("EX9E", 0xE),
  OP_EXA1("EXA1", 0xE),
  OP_FX07("FX07", 0xF),
  OP_FX0A("FX0A", 0xF),
  OP_FX15("FX15", 0xF),
  OP_FX18("FX18", 0xF),
  OP_FX1E("FX1E", 0xF),
  OP_FX29("FX29", 0xF),
  OP_FX33("FX33", 0xF),
  OP_FX55("FX55", 0xF),
  OP_FX65("FX65", 0xF);

  final String message;
  final int prefix;

  OpCode(String message, int prefix) {
    this.message = message;
    this.prefix = prefix;
  }

  public int getPrefix() {
    return prefix;
  }

  @Override
  public String toString(){
    return message;
  }

  public static final Map<Integer, List<OpCode>> OP_CODES_BY_PREFIX = EnumSet.allOf(OpCode.class).stream()
      .collect(Collectors.groupingBy(OpCode::getPrefix));

}
