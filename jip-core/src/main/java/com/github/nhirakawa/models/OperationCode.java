package com.github.nhirakawa.models;

import com.github.nhirakawa.exceptions.UnknownOperationException;

public enum OperationCode {
  OP00E0,
  OP00EE,
  OP1NNN,
  OP2NNN,
  OP3XNN,
  OP4XNN,
  OP5XY0,
  OP6XNN,
  OP7XNN,
  OP8XY0,
  OP8XY1,
  OP8XY2,
  OP8XY3,
  OP8XY4,
  OP8XY5,
  OP8XY6,
  OP8XY7,
  OP8XYE,
  OP9XY0,
  OPANNN,
  OPBNNN,
  OPCXNN,
  OPDXYN,
  OPEX9E,
  OPEXA1,
  OPFX07,
  OPFX0A,
  OPFX15,
  OPFX18,
  OPFX1E,
  OPFX29,
  OPFX33,
  OPFX55,
  OPFX65;

  public static OperationCode fromInteger(int code) {
    int prefix = code & 0xF000;
    switch (prefix) {
      case 0x0000:
        switch (code) {
          case 0x00E0:
            return OP00E0;
          case 0x00EE:
            return OP00EE;
        }
      case 0x1000:
        return OP1NNN;
      case 0x2000:
        return OP2NNN;
      case 0x3000:
        return OP3XNN;
      case 0x4000:
        return OP4XNN;
      case 0x5000:
        return OP5XY0;
      case 0x6000:
        return OP6XNN;
      case 0x7000:
        return OP7XNN;
      case 0x8000:
        switch (code & 0x000F) {
          case 0x0000:
            return OP8XY0;
          case 0x0001:
            return OP8XY1;
          case 0x0002:
            return OP8XY2;
          case 0x0003:
            return OP8XY3;
          case 0x0004:
            return OP8XY4;
          case 0x0005:
            return OP8XY5;
          case 0x0006:
            return OP8XY6;
          case 0x0007:
            return OP8XY7;
          case 0x000E:
            return OP8XYE;
        }
      case 0x9000:
        return OP9XY0;
      case 0xA000:
        return OPANNN;
      case 0xB000:
        return OPBNNN;
      case 0xC000:
        return OPCXNN;
      case 0xD000:
        return OPDXYN;
      case 0xE000:
        switch (code & 0x00FF) {
          case 0x009E:
            return OPEX9E;
          case 0x00A1:
            return OPEXA1;
        }
      case 0xF000:
        switch (code & 0x00FF) {
          case 0x0007:
            return OPFX07;
          case 0x000A:
            return OPFX0A;
          case 0x0015:
            return OPFX15;
          case 0x0018:
            return OPFX18;
          case 0x001E:
            return OPFX1E;
          case 0x0029:
            return OPFX29;
          case 0x0033:
            return OPFX33;
          case 0x0055:
            return OPFX55;
          case 0x0065:
            return OPFX65;
        }
      default:
        throw new UnknownOperationException(code);
    }
  }
}
