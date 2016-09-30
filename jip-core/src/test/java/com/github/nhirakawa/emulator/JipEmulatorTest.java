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
  public void itExecutes00E0() {
    mmu.writeGraphics(0, true);
    mmu.writeGraphics(1, true);
    emulator.loadRom(ints(0x00, 0xE0));
    emulator.step();
    assertThat(mmu.readGraphics(0)).isFalse();
    assertThat(mmu.readGraphics(1)).isFalse();
  }

  @Test
  public void itExecutes00EE() {
    mmu.writeStack(0, 6);
    emulator.loadRom(ints(0x00, 0xEE));
    emulator.setStackPointer(1);
    emulator.step();
    assertThat(emulator.getStackPointer()).isEqualTo(0);
    assertThat(mmu.readStack(0)).isEqualTo(0);
    assertThat(emulator.getProgramCounter()).isEqualTo(8);
  }

  @Test
  public void itExecutes1NNN() {
    emulator.loadRom(ints(0x1F, 0xE9));
    emulator.step();
    assertThat(emulator.getProgramCounter()).isEqualTo(0xFE9);
  }

  @Test
  public void itExecutes2NNN() {
    mmu.writeStack(0, -1);
    emulator.loadRom(ints(0x2B, 0xED));
    emulator.step();
    assertThat(emulator.getProgramCounter()).isEqualTo(0xBED);
    assertThat(mmu.readStack(0)).isEqualTo(512);
  }

  @Test
  public void itExecutes3XNN() {
    mmu.writeRegister(0, 0xA);
    emulator.loadRom(ints(0x30, 0x0A));
    emulator.step();
    assertThat(emulator.getProgramCounter()).isEqualTo(516);

    mmu.writeRegister(0, 0xA);
    emulator.loadRom(ints(0x30, 0x0B));
    emulator.step();
    assertThat(emulator.getProgramCounter()).isEqualTo(514);

  }

  @Test
  public void itExecutes4XNN() {
    mmu.writeRegister(0, 0xD);
    emulator.loadRom(ints(0x40, 0x0E));
    emulator.step();
    assertThat(emulator.getProgramCounter()).isEqualTo(516);

    mmu.writeRegister(0, 0xE);
    emulator.loadRom(ints(0x40, 0x0E));
    emulator.step();
    assertThat(emulator.getProgramCounter()).isEqualTo(514);
  }

  @Test
  public void itExecutes5XY0() {
    mmu.writeRegister(0, 0xD);
    mmu.writeRegister(1, 0xD);
    emulator.loadRom(ints(0x50, 0x10));
    emulator.step();
    assertThat(emulator.getProgramCounter()).isEqualTo(516);

    mmu.writeRegister(0, 0xD);
    mmu.writeRegister(1, 0xF);
    emulator.loadRom(ints(0x50, 0x10));
    emulator.step();
    assertThat(emulator.getProgramCounter()).isEqualTo(514);
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
  public void itExecutes8XY4() {
    mmu.writeRegister(0x0, 1);
    mmu.writeRegister(0x1, 2);
    emulator.loadRom(ints(0x80, 0x14));
    emulator.step();
    assertThat(mmu.readRegister(0x0)).isEqualTo(1 + 2);
    assertThat(mmu.readRegister(0xF)).isEqualTo(0);

    mmu.writeRegister(0x0, 250);
    mmu.writeRegister(0x1, 100);
    emulator.loadRom(ints(0x80, 0x14));
    emulator.step();
    assertThat(mmu.readRegister(0x0)).isEqualTo((250 + 100) & 0xFF);
    assertThat(mmu.readRegister(0xF)).isEqualTo(1);
  }

  @Test
  public void itExecutes8XY5() {
    mmu.writeRegister(0x0, 4);
    mmu.writeRegister(0x1, 2);
    emulator.loadRom(ints(0x80, 0x15));
    emulator.step();
    assertThat(mmu.readRegister(0x0)).isEqualTo(2);
    assertThat(mmu.readRegister(0xF)).isEqualTo(1);

    mmu.writeRegister(0x0, 2);
    mmu.writeRegister(0x1, 4);
    emulator.loadRom(ints(0x80, 0x15));
    emulator.step();
    assertThat(mmu.readRegister(0x0)).isEqualTo(254);
    assertThat(mmu.readRegister(0xF)).isEqualTo(0);
  }

  @Test
  public void itExecutes8XY6() {
    mmu.writeRegister(0x0, 3);
    emulator.loadRom(ints(0x80, 0xF6));
    emulator.step();
    assertThat(mmu.readRegister(0)).isEqualTo(1);
    assertThat(mmu.readRegister(0xF)).isEqualTo(1);
  }

  @Test
  public void itExecutes8XY7() {
    mmu.writeRegister(0x0, 1);
    mmu.writeRegister(0x1, 2);
    emulator.loadRom(ints(0x80, 0x17));
    emulator.step();
    assertThat(mmu.readRegister(0x0)).isEqualTo(1);
    assertThat(mmu.readRegister(0xF)).isEqualTo(1);

    mmu.writeRegister(0x0, 2);
    mmu.writeRegister(0x1, 1);
    emulator.loadRom(ints(0x80, 0x17));
    emulator.step();
    assertThat(mmu.readRegister(0x0)).isEqualTo(255);
    assertThat(mmu.readRegister(0xF)).isEqualTo(0);
  }

  @Test
  public void itExecutes8XYE() {
    mmu.writeRegister(0x0, 0x81);
    emulator.loadRom(ints(0x80, 0x0E));
    emulator.step();
    assertThat(mmu.readRegister(0x0)).isEqualTo(2);
    assertThat(mmu.readRegister(0xF)).isEqualTo(0x1);
  }

  @Test
  public void itExecutes9XY0() {
    mmu.writeRegister(0, 0x3);
    mmu.writeRegister(1, 0xE);
    emulator.loadRom(ints(0x90, 0x10));
    emulator.step();
    assertThat(emulator.getProgramCounter()).isEqualTo(516);

    mmu.writeRegister(0, 0x3);
    mmu.writeRegister(1, 0x3);
    emulator.loadRom(ints(0x90, 0x10));
    emulator.step();
    assertThat(emulator.getProgramCounter()).isEqualTo(514);
  }

  @Test
  public void itExecutesANNN() {
    emulator.setIndexRegister(5);
    emulator.loadRom(ints(0xAA, 0xEC));
    emulator.step();
    assertThat(emulator.getIndexRegister()).isEqualTo(0xAEC);
  }

  @Test
  public void itExecutesBNNN() {
    mmu.writeRegister(0x0, 0xA);
    emulator.loadRom(ints(0xBA, 0xF0));
    emulator.step();
    assertThat(emulator.getProgramCounter()).isEqualTo(0xAF0 + 0xA);
  }

  @Test
  public void itExecutesCXNN() {
    mmu.writeRegister(0, 10);
    emulator.loadRom(ints(0xC0, 0xA0));
    emulator.step();
    assertThat(mmu.readRegister(0)).isEqualTo(31 & 0xA0);
  }

  @Test
  public void itExecutesEX9E() {
    mmu.writeKeypad(0, true);
    emulator.loadRom(ints(0xE0, 0x9E));
    emulator.step();
    assertThat(emulator.getProgramCounter()).isEqualTo(516);

    emulator.setIndexRegister(0);

    mmu.writeKeypad(0, false);
    emulator.loadRom(ints(0xE0, 0x9E));
    emulator.step();
    assertThat(emulator.getProgramCounter()).isEqualTo(514);
  }

  @Test
  public void itExecutesEXA1() {
    mmu.writeKeypad(0, false);
    emulator.loadRom(ints(0xE0, 0xA1));
    emulator.step();
    assertThat(emulator.getProgramCounter()).isEqualTo(516);

    emulator.setIndexRegister(0);

    mmu.writeKeypad(0, true);
    emulator.loadRom(ints(0xE0, 0xA1));
    emulator.step();
    assertThat(emulator.getProgramCounter()).isEqualTo(514);
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
  public void itExecutesFX0A() {
    mmu.writeKeypad(0, false);
    emulator.loadRom(ints(0xF0, 0x0A));
    emulator.step();
    assertThat(emulator.getProgramCounter()).isEqualTo(512);

    emulator.step();
    assertThat(emulator.getProgramCounter()).isEqualTo(512);

    mmu.writeKeypad(0, true);
    emulator.step();
    assertThat(emulator.getProgramCounter()).isEqualTo(514);
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

  @Test
  public void itExecutesFX29() {
    mmu.writeRegister(0, 0xA);
    emulator.loadRom(ints(0xF0, 0x29));
    emulator.step();
    assertThat(emulator.getIndexRegister()).isEqualTo(0x050 + (0xA * 5));
  }

  @Test
  public void itExecutesFX33() {
    mmu.writeRegister(0xA, 214);
    emulator.setIndexRegister(700);
    emulator.loadRom(ints(0xFA, 0x33));
    emulator.step();
    assertThat(mmu.readMemory(700)).isEqualTo(2);
    assertThat(mmu.readMemory(701)).isEqualTo(1);
    assertThat(mmu.readMemory(702)).isEqualTo(4);
  }

  @Test
  public void itExecutesFX55() {
    emulator.setIndexRegister(2);
    for (int i = 0; i < 5; i++) {
      mmu.writeRegister(i, i);
    }
    emulator.loadRom(ints(0xF4, 0x55));
    emulator.step();
    for (int i = 0; i < 5; i++) {
      assertThat(mmu.readMemory(i + 2)).isEqualTo(i);
    }
  }

  @Test
  public void itExecutesFX65() {
    emulator.setIndexRegister(2);
    for (int i = 0; i < 5; i++) {
      mmu.writeMemory(2 + i, i);
    }
    emulator.loadRom(ints(0xF4, 0x65));
    emulator.step();
    for (int i = 0; i < 5; i++) {
      assertThat(mmu.readRegister(i)).isEqualTo(i);
    }
  }

  private static int[] ints(int... ints) {
    return ints;
  }

}
