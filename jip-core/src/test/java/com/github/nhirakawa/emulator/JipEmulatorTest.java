package com.github.nhirakawa.emulator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Before;
import org.junit.Test;

import com.github.nhirakawa.emulator.JipEmulator.UnsupportedOperationException;

public class JipEmulatorTest {

  private static final int ROM_OFFSET = 0x200;

  private MemoryManagementUnit mmu;
  private JipEmulator emulator;

  @Before
  public void setup() {
    mmu = new MemoryManagementUnit(4096, 16, 16, 16, 16, 16);
    emulator = new JipEmulator(mmu);
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
  public void itThrowsUnsupportedOperationException() {
    emulator.loadRom(new byte[] { 0x50, 0x0F });
    assertThatThrownBy(() -> emulator.step()).isInstanceOf(UnsupportedOperationException.class);
  }

  private static int translateAddress(int a) {
    return ROM_OFFSET + a;
  }

  private static int[] ints(int... ints) {
    return ints;
  }

}
