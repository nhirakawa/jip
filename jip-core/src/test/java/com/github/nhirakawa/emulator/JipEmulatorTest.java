package com.github.nhirakawa.emulator;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class JipEmulatorTest {

  private static final int ROM_OFFSET = 0x200;

  private MemoryManagementUnit mmu;
  private Random random;
  private JipEmulator emulator;

  @Before
  public void setup() {
    mmu = new MemoryManagementUnit(4096, 16, 16, 16, 16, 16);
    random = new Random(100);
    emulator = new JipEmulator(mmu, random);
  }

  @Test
  public void itExecutes3XNN() {
    emulator.setProgramCounter(0);
    mmu.writeRegister(0, 0xA);
    emulator.loadRom(ints(0x30, 0x0A));
    emulator.step();
    assertThat(emulator.getProgramCounter()).isEqualTo(4);

    emulator.setProgramCounter(0);
    mmu.writeRegister(0, 0xA);
    emulator.loadRom(ints(0x30, 0x0B));
    emulator.step();
    assertThat(emulator.getProgramCounter()).isEqualTo(2);

  }

  @Test
  public void itExecutes4XNN() {
    emulator.setProgramCounter(0);
    mmu.writeRegister(0, 0xD);
    emulator.loadRom(ints(0x40, 0x0E));
    emulator.step();
    assertThat(emulator.getProgramCounter()).isEqualTo(4);

    emulator.setProgramCounter(0);
    mmu.writeRegister(0, 0xE);
    emulator.loadRom(ints(0x40, 0x0E));
    emulator.step();
    assertThat(emulator.getProgramCounter()).isEqualTo(2);
  }

  @Test
  public void itExecutes5XY0() {
    emulator.setProgramCounter(0);
    mmu.writeRegister(0, 0xD);
    mmu.writeRegister(1, 0xD);
    emulator.loadRom(ints(0x50, 0x10));
    emulator.step();
    assertThat(emulator.getProgramCounter()).isEqualTo(4);

    emulator.setProgramCounter(0);
    mmu.writeRegister(0, 0xD);
    mmu.writeRegister(1, 0xF);
    emulator.loadRom(ints(0x50, 0x10));
    emulator.step();
    assertThat(emulator.getProgramCounter()).isEqualTo(2);
  }

  @Test
  public void itExecutes6XNN() {
    emulator.loadRom(ints(0x65, 0x07));
    emulator.step();
    assertThat(mmu.readRegister(0x5)).isEqualTo(7);
  }

  @Test
  public void itExecutes7XNN() {
    emulator.loadRom(ints(0x7A, 0x4F));
    emulator.step();
    assertThat(mmu.readRegister(0xA)).isEqualTo(0x4F);
  }

  @Test
  public void itExecutes8XY0() {
    mmu.writeRegister(0xA, 0xB);
    emulator.loadRom(ints(0x80, 0xA0));
    emulator.step();
    assertThat(mmu.readRegister(0)).isEqualTo(0xB);
  }

  @Test
  public void itExecutes8XY1() {
    mmu.writeRegister(0, 0xC);
    mmu.writeRegister(1, 0xB);
    emulator.loadRom(ints(0x80, 0x11));
    emulator.step();
    assertThat(mmu.readRegister(0x0)).isEqualTo(0xB | 0xC);
  }

  @Test
  public void itExecutes8XY2() {
    mmu.writeRegister(0, 0x7);
    mmu.writeRegister(1, 0x2);
    emulator.loadRom(ints(0x81, 0x02));
    emulator.step();
    assertThat(mmu.readRegister(0x1)).isEqualTo(0x7 & 0x2);
  }

  @Test
  public void itExecutes8XY3() {
    mmu.writeRegister(0, 1);
    mmu.writeRegister(1, 2);
    emulator.loadRom(ints(0x80, 0x13));
    emulator.step();
    assertThat(mmu.readRegister(0x0)).isEqualTo(1 ^ 2);
  }

  @Test
  public void itExecutesANNN() {
    emulator.setIndexRegister(5);
    emulator.loadRom(ints(0xAA, 0xEC));
    emulator.step();
    assertThat(emulator.getIndexRegister()).isEqualTo(0xAEC);
  }

  @Test
  public void itExecutesCXNN() {
    mmu.writeRegister(0, 10);
    emulator.loadRom(ints(0xC0, 0xA0));
    emulator.step();
    assertThat(mmu.readRegister(0)).isEqualTo(31 & 0xA0);
  }

  @Test
  public void itExecutesFX07() {
    emulator.setDelayTimer(7);
    mmu.writeRegister(1, 4);
    emulator.loadRom(ints(0xF1, 0x07));
    emulator.step();
    assertThat(mmu.readRegister(1)).isEqualTo(7);
  }

  @Test
  public void itExecutesFX15() {
    emulator.setDelayTimer(9);
    mmu.writeRegister(0xA, 0xA);
    emulator.loadRom(ints(0xFA, 0x15));
    emulator.step();
    assertThat(emulator.getDelayTimer()).isEqualTo(0xA);
  }

  @Test
  public void itExecutesFX18() {
    emulator.setSoundTimer(1);
    mmu.writeRegister(0, 6);
    emulator.loadRom(ints(0xF0, 0x18));
    emulator.step();
    assertThat(emulator.getSoundTimer()).isEqualTo(6);
  }

  @Test
  public void itExecutesFX1E() {
    emulator.setIndexRegister(1);
    mmu.writeRegister(0, 5);
    emulator.loadRom(ints(0xF0, 0x1E));
    emulator.step();
    assertThat(emulator.getIndexRegister()).isEqualTo(6);
  }

  private static int translateAddress(int a) {
    return ROM_OFFSET + a;
  }

  private static int[] ints(int... ints) {
    return ints;
  }

}
