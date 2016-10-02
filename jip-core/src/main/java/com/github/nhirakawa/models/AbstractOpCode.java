package com.github.nhirakawa.models;

import org.immutables.value.Value;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;

@Value.Immutable
@JipStyle
public abstract class AbstractOpCode {

  public abstract int getOpCode();

  @Value.Lazy
  public OpCodeType getOpCodeType() {
    return OpCodeType.of(getOpCode());
  }

  @Value.Lazy
  public int getX() {
    return getOpCodeType().getX(getOpCode());
  }

  @Value.Lazy
  public int getY() {
    return getOpCodeType().getY(getOpCode());
  }

  @Value.Lazy
  public int getN() {
    return getOpCodeType().getN(getOpCode());
  }

  @Value.Lazy
  public String describe() {
    return getOpCodeType().describe(getOpCode());
  }

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
