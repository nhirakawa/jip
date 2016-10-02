package com.github.nhirakawa.jip.swing;

import java.awt.*;

import javax.swing.*;

import com.github.nhirakawa.jip.config.JipUiModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;

public class JipFrame extends JFrame {

  private final int MULTIPLIER = 10;

  private final JPanel drawArea;

  @Inject
  public JipFrame(@Named("screen.width") int width,
                  @Named("screen.height") int height) {
    super("Jip");
    this.drawArea = new JPanel();
    drawArea.setMinimumSize(new Dimension(width * MULTIPLIER, height * MULTIPLIER));

    add(drawArea);
    setSize(width * MULTIPLIER, height * MULTIPLIER);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setResizable(false);
    setVisible(true);

  }

  public static void main(String... args) {
    Injector injector = Guice.createInjector(new JipUiModule());
    injector.getInstance(JipFrame.class);
  }
}
