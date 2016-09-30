package com.github.nhirakawa.emulator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class MemoryManagementUnitTest {

  private static final int MEMORY = 2;
  private static final int REGISTERS = 2;
  private static final int STACK = 2;
  private static final int WIDTH = 2;
  private static final int HEIGHT = 2;
  private static final int KEYPAD = 2;

  private MemoryManagementUnit mmu;

  @Before
  public void setup() {
    mmu = new MemoryManagementUnit(MEMORY, REGISTERS, STACK, WIDTH, HEIGHT, KEYPAD);
  }

  @Test
  public void itReadsAndWritesMemory() {
    mmu.writeMemory(0, 1);
    assertThat(mmu.readMemory(0)).isEqualTo(1);
    assertThat(mmu.readMemory(1)).isEqualTo(0);

    mmu.writeMemory(0, 100);
    assertThat(mmu.readMemory(0)).isEqualTo(100);
    assertThat(mmu.readMemory(1)).isEqualTo(0);
  }

  @Test
  public void itReadsAndWritesMemoryBlocks() {
    mmu.writeMemory(0, new int[] { 100, 200 });
    assertThat(mmu.readMemory(0)).isEqualTo(100);
    assertThat(mmu.readMemory(1)).isEqualTo(200);

    mmu.writeMemory(0, new int[] { 1000, 2000 });
    assertThat(mmu.readMemory(0)).isEqualTo(1000);
    assertThat(mmu.readMemory(1)).isEqualTo(2000);
  }

  @Test
  public void itReadsAndWritesRegisters() {
    mmu.writeRegister(0, 1);
    assertThat(mmu.readRegister(0)).isEqualTo(1);
    assertThat(mmu.readRegister(1)).isEqualTo(0);

    mmu.writeRegister(0, 100);
    assertThat(mmu.readRegister(0)).isEqualTo(100);
    assertThat(mmu.readRegister(1)).isEqualTo(0);
  }

  @Test
  public void itReadsAndWritesStack() {
    mmu.writeStack(0, 1);
    assertThat(mmu.readStack(0)).isEqualTo(1);
    assertThat(mmu.readStack(1)).isEqualTo(0);

    mmu.writeStack(0, 100);
    assertThat(mmu.readStack(0)).isEqualTo(100);
    assertThat(mmu.readStack(1)).isEqualTo(0);
  }

  @Test
  public void itReadsAndWritesGraphics() {
    boolean unset = mmu.writeGraphics(0, true);
    assertThat(mmu.readGraphics(0)).isTrue();
    assertThat(mmu.readGraphics(1)).isFalse();
    assertThat(unset).isFalse();

    unset = mmu.writeGraphics(0, false);
    assertThat(mmu.readGraphics(0)).isFalse();
    assertThat(mmu.readGraphics(0)).isFalse();
    assertThat(unset).isTrue();
  }

  @Test
  public void itClearsGraphics() {
    mmu.writeGraphics(0, true);
    mmu.writeGraphics(0, true);
    mmu.clearGraphics();
    assertThat(mmu.readGraphics(0)).isFalse();
    assertThat(mmu.readGraphics(1)).isFalse();
  }

  @Test
  public void itReadsAndWritesKeypad() {
    mmu.writeKeypad(0, true);
    assertThat(mmu.readKeypad(0)).isTrue();
    assertThat(mmu.readKeypad(1)).isFalse();

    mmu.writeKeypad(0, false);
    assertThat(mmu.readKeypad(0)).isFalse();
    assertThat(mmu.readKeypad(1)).isFalse();
  }
}
