package com.github.nhirakawa.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

import com.github.nhirakawa.exceptions.UnknownOperationException;

public class OpCodeTypeTest {

  @Test
  public void itParses0x00E0() {
    int op = 0x00E0;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_00E0);
    assertThat(opcode.getX(op)).isEqualTo(0);
    assertThat(opcode.getY(op)).isEqualTo(0);
    assertThat(opcode.getN(op)).isEqualTo(0);
  }

  @Test
  public void itParses00EE() {
    int op = 0x00EE;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_00EE);
    assertThat(opcode.getX(op)).isEqualTo(0);
    assertThat(opcode.getY(op)).isEqualTo(0);
    assertThat(opcode.getN(op)).isEqualTo(0);
  }

  @Test
  public void itParses1NNN() {
    int op = 0x1000;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_1NNN);
    assertThat(opcode.getX(op)).isEqualTo(0);
    assertThat(opcode.getY(op)).isEqualTo(0);
    assertThat(opcode.getN(op)).isEqualTo(0);

    op = 0x1FFF;
    assertThat(opcode).isEqualTo(OpCodeType.OP_1NNN);
    assertThat(opcode.getX(op)).isEqualTo(0);
    assertThat(opcode.getY(op)).isEqualTo(0);
    assertThat(opcode.getN(op)).isEqualTo(0xFFF);
  }

  @Test
  public void itParses1BNNN() {
    int op = 0xBFFF;
    OpCodeType opcode = OpCodeType.of(0xBFFF);
    assertThat(opcode).isEqualTo(OpCodeType.OP_BNNN);
    assertThat(opcode.getX(op)).isEqualTo(0);
    assertThat(opcode.getY(op)).isEqualTo(0);
    assertThat(opcode.getN(op)).isEqualTo(0xFFF);

    op = 0xB000;
    opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_BNNN);
    assertThat(opcode.getX(op)).isEqualTo(0);
    assertThat(opcode.getY(op)).isEqualTo(0);
    assertThat(opcode.getN(op)).isEqualTo(0);
  }

  @Test
  public void itParses6XNN() {
    int op = 0x6FAA;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_6XNN);
    assertThat(opcode.getX(op)).isEqualTo(0xF);
    assertThat(opcode.getY(op)).isEqualTo(0x0);
    assertThat(opcode.getN(op)).isEqualTo(0xAA);
  }

  @Test
  public void itThrowsUnknownOperationException() {
    assertThatThrownBy(() -> OpCodeType.of(-1)).isInstanceOf(UnknownOperationException.class);
  }
}
