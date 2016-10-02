package com.github.nhirakawa;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.apache.commons.io.IOUtils;

import com.github.nhirakawa.models.OpCodeType;

public class JipUtils {

  private JipUtils() {
  }

  public static int[] readRom(File file) throws IOException {
    return readRom(file.toURI());
  }

  public static int[] readRom(URI uri) throws IOException {
    byte[] bytes = IOUtils.toByteArray(uri);
    int[] translated = new int[bytes.length];
    for (int i = 0; i < bytes.length; i++) {
      translated[i] = bytes[i] & 0xFF;
    }
    return translated;
  }

  public static OpCodeType getOpCode(int op1, int op2) {
    return OpCodeType.of((op1 << 8) | op2);
  }
}
