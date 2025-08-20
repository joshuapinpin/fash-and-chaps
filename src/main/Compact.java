package main;

// ...existing code...

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

class Compact extends JFrame{
  private static final long serialVersionUID= 1L;
  Runnable closePhase= ()->{};
  Map<String, Integer> keyBindings = defaultKeyBindings();

  // Default key bindings
  private static Map<String, Integer> defaultKeyBindings() {
    Map<String, Integer> map = new HashMap<>();
    map.put("up", java.awt.event.KeyEvent.VK_W);
    map.put("down", java.awt.event.KeyEvent.VK_S);
    map.put("left", java.awt.event.KeyEvent.VK_A);
    map.put("right", java.awt.event.KeyEvent.VK_D);
    map.put("swordLeft", java.awt.event.KeyEvent.VK_O);
    map.put("swordRight", java.awt.event.KeyEvent.VK_P);
    return map;
  }
  Compact(){
    assert SwingUtilities.isEventDispatchThread();
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    phaseZero();
    setVisible(true);
    addWindowListener(new WindowAdapter(){
  @Override
  public void windowClosed(WindowEvent e){ closePhase.run(); }
    });
  }
  private void phaseZero() {
    var welcome = new JLabel("Welcome to Compact. A compact Java game!");
    var start = new JButton("Start!");
    var panel = new javax.swing.JPanel(new GridLayout(7, 2));
    panel.add(new JLabel("Up:"));
    var upField = new JTextField("W");
    panel.add(upField);
    panel.add(new JLabel("Down:"));
    var downField = new JTextField("S");
    panel.add(downField);
    panel.add(new JLabel("Left:"));
    var leftField = new JTextField("A");
    panel.add(leftField);
    panel.add(new JLabel("Right:"));
    var rightField = new JTextField("D");
    panel.add(rightField);
    panel.add(new JLabel("Sword Left:"));
    var swordLeftField = new JTextField("O");
    panel.add(swordLeftField);
    panel.add(new JLabel("Sword Right:"));
    var swordRightField = new JTextField("P");
    panel.add(swordRightField);
    closePhase.run();
    closePhase = ()->{
      remove(welcome);
      remove(panel);
      remove(start);
    };
    add(BorderLayout.NORTH, welcome);
    add(BorderLayout.CENTER, panel);
    add(BorderLayout.SOUTH, start);
    start.addActionListener(e->{
      // Validate and store key bindings
      keyBindings.put("up", charToVK(upField.getText()));
      keyBindings.put("down", charToVK(downField.getText()));
      keyBindings.put("left", charToVK(leftField.getText()));
      keyBindings.put("right", charToVK(rightField.getText()));
      keyBindings.put("swordLeft", charToVK(swordLeftField.getText()));
      keyBindings.put("swordRight", charToVK(swordRightField.getText()));
      phaseOne();
    });
    setPreferredSize(new Dimension(800, 400));
    pack();
  }

  // Convert single character to VK code
  private int charToVK(String s) {
    if (s == null || s.length() == 0) return -1;
    char c = Character.toUpperCase(s.charAt(0));
    if (c >= 'A' && c <= 'Z') return java.awt.event.KeyEvent.VK_A + (c - 'A');
    if (c >= '0' && c <= '9') return java.awt.event.KeyEvent.VK_0 + (c - '0');
    return -1;
  }
  
  private void phaseOne(){
    setPhase(Phase.level1(() -> phaseTwo(), () -> phaseZero(), keyBindings));
  }
  private void phaseTwo() {
    setPhase(Phase.level2(() -> phaseThree(), () -> phaseZero(), keyBindings));
  }
  private void phaseThree() {
    setPhase(Phase.level3(() -> victoryPhase(), () -> phaseZero(), keyBindings));
  }
  
  private void victoryPhase() {
  	throw new Error("Victory Phase not yet completed ");
  }
  
  
  
  
  void setPhase(Phase p){
    //set up the viewport and the timer
    Viewport v= new Viewport(p.model());
    v.addKeyListener(p.controller());
    v.setFocusable(true);
    Timer timer= new Timer(34, unused->{
      assert SwingUtilities.isEventDispatchThread();
      p.model().ping();
      v.repaint();
    });
    closePhase.run();//close phase before adding any element of the new phase
    closePhase = ()->{ timer.stop(); remove(v); };
    add(BorderLayout.CENTER, v);//add the new phase viewport
    setPreferredSize(getSize());//to keep the current size
    pack();                     //after pack
    v.requestFocus();//need to be after pack
    timer.start();
  }
}