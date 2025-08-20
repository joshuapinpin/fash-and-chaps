package main;

import java.util.List;
import java.util.stream.Stream;

record Phase(Model model, Controller controller){ 
	static Phase levelTemplate(Runnable next, Runnable first, List<Entity> monsters) {
		Camera c= new Camera(new Point(5, 5));
	  Sword s= new Sword(c);
	  Cells cells= new Cells();
	  var m= new Model(){
	    List<Entity> entities= Stream.concat(monsters.stream(), Stream.of(c,s)).toList();
	    public Camera camera(){ return c; }
	    public List<Entity> entities(){ return entities; }
	    public void remove(Entity e){ 
	      entities= entities.stream()
	        .filter(ei->!ei.equals(e))
	        .toList();
	    }
	    public Cells cells(){ return cells; }
	    public void onGameOver(){ first.run(); }
	    public void onNextLevel(){ next.run(); }
	  };
	  return new Phase(m, new Controller(c, s));    
	}
	
  static Phase level1(Runnable next, Runnable first) {
    return levelTemplate(next, first, List.of(new Monster(new Point(0, 0))));
  }
  static Phase level2(Runnable next, Runnable first) {
  	return levelTemplate(next, first, List.of(
			new Monster(new Point(0, 0)),
	  	new Monster(new Point(15, 15)),
			new Monster(new Point(0, 15)),
			new Monster(new Point(15, 0))
  	));
  }
  static Phase level3(Runnable next, Runnable first) {
  	return levelTemplate(next, first, List.of(new Monster(new Point(0,0))));
  }
}