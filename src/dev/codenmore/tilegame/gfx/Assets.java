package dev.codenmore.tilegame.gfx;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Assets {

	private static final int width = 68, height = 68;

	public static Font font28, font48;

	public static BufferedImage dirt, stone, grass, tree, rock;

	public static BufferedImage wood, pebble, stick, berry;

	public static BufferedImage[] player_left, player_right, player_up_left, player_down_left,
			player_up_right, player_down_right, player_stand_right, player_stand_left;

	public static BufferedImage inventoryScreen;

	public static void init() {
		font28 = FontLoader.loadFont("res/fonts/slkscr.ttf", 28);
		font48 = FontLoader.loadFont("res/fonts/slkscr.ttf", 48);
		SpriteSheet sheet = new SpriteSheet(ImageLoader.loadImage("/textures/sprites.png"));
		SpriteSheet sheet_player = new SpriteSheet(ImageLoader.loadImage("/textures/pig.png"));

		inventoryScreen = ImageLoader.loadImage("/textures/inventory.png");
		
		player_stand_right = new BufferedImage[1];
		player_stand_left = new BufferedImage[1];
		
        player_left = new BufferedImage[2];
		player_right = new BufferedImage[2];
		
		player_up_left = new BufferedImage[2];
		player_down_left = new BufferedImage[2];

		player_up_right = new BufferedImage[2];
		player_down_right = new BufferedImage[2];

		player_stand_right[0] = sheet_player.crop(0, height, width, height);
		player_stand_left[0] = sheet_player.crop(0, 0, width, height);
		
		player_left[0] = sheet_player.crop(width, 0, width, height);
		player_left[1] = sheet_player.crop(width * 2, 0, width, height);
		
		player_right[0] = sheet_player.crop(width, height, width, height);
		player_right[1] = sheet_player.crop(width * 2, height, width, height);

		player_up_left[0] = sheet_player.crop(width, 0, width, height);
		player_up_left[1] = sheet_player.crop(width, 0, width, height);
		player_down_left[0] = sheet_player.crop(width, 0, width, height);
		player_down_left[1] = sheet_player.crop(width, 0, width, height);
		
		player_up_right[0] = sheet_player.crop(width, height, width, height);
		player_up_right[1] = sheet_player.crop(width, height,  width, height);
		player_down_right[0] = sheet_player.crop(width, height, width, height);
		player_down_right[1] = sheet_player.crop(width, height, width, height);

		dirt = sheet.crop(0, 0, width, height);
		stone = sheet.crop(width, 0, width, height);
		grass = sheet.crop(width * 2, 0, width, height);
		tree = sheet.crop(width, height, width, height);
		rock = sheet.crop(width * 2, height, width, height);
		wood =  sheet.crop(0, height * 2, width, height);
		pebble = sheet.crop(width, height * 2, width, height);
	}

}
