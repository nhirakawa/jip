package com.github.nhirakawa.emulator;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

  @Inject
  public JipEmulator(MemoryManagementUnit memoryManagementUnit) {
    this.memoryManagementUnit = memoryManagementUnit;
    this.indexRegister = 0;
    this.programCounter = 0;
    this.stackPointer = 0;
    this.soundTimer = 0;
    this.delayTimer = 0;
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
  }

  public void step() {
    OpCode opcode = fetchOpCode();
    programCounter += 2;
    LOG.debug("{}", opcode);
    executeOpCode(opcode);
  }

  int getProgramCounter() {
    return programCounter;
  }

  int getStackPointer() {
    return stackPointer;
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

  private OpCode fetchOpCode() {
    int op = memoryManagementUnit.readMemory(ROM_OFFSET + programCounter) << 8 | memoryManagementUnit.readMemory(ROM_OFFSET + programCounter + 1);
    OpCodeType opType = OpCodeType.of(op);
    return OpCode.builder()
        .setOpCodeType(opType)
        .setX(opType.getX(op))
        .setY(opType.getY(op))
        .setN(opType.getN(op))
        .build();
  }

  private void executeOpCode(OpCode opcode) {
    LOG.debug("executing {}", opcode);
    final int memoryX;
    final int memoryY;
    final int registerX;
    final int registerY;
    switch (opcode.getOpCodeType()) {
      case OP_6XNN:
        memoryManagementUnit.writeRegister(opcode.getX(), opcode.getN());
        break;
      case OP_7XNN:
        registerX = memoryManagementUnit.readRegister(opcode.getX());
        memoryManagementUnit.writeRegister(opcode.getX(), registerX + opcode.getN());
        break;
      case OP_8XY0:
        registerY = memoryManagementUnit.readRegister(opcode.getY());
        memoryManagementUnit.writeRegister(opcode.getX(), registerY);
        break;
      case OP_8XY1:
        registerX = memoryManagementUnit.readRegister(opcode.getX());
        registerY = memoryManagementUnit.readRegister(opcode.getY());
        memoryManagementUnit.writeRegister(opcode.getX(), registerX | registerY);
        break;
      case OP_8XY2:
        registerX = memoryManagementUnit.readRegister(opcode.getX());
        registerY = memoryManagementUnit.readRegister(opcode.getY());
        memoryManagementUnit.writeRegister(opcode.getX(), registerX & registerY);
        break;
      case OP_8XY3:
        registerX = memoryManagementUnit.readRegister(opcode.getX());
        registerY = memoryManagementUnit.readRegister(opcode.getY());
        memoryManagementUnit.writeRegister(opcode.getX(), registerX ^ registerY);
        break;
      case OP_FX07:
        memoryManagementUnit.writeRegister(opcode.getX(), delayTimer);
        break;
      case OP_FX15:
        delayTimer = memoryManagementUnit.readRegister(opcode.getX());
        break;
      case OP_FX18:
        soundTimer = memoryManagementUnit.readRegister(opcode.getX());
        break;
      case OP_FX1E:
        indexRegister += memoryManagementUnit.readRegister(opcode.getX());
        break;
      default:
        throw new UnsupportedOperationException(opcode.getOpCodeType());
    }
  }

  public static class UnsupportedOperationException extends RuntimeException {

    public UnsupportedOperationException(OpCodeType opCode) {
      super(String.format("%s is not supported", opCode));
    }
  }

}
