package main;

import java.awt.Dimension;
import java.awt.Graphics;
import imgs.Img;


class Monster implements Entity{
  private Point location;
  private MonsterState monsterState = new AwakeState();
  
  Monster(Point location){ this.location= location; }
  
  public Point location(){ return location; }
  public void location(Point p){ location = p; }
  public double speed(){ return 0.05d; }
  
  public void ping(Model m){monsterState.ping(m);}
  public void draw(Graphics g, Point center, Dimension size) {
  	monsterState.draw(g, center, size);
  }
 
  public double chaseTarget(Monster outer, Point target){
    var arrow= target.distance(outer.location());
    double size= arrow.size();
    arrow = arrow.times(speed() / size);
    outer.location(outer.location().add(arrow));
    return size;
  }
  
  private interface MonsterState{
  	void ping(Model m);
  	void draw(Graphics g, Point center, Dimension size);
  }
  
  private class AwakeState implements MonsterState{

		@Override
		public void ping(Model m) {
			var arrow= m.camera().location().distance(location);
	    double size= arrow.size();
	    arrow = arrow.times(speed() / size);
	    location = location.add(arrow); 
	    if (size < 0.6d){ m.onGameOver(); }
		}

		@Override
		public void draw(Graphics g, Point center, Dimension size) {
			drawImg(Img.AwakeMonster.image, g, center, size);
		}
  }
  private class SleepState implements MonsterState{

		@Override
		public void ping(Model m) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void draw(Graphics g, Point center, Dimension size) {
			// TODO Auto-generated method stub
			
		}
  }
  private class DeadState implements MonsterState{

		@Override
		public void ping(Model m) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void draw(Graphics g, Point center, Dimension size) {
			// TODO Auto-generated method stub
			
		}
  	
  }
}