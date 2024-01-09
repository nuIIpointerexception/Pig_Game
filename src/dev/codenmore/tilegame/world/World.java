package dev.codenmore.tilegame.world;

import java.awt.*;
import java.util.*;
import java.util.List;

import dev.codenmore.tilegame.Handler;
import dev.codenmore.tilegame.entities.EntityManager;
import dev.codenmore.tilegame.entities.creatures.Player;
import dev.codenmore.tilegame.entities.statics.Footstep;
import dev.codenmore.tilegame.entities.statics.Rock;
import dev.codenmore.tilegame.entities.statics.Tree;
import dev.codenmore.tilegame.gfx.GameCamera;
import dev.codenmore.tilegame.items.ItemManager;
import dev.codenmore.tilegame.tiles.Tile;
import dev.codenmore.tilegame.world.noise.PerlinNoiseGenerator;

public class World {
	
	private Handler handler;
	private int width, height; 
	private int spawnX, spawnY;
	private Map<Point, Chunk> chunks;
	private final int chunkSize = 16;
	//Entities
	private EntityManager entityManager;
	private ItemManager itemManager;

	private List<Footstep> footsteps = new ArrayList<>();

	private Random random;

	private long seed = "testseed".hashCode();


	
	public World(Handler handler) {
		this.handler = handler;
		this.width = 10;
		this.height = 10;
		chunks = new HashMap<>();

		random = new Random(seed);

		itemManager = new ItemManager(handler);

		entityManager = new EntityManager(handler, new Player(handler, 100, 100));

		generateAllChunks();
		
		entityManager.getPlayer().setX(100);
		entityManager.getPlayer().setY(100);
		
	}

	private void generateAllChunks() {
		for (int chunkY = 0; chunkY < this.height; chunkY++) {
			for (int chunkX = 0; chunkX < this.width; chunkX++) {
				generateChunk(chunkX, chunkY);
			}
		}
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
		GameCamera camera = handler.getGameCamera();

		int visibleWorldWidth = (int) (handler.getWidth() / zoom);
		int visibleWorldHeight = (int) (handler.getHeight() / zoom);

		int xStart = Math.max(0, (int) (camera.getxOffset() / Tile.TILEWIDTH));
		int xEnd = Math.min(width, (xStart + visibleWorldWidth / Tile.TILEWIDTH) + 2);
		int yStart = Math.max(0, (int) (camera.getyOffset() / Tile.TILEHEIGHT));
		int yEnd = Math.min(height, (yStart + visibleWorldHeight / Tile.TILEHEIGHT) + 2);

		float scaledWidth = Tile.TILEWIDTH * zoom;
		float scaledHeight = Tile.TILEHEIGHT * zoom;
		int ceilWidth = (int) Math.ceil(scaledWidth);
		int ceilHeight = (int) Math.ceil(scaledHeight);

		for (int y = yStart; y < yEnd; y++) {
			float baseY = (y * Tile.TILEHEIGHT - camera.getyOffset()) * zoom;
			int ceilY = (int) Math.ceil(baseY);

			for (int x = xStart; x < xEnd; x++) {
				float baseX = (x * Tile.TILEWIDTH - camera.getxOffset()) * zoom;
				int ceilX = (int) Math.ceil(baseX);

				getTile(x, y).render(g, ceilX, ceilY, ceilWidth, ceilHeight);
			}
		}

		renderFootsteps(g, zoom, camera);
		itemManager.render(g);
		entityManager.render(g);
	}

	private void renderFootsteps(Graphics g, float zoom, GameCamera camera) {
		for (Footstep footstep : footsteps) {
			footstep.render(g, zoom, (int) camera.getxOffset(), (int) camera.getyOffset());
		}
	}

	public Tile getTile(int x, int y) {
		int chunkX = x / Chunk.CHUNK_SIZE;
		int chunkY = y / Chunk.CHUNK_SIZE;
		int localX = x % Chunk.CHUNK_SIZE;
		int localY = y % Chunk.CHUNK_SIZE;

		Chunk chunk = chunks.get(new Point(chunkX, chunkY));
		return chunk != null ? chunk.getTile(localX, localY) : Tile.dirtTile;
	}

	public void setTile(int x, int y, int value) {
		int chunkX = x / Chunk.CHUNK_SIZE;
		int chunkY = y / Chunk.CHUNK_SIZE;
		int localX = x % Chunk.CHUNK_SIZE;
		int localY = y % Chunk.CHUNK_SIZE;

		Chunk chunk = chunks.get(new Point(chunkX, chunkY));
		if (chunk != null) {
			chunk.setTile(localX, localY, Tile.tiles[value]);
		}
	}

	private void generateChunk(int chunkX, int chunkY) {
		PerlinNoiseGenerator perlinNoise = new PerlinNoiseGenerator(seed);
		Chunk chunk = new Chunk();

		for (int y = 0; y < chunkSize; y++) {
			for (int x = 0; x < chunkSize; x++) {
				int worldX = chunkX * chunkSize + x;
				int worldY = chunkY * chunkSize + y;

				perlinNoise.setFrequency(0.3);
				perlinNoise.setScale(0.5);
				float grassLayer = (float) perlinNoise.getNoise(worldX, worldY, 0);

				perlinNoise.setFrequency(0.1);
				perlinNoise.setScale(0.2);
				float stoneLayer = (float) perlinNoise.getNoise(worldX, worldY, 1);

				if (stoneLayer > 0.7) {
					chunk.setTile(x, y, Tile.rockTile);
				} else {
					chunk.setTile(x, y, grassLayer > 0.4 ? Tile.grassTile : Tile.dirtTile);

					if (chunk.getTile(x, y).getID() == 0 && random.nextFloat() < 0.1) {
						entityManager.addEntity(new Tree(handler, worldX * Tile.TILEWIDTH, worldY * Tile.TILEHEIGHT));
					} else if (chunk.getTile(x, y).getID() == 1 && random.nextFloat() < 0.1) {
						entityManager.addEntity(new Rock(handler, worldX * Tile.TILEWIDTH, worldY * Tile.TILEHEIGHT));
					}
				}
			}
		}

		chunks.put(new Point(chunkX, chunkY), chunk);
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
