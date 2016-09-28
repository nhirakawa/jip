package com.github.nhirakawa.models;

import java.util.Optional;

import org.immutables.value.Value.Immutable;

@Immutable
@JipStyle
public abstract class AbstractOperation {

  public abstract OperationType getOperationType();
  public abstract Optional<Integer> getX();
  public abstract Optional<Integer> getY();
  public abstract Optional<Integer> getN();
  public abstract Optional<MemoryType> getSource();
  public abstract Optional<MemoryType> getTarget();
  public abstract Optional<Operator> getOperator();
}
