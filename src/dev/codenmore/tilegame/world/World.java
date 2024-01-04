package dev.codenmore.tilegame.world;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import dev.codenmore.tilegame.Handler;
import dev.codenmore.tilegame.entities.EntityManager;
import dev.codenmore.tilegame.entities.creatures.Player;
import dev.codenmore.tilegame.entities.statics.Footstep;
import dev.codenmore.tilegame.entities.statics.Rock;
import dev.codenmore.tilegame.entities.statics.Tree;
import dev.codenmore.tilegame.items.ItemManager;
import dev.codenmore.tilegame.tiles.Tile;
import dev.codenmore.tilegame.utils.Utils;

public class World {
	
	private Handler handler;
	private int width, height; 
	private int spawnX, spawnY;
	private int[][] tiles;
	//Entities
	private EntityManager entityManager;
	private ItemManager itemManager;

	private List<Footstep> footsteps = new ArrayList<>();

	private Random random;

	private long seed = "testseed".hashCode();


	
	public World(Handler handler, String path) {
		this.handler = handler;

		random = new Random(seed);

		itemManager = new ItemManager(handler);

		entityManager = new EntityManager(handler, new Player(handler, 100, 100));
	
		loadWorld(path);
		
		entityManager.getPlayer().setX(spawnX);
		entityManager.getPlayer().setY(spawnY);
		
	}
	
	public void tick() {
		itemManager.tick();
		entityManager.tick();

		Iterator<Footstep> iterator = footsteps.iterator();
		while (iterator.hasNext()) {
			Footstep footstep = iterator.next();
			footstep.tick();
			if (footstep.isDead()) {
				iterator.remove();
			}
		}
	}

	public void render(Graphics g) {
		float zoom = handler.getGameCamera().getZoomFactor();

		int visibleWorldWidth = (int) (handler.getWidth() / zoom);
		int visibleWorldHeight = (int) (handler.getHeight() / zoom);

		int xStart = Math.max(0, (int) (handler.getGameCamera().getxOffset() / Tile.TILEWIDTH));
		int xEnd = Math.min(width, (xStart + visibleWorldWidth / Tile.TILEWIDTH) + 2);
		int yStart = Math.max(0, (int) (handler.getGameCamera().getyOffset() / Tile.TILEHEIGHT));
		int yEnd = Math.min(height, (yStart + visibleWorldHeight / Tile.TILEHEIGHT) + 2);

		for (int y = yStart; y < yEnd; y++) {
			for (int x = xStart; x < xEnd; x++) {
				getTile(x, y).render(g,
						(int) ((x * Tile.TILEWIDTH - handler.getGameCamera().getxOffset()) * zoom),
						(int) ((y * Tile.TILEHEIGHT - handler.getGameCamera().getyOffset()) * zoom),
						(int) (Tile.TILEWIDTH * zoom),
						(int) (Tile.TILEHEIGHT * zoom));
			}
		}

		for (Footstep footstep : footsteps) {
			footstep.render(g, zoom, (int) handler.getGameCamera().getxOffset(), (int) handler.getGameCamera().getyOffset());
		}

		//Items
		itemManager.render(g);
		//Entities
		entityManager.render(g);
	}
	
	public Tile getTile(int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height)
			return Tile.grassTile;
		
		Tile t = Tile.tiles[tiles[x][y]];
		if(t == null)
			return Tile.dirtTile;
		return t;
	}


	private void loadWorld(String path) {
		String file = Utils.loadFileAsString(path);
		String[] tokens = file.split("\\s+");
		width = Utils.parseInt(tokens[0]);
		height = Utils.parseInt(tokens[1]);
		spawnX = Utils.parseInt(tokens[2]);
		spawnY = Utils.parseInt(tokens[3]);

		double treeSpawnChance = 0.25;
		double stoneSpawnChance = 0.1;

		int grassTileValue = 0;
		int dirtTileValue = 1;

		tiles = new int[width][height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				tiles[x][y] = Utils.parseInt(tokens[(x + y * width) + 4]);
				if (tiles[x][y] == grassTileValue && random.nextDouble() < treeSpawnChance) {
					entityManager.addEntity(new Tree(handler, x * Tile.TILEWIDTH, y * Tile.TILEHEIGHT));
				} else if ((tiles[x][y] == grassTileValue || tiles[x][y] == dirtTileValue) && random.nextDouble() < stoneSpawnChance) {
					entityManager.addEntity(new Rock(handler, x * Tile.TILEWIDTH, y * Tile.TILEHEIGHT));
				}
			}
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public ItemManager getItemManager() {
		return itemManager;
	}

	public Handler getHandler() {
		return handler;
	}

	// TODO: move this
	public void addFootstep(int x, int y) {
		footsteps.add(new Footstep(x, y, Tile.TILEWIDTH, Tile.TILEHEIGHT));
	}
  
}
