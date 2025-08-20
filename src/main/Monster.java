package main;

import java.awt.Dimension;
import java.awt.Graphics;
import imgs.Img;


public class Monster implements Entity{
  private Point location;
  protected MonsterState monsterState = new SleepState();
  protected int deadTicks = 0;
  
  public Monster(Point location){ this.location= location; }
  public Monster(Point location, MonsterState monsterState){
  	this.location = location; this.monsterState = monsterState;
  }
  public Point location(){ return location; }
  public void location(Point p){ location = p; }
  public MonsterState monsterState() {return monsterState;}
  public void monsterState(MonsterState state) {monsterState = state;}
  public double speed(){ return 0.05d; }
  public void ping(Model m){monsterState.ping(m, this);}
  public void draw(Graphics g, Point center, Dimension size) {
  	monsterState.draw(g, center, size, this);
  }
  public double chaseTarget(Monster outer, Point target){
    var arrow= target.distance(outer.location());
    double size= arrow.size();
    arrow = arrow.times(speed() / size);
    outer.location(outer.location().add(arrow));
    return size;
  }
  
  public void hitBySword() {
  	if (!(monsterState instanceof DeadState)) {
      monsterState = new DeadState();
      deadTicks = 0;
  	}
  }
  
  protected static interface MonsterState{
  	void ping(Model m, Monster monster);
  	void draw(Graphics g, Point center, Dimension size, Monster monster);
  }
  
  protected static class AwakeState implements MonsterState{
		@Override
		public void ping(Model m, Monster monster) {
			var arrow= m.camera().location().distance(monster.location());
	    double size= arrow.size();
	    arrow = arrow.times(monster.speed() / size);
	    monster.location(monster.location().add(arrow));
//	    location = location.add(arrow);  
	    if (size < 0.6d){ m.onGameOver(); }
	    else if(size > 6.0d) monster.monsterState(new SleepState());
	    
		}
		@Override
		public void draw(Graphics g, Point center, Dimension size, Monster monster) {
			monster.drawImg(Img.AwakeMonster.image, g, center, size);
		}
  }
  
  protected static class SleepState implements MonsterState{
		@Override
		public void ping(Model m, Monster monster) {
			var arrow = m.camera().location().distance(monster.location());
			if(arrow.size() < 6.0d) monster.monsterState(new AwakeState());
		}

		@Override
		public void draw(Graphics g, Point center, Dimension size, Monster monster) {
			monster.drawImg(Img.SleepMonster.image, g, center, size);
		}
  }
  
  protected static class DeadState implements MonsterState{
		public void ping(Model m, Monster monster) {
			monster.deadTicks++;
			if(monster.deadTicks >= 100) m.remove(monster);
		}

		@Override
		public void draw(Graphics g, Point center, Dimension size, Monster monster) {
			monster.drawImg(Img.DeadMonster.image, g, center, size);
		}
  }
  
  protected static class RoamingState implements MonsterState{
  	public int roamingPings = 0;
  	Point randomPoint;
		@Override
		public void ping(Model m, Monster monster) {
			if(roamingPings % 50 == 0) {
				randomPoint = new Point((Math.random()*30)-15, (Math.random()*30)-15);
				System.out.println(randomPoint);
			}
			var arrow = monster.location().distance(randomPoint);
			double size = arrow.size();
			arrow = arrow.times(monster.speed() / Math.max(size, 0.001));
			monster.location(monster.location().add(arrow));
			roamingPings++;
		}

		@Override
		public void draw(Graphics g, Point center, Dimension size, Monster monster) {
			monster.drawImg(Img.AwakeMonster.image, g, center, size);
		}

		
  }
}

class RoamingMonster extends Monster{
	RoamingMonster(Point location){super(location, new RoamingState());}
}