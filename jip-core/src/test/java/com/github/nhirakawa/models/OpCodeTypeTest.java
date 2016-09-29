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
  public void itParses2NNN() {
    int op = 0x2000;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_2NNN);
    assertThat(opcode.getX(op)).isEqualTo(0);
    assertThat(opcode.getY(op)).isEqualTo(0);
    assertThat(opcode.getN(op)).isEqualTo(0x000);

    op = 0x2AAA;
    opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_2NNN);
    assertThat(opcode.getX(op)).isEqualTo(0);
    assertThat(opcode.getY(op)).isEqualTo(0);
    assertThat(opcode.getN(op)).isEqualTo(0xAAA);
  }

  @Test
  public void itParses3XNN() {
    int op = 0x3BCC;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_3XNN);
    assertThat(opcode.getX(op)).isEqualTo(0xB);
    assertThat(opcode.getY(op)).isEqualTo(0);
    assertThat(opcode.getN(op)).isEqualTo(0xCC);
  }

  @Test
  public void itParses4XNN() {
    int op = 0x4ADD;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_4XNN);
    assertThat(opcode.getX(op)).isEqualTo(0xA);
    assertThat(opcode.getY(op)).isEqualTo(0);
    assertThat(opcode.getN(op)).isEqualTo(0xDD);
  }

  @Test
  public void itParses5XY0() {
    int op = 0x5AE0;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_5XY0);
    assertThat(opcode.getX(op)).isEqualTo(0xA);
    assertThat(opcode.getY(op)).isEqualTo(0xE);
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
  public void itParses7XNN() {
    int op = 0x7FA4;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_7XNN);
    assertThat(opcode.getX(op)).isEqualTo(0xF);
    assertThat(opcode.getY(op)).isEqualTo(0);
    assertThat(opcode.getN(op)).isEqualTo(0xA4);
  }

  @Test
  public void itParses8XY0() {
    int op = 0x82D0;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_8XY0);
    assertThat(opcode.getX(op)).isEqualTo(0x2);
    assertThat(opcode.getY(op)).isEqualTo(0xD);
    assertThat(opcode.getN(op)).isEqualTo(0);
  }

  @Test
  public void itParses8XY1() {
    int op = 0x8121;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_8XY1);
    assertThat(opcode.getX(op)).isEqualTo(0x1);
    assertThat(opcode.getY(op)).isEqualTo(0x2);
    assertThat(opcode.getN(op)).isEqualTo(0);
  }

  @Test
  public void itParses8XY2() {
    int op = 0x8412;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_8XY2);
    assertThat(opcode.getX(op)).isEqualTo(0x4);
    assertThat(opcode.getY(op)).isEqualTo(0x1);
    assertThat(opcode.getN(op)).isEqualTo(0);
  }

  @Test
  public void itParses8XY3() {
    int op = 0x89E3;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_8XY3);
    assertThat(opcode.getX(op)).isEqualTo(0x9);
    assertThat(opcode.getY(op)).isEqualTo(0xE);
    assertThat(opcode.getN(op)).isEqualTo(0);
  }

  @Test
  public void itParses8XY4() {
    int op = 0x80A4;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_8XY4);
    assertThat(opcode.getX(op)).isEqualTo(0x0);
    assertThat(opcode.getY(op)).isEqualTo(0xA);
    assertThat(opcode.getN(op)).isEqualTo(0);
  }

  @Test
  public void itParses8XY5() {
    int op = 0x8B75;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_8XY5);
    assertThat(opcode.getX(op)).isEqualTo(0xB);
    assertThat(opcode.getY(op)).isEqualTo(0x7);
    assertThat(opcode.getN(op)).isEqualTo(0);
  }

  @Test
  public void itParses8XY6() {
    int op = 0x86F6;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_8XY6);
    assertThat(opcode.getX(op)).isEqualTo(0x6);
    assertThat(opcode.getY(op)).isEqualTo(0xF);
    assertThat(opcode.getN(op)).isEqualTo(0);
  }

  @Test
  public void itParses8XY7() {
    int op = 0x8AE7;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_8XY7);
    assertThat(opcode.getX(op)).isEqualTo(0xA);
    assertThat(opcode.getY(op)).isEqualTo(0xE);
    assertThat(opcode.getN(op)).isEqualTo(0);
  }

  @Test
  public void itParses8XYE() {
    int op = 0x8EFE;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_8XYE);
    assertThat(opcode.getX(op)).isEqualTo(0xE);
    assertThat(opcode.getY(op)).isEqualTo(0xF);
    assertThat(opcode.getN(op)).isEqualTo(0);
  }

  @Test
  public void itParses9XY0() {
    int op = 0x9010;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_9XY0);
    assertThat(opcode.getX(op)).isEqualTo(0x0);
    assertThat(opcode.getY(op)).isEqualTo(0x1);
    assertThat(opcode.getN(op)).isEqualTo(0);
  }

  @Test
  public void itParsesANNN() {
    int op = 0xAFE3;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_ANNN);
    assertThat(opcode.getX(op)).isEqualTo(0);
    assertThat(opcode.getY(op)).isEqualTo(0);
    assertThat(opcode.getN(op)).isEqualTo(0xFE3);
  }

  @Test
  public void itParsesBNNN() {
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
  public void itParsesCXNN() {
    int op = 0xCB9C;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_CXNN);
    assertThat(opcode.getX(op)).isEqualTo(0xB);
    assertThat(opcode.getY(op)).isEqualTo(0);
    assertThat(opcode.getN(op)).isEqualTo(0x9C);
  }

  @Test
  public void itParsesDXYN() {
    int op = 0xD0FA;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_DXYN);
    assertThat(opcode.getX(op)).isEqualTo(0x0);
    assertThat(opcode.getY(op)).isEqualTo(0xF);
    assertThat(opcode.getN(op)).isEqualTo(0xA);
  }

  @Test
  public void itParsesEX9E() {
    int op = 0xE49E;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_EX9E);
    assertThat(opcode.getX(op)).isEqualTo(0x4);
    assertThat(opcode.getY(op)).isEqualTo(0);
    assertThat(opcode.getN(op)).isEqualTo(0);
  }

  @Test
  public void itParsesEXA1() {
    int op = 0xE8A1;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_EXA1);
    assertThat(opcode.getX(op)).isEqualTo(0x8);
    assertThat(opcode.getY(op)).isEqualTo(0);
    assertThat(opcode.getN(op)).isEqualTo(0);
  }

  @Test
  public void itParsesFX07() {
    int op = 0xF207;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_FX07);
    assertThat(opcode.getX(op)).isEqualTo(0x2);
    assertThat(opcode.getY(op)).isEqualTo(0);
    assertThat(opcode.getN(op)).isEqualTo(0);
  }

  @Test
  public void itParsesFX0A() {
    int op = 0xFD0A;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_FX0A);
    assertThat(opcode.getX(op)).isEqualTo(0xD);
    assertThat(opcode.getY(op)).isEqualTo(0);
    assertThat(opcode.getN(op)).isEqualTo(0);
  }

  @Test
  public void itParsesFX15() {
    int op = 0xF115;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_FX15);
    assertThat(opcode.getX(op)).isEqualTo(0x1);
    assertThat(opcode.getY(op)).isEqualTo(0);
    assertThat(opcode.getN(op)).isEqualTo(0);
  }

  @Test
  public void itParsesFX18() {
    int op = 0xF218;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_FX18);
    assertThat(opcode.getX(op)).isEqualTo(0x2);
    assertThat(opcode.getY(op)).isEqualTo(0);
    assertThat(opcode.getN(op)).isEqualTo(0);
  }

  @Test
  public void itParsesFX1E() {
    int op = 0xF61E;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_FX1E);
    assertThat(opcode.getX(op)).isEqualTo(0x6);
    assertThat(opcode.getY(op)).isEqualTo(0);
    assertThat(opcode.getN(op)).isEqualTo(0);
  }

  @Test
  public void itParsesFX29() {
    int op = 0xF729;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_FX29);
    assertThat(opcode.getX(op)).isEqualTo(0x7);
    assertThat(opcode.getY(op)).isEqualTo(0);
    assertThat(opcode.getN(op)).isEqualTo(0);
  }

  @Test
  public void itParsesFX33() {
    int op = 0xFA33;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_FX33);
    assertThat(opcode.getX(op)).isEqualTo(0xA);
    assertThat(opcode.getY(op)).isEqualTo(0);
    assertThat(opcode.getN(op)).isEqualTo(0);
  }

  @Test
  public void itParsesFX55() {
    int op = 0xFB55;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_FX55);
    assertThat(opcode.getX(op)).isEqualTo(0xB);
    assertThat(opcode.getY(op)).isEqualTo(0);
    assertThat(opcode.getN(op)).isEqualTo(0);
  }

  @Test
  public void itParsesFX65() {
    int op = 0xFD65;
    OpCodeType opcode = OpCodeType.of(op);
    assertThat(opcode).isEqualTo(OpCodeType.OP_FX65);
    assertThat(opcode.getX(op)).isEqualTo(0xD);
    assertThat(opcode.getY(op)).isEqualTo(0);
    assertThat(opcode.getN(op)).isEqualTo(0);
  }

  @Test
  public void itThrowsUnknownOperationException() {
    assertThatThrownBy(() -> OpCodeType.of(-1)).isInstanceOf(UnknownOperationException.class);
  }
}
