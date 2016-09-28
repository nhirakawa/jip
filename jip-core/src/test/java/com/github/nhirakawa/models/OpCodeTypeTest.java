package com.github.nhirakawa.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

import com.github.nhirakawa.exceptions.UnknownOperationException;

public class OpCodeTypeTest {

  @Test
  public void itParses0x00E0() {
    assertThat(OpCodeType.of(0x00E0)).isEqualTo(OpCodeType.OP_00E0);
  }

  @Test
  public void itParses0x00EE() {
    assertThat(OpCodeType.of(0x00EE)).isEqualTo(OpCodeType.OP_00EE);
  }

  @Test
  public void itParses0x1NNN(){
    assertThat(OpCodeType.of(0x1000)).isEqualTo(OpCodeType.OP_1NNN);
    assertThat(OpCodeType.of(0x1FFF)).isEqualTo(OpCodeType.OP_1NNN);
  }

  @Test
  public void itParses0x1BNNN(){
    assertThat(OpCodeType.of(0xBFFF)).isEqualTo(OpCodeType.OP_BNNN);
    assertThat(OpCodeType.of(0xB000)).isEqualTo(OpCodeType.OP_BNNN);
  }

  @Test
  public void itThrowsUnknownOperationException() {
    assertThatThrownBy(() -> OpCodeType.of(-1)).isInstanceOf(UnknownOperationException.class);
  }
}
