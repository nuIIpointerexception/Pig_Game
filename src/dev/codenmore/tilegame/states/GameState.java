package dev.codenmore.tilegame.states;

import dev.codenmore.tilegame.Handler;
import dev.codenmore.tilegame.world.World;

import java.awt.*;

public class GameState extends State{
	
	
	private World world;
	
	
	public GameState(Handler handler) {
		super(handler);
		// TODO: Saving and loading worlds
		world = new World(handler, "test".hashCode());
		handler.setWorld(world);
	}
		
	@Override
	public void tick() {
		world.tick();
		
	}

	@Override
	public void render(Graphics g) {
		world.render(g);
		
	}
	
	

}
