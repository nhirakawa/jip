package com.github.nhirakawa.jip.swing;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

import com.github.nhirakawa.emulator.JipEmulator;
import com.github.nhirakawa.jip.config.JipUiModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;

public class JipFrame extends JFrame {

  private final int MULTIPLIER = 20;

  private final JipEmulator emulator;
  private final JipDrawPanel drawArea;

  @Inject
  public JipFrame(JipEmulator emulator,
                  @Named("screen.width") int width,
                  @Named("screen.height") int height) {
    super("Jip");
    this.emulator = emulator;
    this.drawArea = new JipDrawPanel(width, height, MULTIPLIER);
    drawArea.setMinimumSize(new Dimension(width * MULTIPLIER, height * MULTIPLIER));

    add(drawArea);
    setSize(width * MULTIPLIER, height * MULTIPLIER);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//    setResizable(false);
    setVisible(true);

  }

  public void run(File rom) {
    try {
      emulator.loadRom(rom);
      while (true) {
        emulator.step();
        drawArea.setGraphicsBuffer(emulator.getGraphics());
      }
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  public static void main(String... args) {
    Injector injector = Guice.createInjector(new JipUiModule());
    injector.getInstance(JipFrame.class).run(new File("PONG.rom"));
  }
}
