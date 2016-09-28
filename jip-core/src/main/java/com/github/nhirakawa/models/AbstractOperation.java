package com.github.nhirakawa.models;

import org.immutables.value.Value.Immutable;

@Immutable
@JipStyle
public abstract class AbstractOperation {

  public abstract OperationType getOperationType();
  public abstract int getX();
  public abstract int getY();
  public abstract int getN();
}
