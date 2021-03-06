package com.github.nhirakawa.emulator;

import java.util.Arrays;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class MemoryManagementUnit {

  private final int[] memory;
  private final int[] registers;
  private final int[] stack;
  private final boolean[] graphics;
  private final boolean[] keypad;

  @Inject
  public MemoryManagementUnit(@Named("memory") int memorySize,
                              @Named("registers") int registerSize,
                              @Named("stack") int stackSize,
                              @Named("screen.width") int graphicsWidth,
                              @Named("screen.height") int graphicsHeight,
                              @Named("keypad") int keypadSize) {
    this.memory = new int[memorySize];
    this.registers = new int[registerSize];
    this.stack = new int[stackSize];
    this.graphics = new boolean[graphicsWidth * graphicsHeight];
    this.keypad = new boolean[keypadSize];
  }

  public int readMemory(int offset) {
    return memory[offset];
  }

  public int[] viewMemory() {
    return Arrays.copyOf(memory, memory.length);
  }

  public void writeMemory(int offset, int value) {
    memory[offset] = value;
  }

  public void writeMemory(int offset, int[] values) {
    System.arraycopy(values, 0, memory, offset, values.length);
  }

  public int readRegister(int offset) {
    return registers[offset];
  }

  public int[] viewRegisters() {
    return Arrays.copyOf(registers, registers.length);
  }

  public void writeRegister(int offset, int value) {
    registers[offset] = value;
  }

  public int readStack(int offset) {
    return stack[offset];
  }

  public int[] viewStack() {
    return Arrays.copyOf(stack, stack.length);
  }

  public void writeStack(int offset, int value) {
    stack[offset] = value;
  }

  public boolean readGraphics(int offset) {
    return graphics[offset];
  }

  public boolean[] viewGraphics() {
    return Arrays.copyOf(graphics, graphics.length);
  }

  public boolean writeGraphics(int offset, boolean value) {
    boolean unset = graphics[offset] && !value;
    graphics[offset] = value;
    return unset;
  }

  public boolean writeGraphics(int offset, boolean[] values) {
    boolean unset = false;
    for (int i = 0; i < values.length; i++) {
      unset |= writeGraphics(offset + i, values[i]);
    }
    return unset;
  }

  public void clearGraphics() {
    for (int i = 0; i < graphics.length; i++) {
      graphics[i] = false;
    }
  }

  public boolean readKeypad(int offset) {
    return keypad[offset];
  }

  public boolean[] viewKeypad() {
    return Arrays.copyOf(keypad, keypad.length);
  }

  public void writeKeypad(int offset, boolean value) {
    keypad[offset] = value;
  }

}
