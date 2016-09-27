package com.github.nhirakawa.models;

import org.immutables.value.Value;
import org.immutables.value.Value.Immutable;

@Immutable
@JipStyle
public abstract class AbstractOperation {

  public abstract OperationType getOperationType();

  @Value.Default
  public int getX() {
    return Integer.MIN_VALUE;
  }

  @Value.Default
  public int getY() {
    return Integer.MIN_VALUE;
  }

  @Value.Default
  public int getN() {
    return Integer.MIN_VALUE;
  }
}
