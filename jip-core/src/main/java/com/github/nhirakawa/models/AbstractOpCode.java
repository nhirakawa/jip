package com.github.nhirakawa.models;

import org.immutables.value.Value;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;

@Value.Immutable
@JipStyle
public abstract class AbstractOpCode {

  public abstract OpCodeType getOpCodeType();
  public abstract int getX();
  public abstract int getY();
  public abstract int getN();

  @Override
  public String toString() {
    ToStringHelper helper = MoreObjects.toStringHelper(OpCode.class)
        .add("OpCodeType", getOpCodeType());
    if (getX() != Integer.MIN_VALUE) {
      helper.add("x", getX());
    }
    if (getY() != Integer.MIN_VALUE) {
      helper.add("y", getY());
    }
    if (getN() != Integer.MIN_VALUE) {
      helper.add("n", getN());
    }
    return helper.toString();
  }
}
