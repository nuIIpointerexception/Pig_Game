package dev.codenmore.tilegame.world;

import dev.codenmore.tilegame.tiles.Tile;

public class Chunk {
    public static final int CHUNK_SIZE = 4;
    private Tile[][] tiles;

    public Chunk() {
        tiles = new Tile[CHUNK_SIZE][CHUNK_SIZE];
    }

    public void setTile(int x, int y, Tile value) {
        tiles[x][y] = value;
    }

    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }

}