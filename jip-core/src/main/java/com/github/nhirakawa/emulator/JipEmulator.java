package com.github.nhirakawa.emulator;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.nhirakawa.models.Font;
import com.github.nhirakawa.models.OpCode;
import com.github.nhirakawa.models.OpCodeType;
import com.google.inject.Inject;

public class JipEmulator {

  private static final Logger LOG = LogManager.getLogger(JipEmulator.class);

  private static final int FONT_OFFSET = 0x050;
  private static final int ROM_OFFSET = 0x200;

  private int indexRegister;
  private int programCounter;
  private int stackPointer;

  private int soundTimer;
  private int delayTimer;

  private final MemoryManagementUnit memoryManagementUnit;
  private final Random random;

  @Inject
  public JipEmulator(MemoryManagementUnit memoryManagementUnit,
                     Random random) {
    this.memoryManagementUnit = memoryManagementUnit;
    this.random = random;
    this.indexRegister = 0;
    this.programCounter = 0;
    this.stackPointer = 0;
    this.soundTimer = 0;
    this.delayTimer = 0;
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
    OpCode opcode = fetchOpCode();
    LOG.debug("{}", opcode);
    int instructionsToAdvance = executeOpCode(opcode);
    programCounter += (instructionsToAdvance * 2);
  }

  private OpCode fetchOpCode() {
    int op = memoryManagementUnit.readMemory(programCounter) << 8 | memoryManagementUnit.readMemory(programCounter + 1);
    OpCodeType opType = OpCodeType.of(op);
    return OpCode.builder()
        .setOpCodeType(opType)
        .setX(opType.getX(op))
        .setY(opType.getY(op))
        .setN(opType.getN(op))
        .build();
  }

  private int executeOpCode(OpCode opcode) {
    LOG.debug("executing {}", opcode);
    final int registerX;
    final int registerY;
    switch (opcode.getOpCodeType()) {
      case OP_00E0:
        memoryManagementUnit.clearGraphics();
        return 1;
      case OP_00EE:
        stackPointer--;
        programCounter = memoryManagementUnit.readStack(stackPointer);
        memoryManagementUnit.writeStack(stackPointer, 0);
        return 1;
      case OP_1NNN:
        programCounter = opcode.getN();
        return 0;
      case OP_2NNN:
        memoryManagementUnit.writeStack(stackPointer, programCounter);
        stackPointer++;
        programCounter = opcode.getN();
        return 0;
      case OP_3XNN:
        registerX = memoryManagementUnit.readRegister(opcode.getX());
        return registerX == opcode.getN() ? 2 : 1;
      case OP_4XNN:
        registerX = memoryManagementUnit.readRegister(opcode.getX());
        return registerX != opcode.getN() ? 2 : 1;
      case OP_5XY0:
        registerX = memoryManagementUnit.readRegister(opcode.getX());
        registerY = memoryManagementUnit.readRegister(opcode.getY());
        return registerX == registerY ? 2 : 1;
      case OP_6XNN:
        memoryManagementUnit.writeRegister(opcode.getX(), opcode.getN());
        return 1;
      case OP_7XNN:
        registerX = memoryManagementUnit.readRegister(opcode.getX());
        memoryManagementUnit.writeRegister(opcode.getX(), registerX + opcode.getN());
        return 1;
      case OP_8XY0:
        registerY = memoryManagementUnit.readRegister(opcode.getY());
        memoryManagementUnit.writeRegister(opcode.getX(), registerY);
        return 1;
      case OP_8XY1:
        registerX = memoryManagementUnit.readRegister(opcode.getX());
        registerY = memoryManagementUnit.readRegister(opcode.getY());
        memoryManagementUnit.writeRegister(opcode.getX(), registerX | registerY);
        return 1;
      case OP_8XY2:
        registerX = memoryManagementUnit.readRegister(opcode.getX());
        registerY = memoryManagementUnit.readRegister(opcode.getY());
        memoryManagementUnit.writeRegister(opcode.getX(), registerX & registerY);
        return 1;
      case OP_8XY3:
        registerX = memoryManagementUnit.readRegister(opcode.getX());
        registerY = memoryManagementUnit.readRegister(opcode.getY());
        memoryManagementUnit.writeRegister(opcode.getX(), registerX ^ registerY);
        return 1;
      case OP_8XY4:
        registerX = memoryManagementUnit.readRegister(opcode.getX());
        registerY = memoryManagementUnit.readRegister(opcode.getY());
        if (registerX + registerY > 0xFF) {
          memoryManagementUnit.writeRegister(0xF, 1);
        } else {
          memoryManagementUnit.writeRegister(0xF, 0);
        }
        memoryManagementUnit.writeRegister(opcode.getX(), (registerX + registerY) & 0xFF);
        return 1;
      case OP_8XY5:
        registerX = memoryManagementUnit.readRegister(opcode.getX());
        registerY = memoryManagementUnit.readRegister(opcode.getY());
        if (registerY > registerX) {
          memoryManagementUnit.writeRegister(0xF, 0);
        } else {
          memoryManagementUnit.writeRegister(0xF, 1);
        }
        memoryManagementUnit.writeRegister(opcode.getX(), (registerX - registerY) & 0xFF);
        return 1;
      case OP_8XY6:
        registerX = memoryManagementUnit.readRegister(opcode.getX());
        memoryManagementUnit.writeRegister(0xF, registerX & 0x1);
        memoryManagementUnit.writeRegister(opcode.getX(), registerX >> 1);
        return 1;
      case OP_8XY7:
        registerX = memoryManagementUnit.readRegister(opcode.getX());
        registerY = memoryManagementUnit.readRegister(opcode.getY());
        if (registerX > registerY) {
          memoryManagementUnit.writeRegister(0xF, 0);
        } else {
          memoryManagementUnit.writeRegister(0xF, 1);
        }
        memoryManagementUnit.writeRegister(opcode.getX(), (registerY - registerX) & 0xFF);
        return 1;
      case OP_8XYE:
        registerX = memoryManagementUnit.readRegister(opcode.getX());
        memoryManagementUnit.writeRegister(0xF, (registerX & 0x80) >> 7);
        memoryManagementUnit.writeRegister(opcode.getX(), (registerX << 1) & 0xFF);
        return 1;
      case OP_9XY0:
        registerX = memoryManagementUnit.readRegister(opcode.getX());
        registerY = memoryManagementUnit.readRegister(opcode.getY());
        return registerX != registerY ? 2 : 1;
      case OP_ANNN:
        indexRegister = opcode.getN();
        return 1;
      case OP_CXNN:
        int rand = random.nextInt(Byte.MAX_VALUE) & opcode.getN();
        memoryManagementUnit.writeRegister(opcode.getX(), rand);
        return 1;
      case OP_EX9E:
        registerX = memoryManagementUnit.readRegister(opcode.getX());
        return memoryManagementUnit.readKeypad(registerX) ? 2 : 1;
      case OP_EXA1:
        registerX = memoryManagementUnit.readRegister(opcode.getX());
        return memoryManagementUnit.readKeypad(registerX) ? 1 : 2;
      case OP_FX07:
        memoryManagementUnit.writeRegister(opcode.getX(), delayTimer);
        return 1;
      case OP_FX0A:
        registerX = opcode.getX();
        return memoryManagementUnit.readKeypad(registerX) ? 1 : 0;
      case OP_FX15:
        delayTimer = memoryManagementUnit.readRegister(opcode.getX());
        return 1;
      case OP_FX18:
        soundTimer = memoryManagementUnit.readRegister(opcode.getX());
        return 1;
      case OP_FX1E:
        indexRegister += memoryManagementUnit.readRegister(opcode.getX());
        return 1;
      case OP_FX29:
        indexRegister = FONT_OFFSET + (memoryManagementUnit.readRegister(opcode.getX()) * 5);
        return 1;
      case OP_FX55:
        registerX = opcode.getX();
        for (int i = 0; i <= registerX; i++) {
          int registerValue = memoryManagementUnit.readRegister(i);
          memoryManagementUnit.writeMemory(indexRegister + i, registerValue);
        }
        return 1;
      case OP_FX65:
        registerX = opcode.getX();
        for (int i = 0; i <= registerX; i++) {
          int memoryValue = memoryManagementUnit.readMemory(indexRegister + i);
          memoryManagementUnit.writeRegister(i, memoryValue);
        }
        return 1;
      default:
        throw new UnsupportedOperationException(opcode.getOpCodeType());
    }
  }

  private void loadFontSet() {
    int index = 0;
    for (Font f : Font.values()) {
      memoryManagementUnit.writeMemory(FONT_OFFSET + (index * 5), f.getPixels());
      index++;
    }
  }

  public static class UnsupportedOperationException extends RuntimeException {

    public UnsupportedOperationException(OpCodeType opCode) {
      super(String.format("%s is not supported", opCode));
    }
  }

}
