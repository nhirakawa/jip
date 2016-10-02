package com.github.nhirakawa.jip.swing;

import java.awt.*;

import javax.swing.*;

public class JipDrawPanel extends JPanel {

  private final int width;
  private final int height;
  private final int multiplier;
  private boolean[] graphicsBuffer;

  public JipDrawPanel(int width,
                      int height,
                      int multiplier) {
    this.width = width;
    this.height = height;
    this.multiplier = multiplier;
    this.graphicsBuffer = new boolean[width * height];
  }

  public void setGraphicsBuffer(boolean[] graphicsBuffer) {
    this.graphicsBuffer = graphicsBuffer;
    invalidate();
    repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    for (int i = 0; i < width * height; i++) {
      if (graphicsBuffer[i]) {
        g.setColor(Color.WHITE);
      } else {
        g.setColor(Color.BLACK);
      }
      int x = (i / width) * multiplier;
      int y = (i % width) * multiplier;
      g.fillRect(x, y, multiplier, multiplier);
    }
  }
}
