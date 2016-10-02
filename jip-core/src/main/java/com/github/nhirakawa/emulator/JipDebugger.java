package com.github.nhirakawa.emulator;

import java.io.IOException;
import java.net.URI;

import javax.swing.*;

import com.github.nhirakawa.JipUtils;
import com.github.nhirakawa.models.OpCode;
import com.google.inject.Inject;

public class JipDebugger extends JFrame {

  private final JipEmulator emulator;

  private final JTextArea memory;
  private final JTextArea registers;
  private final JTextArea stack;

  private final JTextField opCode;
  private final JTextField programCounter;
  private final JTextField indexRegister;
  private final JTextField stackPointer;

  @Inject
  public JipDebugger(JipEmulator emulator) {
    super("JipDebugger");

    this.emulator = emulator;
    JPanel panel = new JPanel(true);
    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

    memory = new JTextArea(16, 10);
    registers = new JTextArea(16, 10);
    stack = new JTextArea(16, 10);
    JPanel textPanel = getMemoryPanel();

    opCode = new JTextField();
    programCounter = new JTextField();
    indexRegister = new JTextField();
    stackPointer = new JTextField();
    JPanel currentInstructionPanel = getCurrentInstructionPanel();

    panel.add(textPanel);
    panel.add(currentInstructionPanel);

    setContentPane(panel);
    setResizable(false);
    setLocationRelativeTo(null);
    pack();
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setVisible(true);
  }

  public void debug(URI uri) throws IOException {
    emulator.loadRom(uri);
    loadMemory();
    loadState();
  }

  private void emulateCycle() {
    emulator.step();
    loadMemory();
    loadState();
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

    stack.setText("");
    int[] stack = emulator.getStack();
    for (int i = 0; i < stack.length; i++) {
      this.stack.append(String.format("0x%X - %s%n", i, JipUtils.toHexString(stack[i])));
    }
  }

  private void loadState() {
    OpCode emulatorOpCode = emulator.getOpCode();
    if (emulatorOpCode != null) {
      opCode.setText(String.format("0x%04X", emulatorOpCode.getOpCode()));
    }
    programCounter.setText(String.format("0x%X", emulator.getProgramCounter()));
    indexRegister.setText(String.format("0x%X", emulator.getIndexRegister()));
    stackPointer.setText(String.format("0x%X", emulator.getStackPointer()));
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
    registersScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
    registersScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    registerTextPanel.add(registersScrollPane);

    JPanel stackTextPanel = new JPanel();
    stackTextPanel.setBorder(BorderFactory.createTitledBorder("stack"));
    JScrollPane stackScrollPane = new JScrollPane(stack);
    stackScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
    stackScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    stackTextPanel.add(stackScrollPane);

    memoryPanel.add(memoryTextPanel);
    memoryPanel.add(registerTextPanel);
    memoryPanel.add(stackTextPanel);
    return memoryPanel;
  }

  private JPanel getCurrentInstructionPanel() {
    JPanel controlPanel = new JPanel();
    controlPanel.setBorder(BorderFactory.createTitledBorder("current state"));

    JLabel opCodeLabel = new JLabel("op");
    opCodeLabel.setLabelFor(opCode);
    opCode.setEditable(false);
    opCode.setColumns(6);

    JLabel programCounterLabel = new JLabel("pc");
    programCounterLabel.setLabelFor(programCounter);
    programCounter.setEditable(false);
    programCounter.setColumns(4);

    JLabel indexRegisterLabel = new JLabel("index");
    indexRegisterLabel.setLabelFor(indexRegister);
    indexRegister.setEditable(false);
    indexRegister.setColumns(4);

    JLabel stackPointerLabel = new JLabel("sp");
    stackPointerLabel.setLabelFor(stackPointer);
    stackPointer.setEditable(false);
    stackPointer.setColumns(4);

    JButton next = new JButton("next");
    next.addActionListener(e -> {
      emulator.step();
      loadMemory();
    });

    controlPanel.add(opCodeLabel);
    controlPanel.add(opCode);

    controlPanel.add(programCounterLabel);
    controlPanel.add(programCounter);

    controlPanel.add(indexRegisterLabel);
    controlPanel.add(indexRegister);

    controlPanel.add(stackPointerLabel);
    controlPanel.add(stackPointer);

    controlPanel.add(next);

    return controlPanel;
  }

}
