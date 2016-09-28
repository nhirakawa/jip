package com.github.nhirakawa.emulator;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

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
    int opcode = fetchOpCode();
    programCounter += 2;
    System.out.println(String.format("0x%s", Integer.toHexString(opcode)));
  }

  private int fetchOpCode() {
    int op1 = memoryManagementUnit.readMemory(ROM_OFFSET + programCounter);
    int op2 = memoryManagementUnit.readMemory(ROM_OFFSET + programCounter + 1);
    return op1 << 8 | op2;
  }

}
