package com.github.nhirakawa.emulator;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.*;

import com.github.nhirakawa.JipUtils;
import com.github.nhirakawa.config.JipCoreModule;
import com.google.common.io.Resources;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class JipDebugger extends JFrame implements Runnable {

  private final JipEmulator emulator;
  private final JPanel panel;
  private final JPanel textPanel;
  private final JTextArea memory;
  private final JTextArea registers;
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

    memory = new JTextArea(16, 10);
    registers = new JTextArea(16, 10);
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
    try {
      emulator.loadRom(Resources.getResource("MAZE.rom").toURI());
      loadMemory();
    } catch (IOException | URISyntaxException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private void emulateCycle() {

  }

  private void loadMemory() {
    memory.setText("");
    int[] memory = emulator.getMemory();
    for (int i = 0x200; i < memory.length; i++) {
      this.memory.append(String.format("0x%s - %s%n", JipUtils.toHexString(i), JipUtils.toHexString(memory[i])));
    }

    registers.setText("");
    int[] registers = emulator.getRegisters();
    for (int i = 0; i < registers.length; i++) {
      this.registers.append(String.format("V%X - %s%n", i, JipUtils.toHexString(registers[i])));
    }
  }

  private JPanel getMemoryPanel() {
    JPanel memoryPanel = new JPanel();

    JPanel memoryTextPanel = new JPanel();
    memoryTextPanel.setBorder(BorderFactory.createTitledBorder("memory"));
    memory.setEditable(false);
    JScrollPane memoryScrollPane = new JScrollPane(memory);
    memoryScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    memoryScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    memoryTextPanel.add(memoryScrollPane);

    JPanel registerTextPanel = new JPanel();
    registerTextPanel.setBorder(BorderFactory.createTitledBorder("registers"));
    JScrollPane registersScrollPane = new JScrollPane(registers);
    registersScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    registersScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    registerTextPanel.add(registersScrollPane);

    memoryPanel.add(memoryTextPanel);
    memoryPanel.add(registerTextPanel);
    return memoryPanel;
  }

  private JPanel getCurrentInstructionPanel() {
    JPanel controlPanel = new JPanel();
    return controlPanel;
  }

  private JPanel getControlPanel() {
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBorder(BorderFactory.createTitledBorder("controls"));
    JButton next = new JButton("next");
    next.addActionListener(e -> {
      emulator.step();
      loadMemory();
    });
    buttonPanel.add(next);
    return buttonPanel;
  }

  public static void main(String... args) {
    Injector injector = Guice.createInjector(new JipCoreModule());
    JipDebugger debugger = injector.getInstance(JipDebugger.class);
    debugger.run();
  }
}
