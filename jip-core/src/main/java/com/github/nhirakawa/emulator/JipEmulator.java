package com.github.nhirakawa.emulator;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import com.github.nhirakawa.models.OpCode;
import com.google.inject.Inject;

public class JipEmulator {

  private static final int FONT_OFFSET = 0x050;
  private static final int ROM_OFFSET = 0x200;

  private int indexRegister;
  private int programCounter;
  private int stackPointer;

  private final MemoryManagementUnit memoryManagementUnit;

  @Inject
  public JipEmulator(MemoryManagementUnit memoryManagementUnit) {
    this.memoryManagementUnit = memoryManagementUnit;
    this.indexRegister = 0;
    this.programCounter = 0;
    this.stackPointer = 0;
  }

  public void loadRom(File file) throws IOException {
    byte[] rom = IOUtils.toByteArray(file.toURI());
    loadRom(rom);
  }

  public void loadRom(byte[] rom) {
    int[] translatedRom = new int[rom.length];
    for (int i = 0; i < rom.length; i++) {
      translatedRom[i] = (rom[i] & 0xFF);
    }
    memoryManagementUnit.writeMemory(ROM_OFFSET, translatedRom);
  }

  public void step() {
    OpCode opcode = fetchOpCode();
    programCounter += 2;
    System.out.println(opcode);
  }

  private OpCode fetchOpCode() {
    int op1 = memoryManagementUnit.readMemory(ROM_OFFSET + programCounter);
    int op2 = memoryManagementUnit.readMemory(ROM_OFFSET + programCounter + 1);
    return OpCode.of(op1 << 8 | op2);
  }

  private void executeOpCode(OpCode opcode) {
    switch (opcode) {
      default:
        throw new UnsupportedOperationException(opcode);
    }
  }

  private static int getX(int op) {
    return op & 0x0F00 >> 8;
  }

  private static int getY(int op) {
    return op & 0x00F0 >> 4;
  }

  private static int getN(int op, int places) {
    if (places < 1 || places > 3) {
      throw new IllegalArgumentException(String.format("cannot get %d places", places));
    }
    int mask = 0xF;
    while (places > 1) {
      mask = (mask << 4) & 0xF;
    }
    return op & mask;
  }

  public static class UnsupportedOperationException extends RuntimeException {

    public UnsupportedOperationException(OpCode opCode) {
      super(String.format("%s is not supported yet", opCode));
    }
  }

}
