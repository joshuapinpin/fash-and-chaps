package main;

import java.awt.event.KeyEvent;
import java.util.Map;



class Controller extends Keys{
  Controller(Camera c, Sword s, Map<String, Integer> keyBindings){
    setAction(keyBindings.getOrDefault("up", KeyEvent.VK_W), c.set(Direction::up), c.set(Direction::unUp));
    setAction(keyBindings.getOrDefault("down", KeyEvent.VK_S), c.set(Direction::down), c.set(Direction::unDown));
    setAction(keyBindings.getOrDefault("left", KeyEvent.VK_A), c.set(Direction::left), c.set(Direction::unLeft));
    setAction(keyBindings.getOrDefault("right", KeyEvent.VK_D), c.set(Direction::right), c.set(Direction::unRight));
    setAction(keyBindings.getOrDefault("swordLeft", KeyEvent.VK_O), s.set(Direction::left), s.set(Direction::unLeft));
    setAction(keyBindings.getOrDefault("swordRight", KeyEvent.VK_P), s.set(Direction::right), s.set(Direction::unRight));
  }
}