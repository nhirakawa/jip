package com.github.nhirakawa.emulator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;

import com.github.nhirakawa.JipUtils;
import com.github.nhirakawa.models.OpCode;

public class JipDisassembler {

  private final int[] rom;

  public JipDisassembler(File file) throws IOException {
    this(file.toURI());
  }

  public JipDisassembler(URI uri) throws IOException {
    this.rom = JipUtils.readRom(uri);
  }

  public void disassemble(String filename) throws IOException {
    disassemble(new File(filename));
  }

  public void disassemble(File file) throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
    for (int i = 0; i < rom.length; i += 2) {
      OpCode opCode = JipUtils.getOpCode(rom[i], rom[i + 1]);
      writer.write(String.format("%s - %s%n", opCode.getOpCodeType(), opCode.describe()));
    }
    writer.close();
  }

}
