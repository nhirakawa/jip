package com.github.nhirakawa.emulator;

public class JipEmulator {

  private static final int FONT_OFFSET = 0x050;
  private static final int ROM_OFFSET = 0x200;
  private static final int MEMORY_SIZE = 4096;
  private static final int REGISTER_SIZE = 16;
  private static final int STACK_SIZE = 16;
  private static final int KEYPAD_SIZE = 16;
  private static final int SCREEN_WIDTH = 64;
  private static final int SCREEN_HEIGHT = 32;
  private static final int SCREEN_SIZE = SCREEN_WIDTH * SCREEN_HEIGHT;

  private int indexRegister;
  private int programCounter;
  private int stackPointer;

  private final MemoryManagementUnit memoryManagementUnit;

  public JipEmulator(byte[] rom) {
    memoryManagementUnit = new MemoryManagementUnit(MEMORY_SIZE, REGISTER_SIZE, STACK_SIZE, SCREEN_WIDTH, SCREEN_HEIGHT, KEYPAD_SIZE);
  }

}
