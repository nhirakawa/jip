package com.github.nhirakawa.models;

import org.immutables.value.Value;

@Value.Immutable
@JipStyle
public interface OpCodeIF {

  OpCodeType getOpCodeType();
  int getX();
  int getY();
  int getN();
}
