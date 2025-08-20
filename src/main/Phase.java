package main;

import java.util.List;
import java.util.stream.Stream;

record Phase(Model model, Controller controller){ 
	static Phase levelTemplate(Runnable next, Runnable first, List<Entity> monsters, java.util.Map<String, Integer> keyBindings) {
		Camera c= new Camera(new Point(5, 5));
		Sword s= new Sword(c);
		Cells cells= new Cells();
		var m= new Model(){
			List<Entity> entities= Stream.concat(monsters.stream(), Stream.of(c,s)).toList();
			@Override
			public Camera camera(){ return c; }
			@Override
			public List<Entity> entities(){ return entities; }
			@Override
			public void remove(Entity e){  
				entities= entities.stream()
					.filter(ei->!ei.equals(e))
					.toList();
			}
			@Override
			public Cells cells(){ return cells; }
			@Override
			public void onGameOver(){ first.run(); }
			@Override
			public void onNextLevel(){ next.run(); }
		};
		return new Phase(m, new Controller(c, s, keyBindings));    
	}
	
	static Phase level1(Runnable next, Runnable first, java.util.Map<String, Integer> keyBindings) {
		return levelTemplate(next, first, List.of(new Monster(new Point(0, 0))), keyBindings);
	}
	static Phase level2(Runnable next, Runnable first, java.util.Map<String, Integer> keyBindings) {
		return levelTemplate(next, first, List.of(
			new Monster(new Point(0, 0)),
			new Monster(new Point(16, 16)),
			new RoamingMonster(new Point(0, 16)),
			new Monster(new Point(16, 0))
		), keyBindings);
	}
	static Phase level3(Runnable next, Runnable first, java.util.Map<String, Integer> keyBindings) {
		Monster m = new Monster(new Point(0,0));
		Sword s = new Sword(m) {
			@Override public double distance(){ return 1.5d; }
			@Override public double speed(){ return 0.4d; }
		};
		s.set(Direction::left).run();
		return levelTemplate(next, first, List.of(s,m), keyBindings);
	}
}