package com.github.nhirakawa.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

import com.github.nhirakawa.exceptions.UnknownOperationException;

public class OpCodeTest {

  @Test
  public void itParses0x00E0() {
    assertThat(OpCode.of(0x00E0)).isEqualTo(OpCode.OP_00E0);
  }

  @Test
  public void itParses0x00EE() {
    assertThat(OpCode.of(0x00EE)).isEqualTo(OpCode.OP_00EE);
  }

  @Test
  public void itParses0x1NNN(){
    assertThat(OpCode.of(0x1000)).isEqualTo(OpCode.OP_1NNN);
    assertThat(OpCode.of(0x1FFF)).isEqualTo(OpCode.OP_1NNN);
  }

  @Test
  public void itParses0x1BNNN(){
    assertThat(OpCode.of(0xBFFF)).isEqualTo(OpCode.OP_BNNN);
    assertThat(OpCode.of(0xB000)).isEqualTo(OpCode.OP_BNNN);
  }

  @Test
  public void itThrowsUnknownOperationException() {
    assertThatThrownBy(() -> OpCode.of(-1)).isInstanceOf(UnknownOperationException.class);
  }
}
