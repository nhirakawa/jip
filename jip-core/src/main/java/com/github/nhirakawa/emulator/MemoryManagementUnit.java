package com.github.nhirakawa.emulator;

public class MemoryManagementUnit {

  private final int[] memory;
  private final int[] registers;
  private final int[] stack;
  private final boolean[] graphics;
  private final boolean[] keypad;

  public MemoryManagementUnit(int memorySize,
                              int registerSize,
                              int stackSize,
                              int graphicsWidth,
                              int graphicsHeight,
                              int keypadSize) {
    this.memory = new int[memorySize];
    this.registers = new int[registerSize];
    this.stack = new int[stackSize];
    this.graphics = new boolean[graphicsWidth * graphicsHeight];
    this.keypad = new boolean[keypadSize];
  }

  public int readMemory(int offset) {
    return memory[offset];
  }

  public void writeMemory(int offset, int value) {
    memory[offset] = value;
  }

  public int readRegister(int offset) {
    return registers[offset];
  }

  public void writeRegister(int offset, int value) {
    registers[offset] = value;
  }

  public int readStack(int offset) {
    return stack[offset];
  }

  public void writeStack(int offset, int value) {
    stack[offset] = value;
  }

  public boolean readGraphics(int offset) {
    return graphics[offset];
  }

  public void writeGraphics(int offset, boolean value) {
    graphics[offset] = value;
  }

  public boolean readKeypad(int offset) {
    return keypad[offset];
  }

  public void writeKeypad(int offset, boolean value) {
    keypad[offset] = value;
  }

}
