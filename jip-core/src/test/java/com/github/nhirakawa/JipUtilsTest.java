package com.github.nhirakawa;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.github.nhirakawa.models.OpCodeType;

public class JipUtilsTest {

  @Test
  public void itGetsOpCodeType() {
    int op1 = 0x00;
    int op2 = 0xE0;
    assertThat(JipUtils.getOpCode(op1, op2)).isEqualTo(OpCodeType.OP_00E0);

    op1 = 0x91;
    op2 = 0xB0;
    assertThat(JipUtils.getOpCode(op1, op2)).isEqualTo(OpCodeType.OP_9XY0);
  }
}
