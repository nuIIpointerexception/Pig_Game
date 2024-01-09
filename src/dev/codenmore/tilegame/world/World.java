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
	private final int chunkSize = 4;
	//Entities
	private EntityManager entityManager;
	private ItemManager itemManager;

	private List<Footstep> footsteps = new ArrayList<>();

	private Random random;

	private long seed;


	
	public World(Handler handler, long seed) {
		this.handler = handler;
		this.width = 10;
		this.height = 10;
		chunks = new HashMap<>();
		this.seed = seed;
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

		int playerX = (int) entityManager.getPlayer().getX();
		int playerY =  (int) entityManager.getPlayer().getY();

		int playerChunkX = playerX / (Chunk.CHUNK_SIZE * Tile.TILEWIDTH);
		int playerChunkY = playerY / (Chunk.CHUNK_SIZE * Tile.TILEHEIGHT);

		int chunkRange = 3;
		int xChunkStart = Math.max(0, playerChunkX - chunkRange);
		int xChunkEnd = Math.min(width - 1, playerChunkX + chunkRange);
		int yChunkStart = Math.max(0, playerChunkY - chunkRange);
		int yChunkEnd = Math.min(height - 1, playerChunkY + chunkRange);

		for (int chunkY = yChunkStart; chunkY <= yChunkEnd; chunkY++) {
			for (int chunkX = xChunkStart; chunkX <= xChunkEnd; chunkX++) {
				for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
					for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
						int worldX = chunkX * Chunk.CHUNK_SIZE + x;
						int worldY = chunkY * Chunk.CHUNK_SIZE + y;
						getTile(worldX, worldY).render(g, (int) Math.ceil( (worldX * Tile.TILEWIDTH - camera.getxOffset()) * zoom),
								(int) Math.ceil((worldY * Tile.TILEHEIGHT - camera.getyOffset()) * zoom),
								(int) Math.ceil(Tile.TILEWIDTH * zoom),
								(int) Math.ceil(Tile.TILEHEIGHT * zoom));
					}
				}
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
