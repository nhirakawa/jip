package com.github.nhirakawa.emulator;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.nhirakawa.exceptions.UnsupportedOperationException;
import com.github.nhirakawa.models.Font;
import com.github.nhirakawa.models.OpCode;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class JipEmulator {

  private static final Logger LOG = LogManager.getLogger(JipEmulator.class);

  private static final int FONT_OFFSET = 0x050;
  private static final int ROM_OFFSET = 0x200;

  private OpCode opCode;
  private int indexRegister;
  private int programCounter;
  private int stackPointer;

  private int soundTimer;
  private int delayTimer;

  private final MemoryManagementUnit memoryManagementUnit;
  private final Random random;

  private final int SCREEN_WIDTH;
  private final int KEYPAD;

  @Inject
  public JipEmulator(MemoryManagementUnit memoryManagementUnit,
                     Random random,
                     @Named("keypad") int keypad,
                     @Named("screen.width") int width) {
    this.memoryManagementUnit = memoryManagementUnit;
    this.random = random;
    this.indexRegister = 0;
    this.programCounter = 0;
    this.stackPointer = 0;
    this.soundTimer = 0;
    this.delayTimer = 0;
    this.SCREEN_WIDTH = width;
    this.KEYPAD = keypad;
    loadFontSet();
  }

  public void loadRom(File file) throws IOException {
    loadRom(file.toURI());
  }

  public void loadRom(URI uri) throws IOException {
    loadRom(IOUtils.toByteArray(uri));
  }

  public void loadRom(byte[] rom) {
    int[] translatedRom = new int[rom.length];
    for (int i = 0; i < rom.length; i++) {
      translatedRom[i] = (rom[i] & 0xFF);
    }
    loadRom(translatedRom);
  }

  public void loadRom(int[] rom) {
    memoryManagementUnit.writeMemory(ROM_OFFSET, rom);
    programCounter = ROM_OFFSET;
  }

  OpCode getOpCode() {
    return opCode;
  }

  int getProgramCounter() {
    return programCounter;
  }

  void setProgramCounter(int i) {
    programCounter = i;
  }

  int getStackPointer() {
    return stackPointer;
  }

  void setStackPointer(int i) {
    stackPointer = i;
  }

  int getIndexRegister() {
    return indexRegister;
  }

  void setIndexRegister(int i) {
    indexRegister = i;
  }

  int getSoundTimer() {
    return soundTimer;
  }

  void setSoundTimer(int i) {
    soundTimer = i;
  }

  int getDelayTimer() {
    return delayTimer;
  }

  void setDelayTimer(int i) {
    delayTimer = i;
  }

  public void step() {
    fetchOpCode();
    int instructionsToAdvance = executeOpCode();
    programCounter += (instructionsToAdvance * 2);
  }

  public int[] getMemory() {
    return memoryManagementUnit.viewMemory();
  }

  public int[] getRegisters() {
    return memoryManagementUnit.viewRegisters();
  }

  public int[] getStack() {
    return memoryManagementUnit.viewStack();
  }

  public boolean[] getGraphics() {
    return memoryManagementUnit.viewGraphics();
  }

  public boolean[] getKeypad() {
    return memoryManagementUnit.viewKeypad();
  }

  private void fetchOpCode() {
    int op = memoryManagementUnit.readMemory(programCounter) << 8 | memoryManagementUnit.readMemory(programCounter + 1);
    opCode = OpCode.builder()
        .setOpCode(op)
        .build();
  }

  private int executeOpCode() {
    LOG.debug("executing {}", opCode);
    final int registerX;
    final int registerY;
    switch (opCode.getOpCodeType()) {
      case OP_00E0:
        memoryManagementUnit.clearGraphics();
        return 1;
      case OP_00EE:
        stackPointer--;
        programCounter = memoryManagementUnit.readStack(stackPointer);
        memoryManagementUnit.writeStack(stackPointer, 0);
        return 1;
      case OP_1NNN:
        programCounter = opCode.getN();
        return 0;
      case OP_2NNN:
        memoryManagementUnit.writeStack(stackPointer, programCounter);
        stackPointer++;
        programCounter = opCode.getN();
        return 0;
      case OP_3XNN:
        registerX = memoryManagementUnit.readRegister(opCode.getX());
        return registerX == opCode.getN() ? 2 : 1;
      case OP_4XNN:
        registerX = memoryManagementUnit.readRegister(opCode.getX());
        return registerX != opCode.getN() ? 2 : 1;
      case OP_5XY0:
        registerX = memoryManagementUnit.readRegister(opCode.getX());
        registerY = memoryManagementUnit.readRegister(opCode.getY());
        return registerX == registerY ? 2 : 1;
      case OP_6XNN:
        memoryManagementUnit.writeRegister(opCode.getX(), opCode.getN());
        return 1;
      case OP_7XNN:
        registerX = memoryManagementUnit.readRegister(opCode.getX());
        memoryManagementUnit.writeRegister(opCode.getX(), registerX + opCode.getN());
        return 1;
      case OP_8XY0:
        registerY = memoryManagementUnit.readRegister(opCode.getY());
        memoryManagementUnit.writeRegister(opCode.getX(), registerY);
        return 1;
      case OP_8XY1:
        registerX = memoryManagementUnit.readRegister(opCode.getX());
        registerY = memoryManagementUnit.readRegister(opCode.getY());
        memoryManagementUnit.writeRegister(opCode.getX(), registerX | registerY);
        return 1;
      case OP_8XY2:
        registerX = memoryManagementUnit.readRegister(opCode.getX());
        registerY = memoryManagementUnit.readRegister(opCode.getY());
        memoryManagementUnit.writeRegister(opCode.getX(), registerX & registerY);
        return 1;
      case OP_8XY3:
        registerX = memoryManagementUnit.readRegister(opCode.getX());
        registerY = memoryManagementUnit.readRegister(opCode.getY());
        memoryManagementUnit.writeRegister(opCode.getX(), registerX ^ registerY);
        return 1;
      case OP_8XY4:
        registerX = memoryManagementUnit.readRegister(opCode.getX());
        registerY = memoryManagementUnit.readRegister(opCode.getY());
        if (registerX + registerY > 0xFF) {
          memoryManagementUnit.writeRegister(0xF, 1);
        } else {
          memoryManagementUnit.writeRegister(0xF, 0);
        }
        memoryManagementUnit.writeRegister(opCode.getX(), (registerX + registerY) & 0xFF);
        return 1;
      case OP_8XY5:
        registerX = memoryManagementUnit.readRegister(opCode.getX());
        registerY = memoryManagementUnit.readRegister(opCode.getY());
        if (registerY > registerX) {
          memoryManagementUnit.writeRegister(0xF, 0);
        } else {
          memoryManagementUnit.writeRegister(0xF, 1);
        }
        memoryManagementUnit.writeRegister(opCode.getX(), (registerX - registerY) & 0xFF);
        return 1;
      case OP_8XY6:
        registerX = memoryManagementUnit.readRegister(opCode.getX());
        memoryManagementUnit.writeRegister(0xF, registerX & 0x1);
        memoryManagementUnit.writeRegister(opCode.getX(), registerX >> 1);
        return 1;
      case OP_8XY7:
        registerX = memoryManagementUnit.readRegister(opCode.getX());
        registerY = memoryManagementUnit.readRegister(opCode.getY());
        if (registerX > registerY) {
          memoryManagementUnit.writeRegister(0xF, 0);
        } else {
          memoryManagementUnit.writeRegister(0xF, 1);
        }
        memoryManagementUnit.writeRegister(opCode.getX(), (registerY - registerX) & 0xFF);
        return 1;
      case OP_8XYE:
        registerX = memoryManagementUnit.readRegister(opCode.getX());
        memoryManagementUnit.writeRegister(0xF, (registerX & 0x80) >> 7);
        memoryManagementUnit.writeRegister(opCode.getX(), (registerX << 1) & 0xFF);
        return 1;
      case OP_9XY0:
        registerX = memoryManagementUnit.readRegister(opCode.getX());
        registerY = memoryManagementUnit.readRegister(opCode.getY());
        return registerX != registerY ? 2 : 1;
      case OP_ANNN:
        indexRegister = opCode.getN();
        return 1;
      case OP_BNNN:
        programCounter = opCode.getN() + memoryManagementUnit.readRegister(0);
        return 0;
      case OP_CXNN:
        int rand = random.nextInt(Byte.MAX_VALUE) & opCode.getN();
        memoryManagementUnit.writeRegister(opCode.getX(), rand);
        return 1;
      case OP_DXYN:
        boolean unsetFlag = false;
        for (int y = 0; y < opCode.getN(); y++) {
          int sprite = memoryManagementUnit.readMemory(indexRegister + y);
          boolean[] spriteRow = new boolean[8];
          for (int x = 0; x < 8; x++) {
            spriteRow[x] = ((sprite >> 7 - x) & 0x1) == 1;
          }
          unsetFlag |= memoryManagementUnit.writeGraphics(opCode.getX() + ((opCode.getY() + y) * SCREEN_WIDTH), spriteRow);
        }
        int flag = unsetFlag ? 1 : 0;
        memoryManagementUnit.writeRegister(0xF, flag);
        return 1;
      case OP_EX9E:
        registerX = memoryManagementUnit.readRegister(opCode.getX());
        return memoryManagementUnit.readKeypad(registerX) ? 2 : 1;
      case OP_EXA1:
        registerX = memoryManagementUnit.readRegister(opCode.getX());
        return memoryManagementUnit.readKeypad(registerX) ? 1 : 2;
      case OP_FX07:
        memoryManagementUnit.writeRegister(opCode.getX(), delayTimer);
        return 1;
      case OP_FX0A:
        for (int i = 0; i < KEYPAD; i++) {
          if (memoryManagementUnit.readKeypad(i)) {
            memoryManagementUnit.writeRegister(opCode.getX(), i);
            return 1;
          }
        }
        return 0;
      case OP_FX15:
        delayTimer = memoryManagementUnit.readRegister(opCode.getX());
        return 1;
      case OP_FX18:
        soundTimer = memoryManagementUnit.readRegister(opCode.getX());
        return 1;
      case OP_FX1E:
        indexRegister += memoryManagementUnit.readRegister(opCode.getX());
        return 1;
      case OP_FX29:
        indexRegister = FONT_OFFSET + (memoryManagementUnit.readRegister(opCode.getX()) * 5);
        return 1;
      case OP_FX33:
        registerX = memoryManagementUnit.readRegister(opCode.getX());
        memoryManagementUnit.writeMemory(indexRegister, registerX / 100);
        memoryManagementUnit.writeMemory(indexRegister + 1, (registerX / 10) % 10);
        memoryManagementUnit.writeMemory(indexRegister + 2, (registerX % 100) % 10);
        return 1;
      case OP_FX55:
        registerX = opCode.getX();
        for (int i = 0; i <= registerX; i++) {
          int registerValue = memoryManagementUnit.readRegister(i);
          memoryManagementUnit.writeMemory(indexRegister + i, registerValue);
        }
        return 1;
      case OP_FX65:
        registerX = opCode.getX();
        for (int i = 0; i <= registerX; i++) {
          int memoryValue = memoryManagementUnit.readMemory(indexRegister + i);
          memoryManagementUnit.writeRegister(i, memoryValue);
        }
        return 1;
      default:
        throw new UnsupportedOperationException(opCode.getOpCodeType());
    }
  }

  private void loadFontSet() {
    int index = 0;
    for (Font f : Font.values()) {
      memoryManagementUnit.writeMemory(FONT_OFFSET + (index * 5), f.getPixels());
      index++;
    }
  }

}
