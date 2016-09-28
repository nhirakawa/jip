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
  }

  public void loadRom(File file) throws IOException {
    byte[] rom = IOUtils.toByteArray(file.toURI());
    int[] translatedRom = new int[rom.length];
    for (int i = 0; i < rom.length; i++) {
      translatedRom[i] = (rom[i] & 0xFF);
    }
    memoryManagementUnit.writeMemory(ROM_OFFSET, translatedRom);
  }

}
