package com.github.nhirakawa.models;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.github.nhirakawa.exceptions.UnknownOperationException;

public class OperationCodeTest {

  @Test
  public void testKnownOperationCode() {
    assertThat(OperationCode.fromInteger(0x00E0)).isEqualTo(OperationCode.OP00E0);
  }

  @Test(expected = UnknownOperationException.class)
  public void testUnkownOperationCode() {
    OperationCode.fromInteger(-1);
  }
}
