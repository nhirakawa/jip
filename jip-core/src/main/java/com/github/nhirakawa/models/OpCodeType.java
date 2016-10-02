package com.github.nhirakawa.models;

import com.github.nhirakawa.exceptions.UnknownOperationException;

public enum OpCodeType {
  OP_00E0("00E0", 0x00E0, 0xF0FF) {
    public String describe(int op) {
      return "clear screen";
    }
  },
  OP_00EE("00EE", 0x00EE, 0xF0FF) {
    public String describe(int op) {
      return "return from subroutine";
    }
  },
  OP_1NNN("1NNN", 0x1000, 0xF000) {
    public int getN(int op) {
      return op & 0x0FFF;
    }

    public String describe(int op) {
      return String.format("jump %s", hex(op));
    }
  },
  OP_2NNN("2NNN", 0x2000, 0xF000) {
    public int getN(int op) {
      return op & 0x0FFF;
    }

    public String describe(int op) {
      return String.format("call %s", hex(op));
    }
  },
  OP_3XNN("3XNN", 0x3000, 0xF000) {
    public int getX(int op) {
      return (op & 0x0F00) >> 8;
    }

    public int getN(int op) {
      return op & 0x00FF;
    }

    public String describe(int op) {
      return String.format("skip V%X eq %X", getX(op), getN(op));
    }
  },
  OP_4XNN("4XNN", 0x4000, 0xF000) {
    public int getX(int op) {
      return (op & 0x0F00) >> 8;
    }

    public int getN(int op) {
      return op & 0x00FF;
    }

    public String describe(int op) {
      return String.format("skip V%X neq %X", getX(op), getN(op));
    }
  },
  OP_5XY0("5XY0", 0x5000, 0xF00F) {
    public int getX(int op) {
      return (op & 0x0F00) >> 8;
    }

    public int getY(int op) {
      return (op & 0x00F0) >> 4;
    }

    public String describe(int op) {
      return String.format("skip V%X eq V%X", getX(op), getY(op));
    }
  },
  OP_6XNN("6XNN", 0x6000, 0xF000) {
    public int getX(int op) {
      return (op & 0x0F00) >> 8;
    }

    public int getN(int op) {
      return op & 0x00FF;
    }

    public String describe(int op) {
      return String.format("set V%X %X", getX(op), getN(op));
    }
  },
  OP_7XNN("7XNN", 0x7000, 0xF000) {
    public int getX(int op) {
      return (op & 0x0F00) >> 8;
    }

    public int getN(int op) {
      return op & 0x00FF;
    }

    public String describe(int op) {
      return String.format("add %X %X", getN(op), getX(op));
    }
  },
  OP_8XY0("8XY0", 0x8000, 0xF00F) {
    public int getX(int op) {
      return (op & 0x0F00) >> 8;
    }

    public int getY(int op) {
      return (op & 0x00F0) >> 4;
    }

    public String describe(int op) {
      return String.format("set V%X V%X", getX(op), getY(op));
    }
  },
  OP_8XY1("8XY1", 0x8001, 0xF00F) {
    public int getX(int op) {
      return (op & 0x0F00) >> 8;
    }

    public int getY(int op) {
      return (op & 0x00F0) >> 4;
    }

    public String describe(int op) {
      return String.format("set V%X V%X or V%X", getX(op), getX(op), getY(op));
    }
  },
  OP_8XY2("8XY2", 0x8002, 0xF00F) {
    public int getX(int op) {
      return (op & 0x0F00) >> 8;
    }

    public int getY(int op) {
      return (op & 0x00F0) >> 4;
    }

    public String describe(int op) {
      return String.format("set V%X V%X and V%X", getX(op), getX(op), getY(op));
    }
  },
  OP_8XY3("8XY3", 0x8003, 0xF00F) {
    public int getX(int op) {
      return (op & 0x0F00) >> 8;
    }

    public int getY(int op) {
      return (op & 0x00F0) >> 4;
    }

    public String describe(int op) {
      return String.format("set V%X V%X xor V%X", getX(op), getX(op), getY(op));
    }
  },
  OP_8XY4("8XY4", 0x8004, 0xF00F) {
    public int getX(int op) {
      return (op & 0x0F00) >> 8;
    }

    public int getY(int op) {
      return (op & 0x00F0) >> 4;
    }

    public String describe(int op) {
      return String.format("add V%X V%X", getY(op), getX(op));
    }
  },
  OP_8XY5("8XY5", 0x8005, 0xF00F) {
    public int getX(int op) {
      return (op & 0x0F00) >> 8;
    }

    public int getY(int op) {
      return (op & 0x00F0) >> 4;
    }

    public String describe(int op) {
      return String.format("sub V%X V%X", getY(op), getX(op));
    }
  },
  OP_8XY6("8XY6", 0x8006, 0xF00F) {
    public int getX(int op) {
      return (op & 0x0F00) >> 8;
    }

    public int getY(int op) {
      return (op & 0x00F0) >> 4;
    }

    public String describe(int op) {
      return String.format("shiftr V%X", getX(op));
    }
  },
  OP_8XY7("8XY7", 0x8007, 0xF00F) {
    public int getX(int op) {
      return (op & 0x0F00) >> 8;
    }

    public int getY(int op) {
      return (op & 0x00F0) >> 4;
    }

    public String describe(int op) {
      return String.format("set V%X V%X sub V%X", getX(op), getY(op), getX(op));
    }
  },
  OP_8XYE("8XYE", 0x800E, 0xF00F) {
    public int getX(int op) {
      return (op & 0x0F00) >> 8;
    }

    public int getY(int op) {
      return (op & 0x00F0) >> 4;
    }

    public String describe(int op) {
      return String.format("shiftl V%X", getX(op));
    }
  },
  OP_9XY0("9XY0", 0x9000, 0xF00F) {
    public int getX(int op) {
      return (op & 0x0F00) >> 8;
    }

    public int getY(int op) {
      return (op & 0x00F0) >> 4;
    }

    public String describe(int op) {
      return String.format("skip V%X neq V%X", getX(op), getY(op));
    }
  },
  OP_ANNN("ANNN", 0xA000, 0xF000) {
    public int getN(int op) {
      return op & 0x0FFF;
    }

    public String describe(int op) {
      return String.format("set I %X", getN(op));
    }
  },
  OP_BNNN("BNNN", 0xB000, 0xF000) {
    public int getN(int op) {
      return op & 0x0FFF;
    }

    public String describe(int op) {
      return String.format("jump %s V0", hex(op));
    }
  },
  OP_CXNN("CXNN", 0xC000, 0xF000) {
    public int getX(int op) {
      return (op & 0x0F00) >> 8;
    }

    public int getN(int op) {
      return op & 0x00FF;
    }

    public String describe(int op) {
      return String.format("set V%X rnd and %X", getX(op), getN(op));
    }
  },
  OP_DXYN("DXYN", 0xD000, 0xF000) {
    public int getX(int op) {
      return (op & 0x0F00) >> 8;
    }

    public int getY(int op) {
      return (op & 0x00F0) >> 4;
    }

    public int getN(int op) {
      return op & 0x000F;
    }

    public String describe(int op) {
      return String.format("draw V%X V%X %d", getX(op), getY(op), getN(op));
    }
  },
  OP_EX9E("EX9E", 0xE09E, 0xF0FF) {
    public int getX(int op) {
      return (op & 0x0F00) >> 8;
    }

    public String describe(int op) {
      return String.format("skip V%X press", getX(op));
    }
  },
  OP_EXA1("EXA1", 0xE0A1, 0xF0FF) {
    public int getX(int op) {
      return (op & 0x0F00) >> 8;
    }

    public String describe(int op) {
      return String.format("skip V%X npress", getX(op));
    }
  },
  OP_FX07("FX07", 0xF007, 0xF0FF) {
    public int getX(int op) {
      return (op & 0x0F00) >> 8;
    }

    public String describe(int op) {
      return String.format("set V%X delay", getX(op));
    }
  },
  OP_FX0A("FX0A", 0xF00A, 0xF0FF) {
    public int getX(int op) {
      return (op & 0x0F00) >> 8;
    }

    public String describe(int op) {
      return String.format("key V%X", getX(op));
    }
  },
  OP_FX15("FX15", 0xF015, 0xF0FF) {
    public int getX(int op) {
      return (op & 0x0F00) >> 8;
    }

    public String describe(int op) {
      return String.format("set delay V%X", getX(op));
    }
  },
  OP_FX18("FX18", 0xF018, 0xF0FF) {
    public int getX(int op) {
      return (op & 0x0F00) >> 8;
    }

    public String describe(int op) {
      return String.format("set sound V%X", getX(op));
    }
  },
  OP_FX1E("FX1E", 0xF01E, 0xF0FF) {
    public int getX(int op) {
      return (op & 0x0F00) >> 8;
    }

    public String describe(int op) {
      return String.format("add I V%X", getX(op));
    }
  },
  OP_FX29("FX29", 0xF029, 0xF0FF) {
    public int getX(int op) {
      return (op & 0x0F00) >> 8;
    }

    public String describe(int op) {
      return String.format("set I sprite V%X", getX(op));
    }
  },
  OP_FX33("FX33", 0xF033, 0xF0FF) {
    public int getX(int op) {
      return (op & 0x0F00) >> 8;
    }

    public String describe(int op) {
      return String.format("store vcd V%X", getX(op));
    }
  },
  OP_FX55("FX55", 0xF055, 0xF0FF) {
    public int getX(int op) {
      return (op & 0x0F00) >> 8;
    }

    public String describe(int op) {
      return String.format("store V0 V%X", getX(op));
    }
  },
  OP_FX65("FX65", 0xF065, 0xF0FF) {
    public int getX(int op) {
      return (op & 0x0F00) >> 8;
    }

    public String describe(int op) {
      return String.format("load V0 V%X", getX(op));
    }
  };

  final String message;
  final int signature;
  final int bitmask;

  OpCodeType(String message,
             int signature,
             int sigMask) {
    this.message = message;
    this.signature = signature;
    this.bitmask = sigMask;
  }

  private int getSignature() {
    return signature;
  }

  private int getBitmask() {
    return bitmask;
  }

  public int getX(int op) {
    return Integer.MIN_VALUE;
  }

  public int getY(int op) {
    return Integer.MIN_VALUE;
  }

  public int getN(int op) {
    return Integer.MIN_VALUE;
  }

  public abstract String describe(int op);

  private static String hex(int op) {
    return String.format("%04X", op);
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
