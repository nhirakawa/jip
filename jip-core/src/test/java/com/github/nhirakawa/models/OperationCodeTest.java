package com.github.nhirakawa.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

import com.github.nhirakawa.exceptions.UnknownOperationException;

public class OperationCodeTest {

  @Test
  public void testKnownOperationCode() {
    assertThat(OperationCode.fromInteger(0x00E0)).isEqualTo(OperationCode.OP00E0);
  }

  @Test
  public void testUnkownOperationCode() {
    assertThatThrownBy(() -> OperationCode.fromInteger(-1)).isInstanceOf(UnknownOperationException.class);
  }
}
