package dev.codenmore.tilegame.entities;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

import dev.codenmore.tilegame.Handler;
import dev.codenmore.tilegame.entities.creatures.Player;

public class EntityManager {
	
	private Handler handler;
	private Player player;
	private ArrayList<Entity> entities;
	private Comparator<Entity> renderSorter = (a, b) -> {
        int ay = (int) (a.getY() + a.getHeight());
        int by = (int) (b.getY() + b.getHeight());
        // Handle the case where ay and by are equal
        return Integer.compare(ay, by);
    };


	public EntityManager(Handler handler, Player player) {
		this.handler = handler;
		this.player = player;
		entities = new ArrayList<>();
		addEntity(player);
	}
	
	public void tick() {
		Iterator<Entity> it = entities.iterator();
        while (it.hasNext()) {
			Entity e = it.next();
            e.tick();
            if (!e.isActive()) {
				it.remove();
			}
        }
		entities.sort(renderSorter);
	}
	
	public void render(Graphics g) {
		for(Entity e : entities) {
			if (e.isVisible())
				e.render(g);
		}
		player.postRender(g);
	}
	
	public void addEntity(Entity e) {
		entities.add(e);
		
	}
	
	//GETTERS SETTERS

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

	public void setEntities(ArrayList<Entity> entities) {
		this.entities = entities;
	}

}
