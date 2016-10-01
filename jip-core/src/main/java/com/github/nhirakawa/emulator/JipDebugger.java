package com.github.nhirakawa.emulator;

import javax.swing.*;

import com.github.nhirakawa.config.JipCoreModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class JipDebugger extends JFrame implements Runnable {

  private final JipEmulator emulator;
  private final JPanel panel;
  private final JPanel textPanel;
  private final JTextArea textArea;
  private final JPanel currentInstructionPanel;
  private final JTextField programCounter;
  private final JTextField instructionRegister;
  private final JPanel controlPanel;

  @Inject
  public JipDebugger(JipEmulator emulator) {
    super("JipDebugger");
    this.emulator = emulator;
    panel = new JPanel(true);
    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

    textArea = new JTextArea(10, 50);
    textPanel = getMemoryPanel();

    controlPanel = getControlPanel();

    currentInstructionPanel = new JPanel();
    currentInstructionPanel.setBorder(BorderFactory.createTitledBorder("memory"));
    programCounter = new JTextField();
    programCounter.setEditable(false);
    programCounter.setText("pc");
    instructionRegister = new JTextField();
    instructionRegister.setEditable(false);
    instructionRegister.setText("isr");
    currentInstructionPanel.add(programCounter);
    currentInstructionPanel.add(instructionRegister);

    panel.add(textPanel);
    panel.add(currentInstructionPanel);
    panel.add(controlPanel);

    setContentPane(panel);
    pack();
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setVisible(true);
  }

  @Override
  public void run() {

  }

  private JPanel getMemoryPanel() {
    JPanel textPanel = new JPanel();
    textPanel.setBorder(BorderFactory.createTitledBorder("memory"));
    textArea.setEditable(false);
    textArea.append("asdf");
    textPanel.add(textArea);
    return textPanel;
  }

  private JPanel getCurrentInstructionPanel() {
    JPanel controlPanel = new JPanel();
    return controlPanel;
  }

  private JPanel getControlPanel() {
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBorder(BorderFactory.createTitledBorder("controls"));
    JButton next = new JButton("next");
    next.addActionListener(e -> emulator.step());
    buttonPanel.add(next);
    return buttonPanel;
  }

  public static void main(String... args) {
    Injector injector = Guice.createInjector(new JipCoreModule());
    JipDebugger debugger = injector.getInstance(JipDebugger.class);
    debugger.run();
  }
}
