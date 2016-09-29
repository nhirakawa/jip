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
    emulator.loadRom(new byte[] { 0x65, 0x07 });
    emulator.step();
    assertThat(mmu.readRegister(0x5)).isEqualTo(7);
  }

  @Test
  public void itExecutes7XNN() {
    emulator.loadRom(new byte[] { 0x7A, 0x4F });
    emulator.step();
    assertThat(mmu.readRegister(0xA)).isEqualTo(0x4F);
  }

  @Test
  public void itThrowsUnsupportedOperationException() {
    emulator.loadRom(new byte[] { 0x50, 0x0F });
    assertThatThrownBy(() -> emulator.step()).isInstanceOf(UnsupportedOperationException.class);
  }

  private static int translateAddress(int a) {
    return ROM_OFFSET + a;
  }
}
