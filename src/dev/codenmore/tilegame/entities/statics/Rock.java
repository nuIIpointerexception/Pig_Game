package dev.codenmore.tilegame.entities.statics;

import dev.codenmore.tilegame.Handler;
import dev.codenmore.tilegame.gfx.Assets;
import dev.codenmore.tilegame.items.Item;
import dev.codenmore.tilegame.tiles.Tile;
import dev.codenmore.tilegame.hud.HealthBar;

import java.awt.*;

public class Rock extends StaticEntity {

	private HealthBar healthBar;

	public Rock(Handler handler, float x, float y) {
		super(handler, x, y, Tile.TILEWIDTH, Tile.TILEHEIGHT);

		this.health = 3;
		bounds.x = 10;
		bounds.y = (int) (height / 1.5f);
		bounds.width = width - 20;
		bounds.height = (int) (height * 0.1f);
		healthBar = new HealthBar();
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void hurt(int amt){
		super.hurt(amt);
		healthBar.registerHit();
	}

	@Override
	public void die(){
		handler.getWorld().getItemManager().addItem(Item.pebbleItem.createNew((int) x, (int) y));
	}

	@Override
	public void render(Graphics g) {
		float zoom = handler.getGameCamera().getZoomFactor();
		g.drawImage(Assets.rock,
				(int) ((x - handler.getGameCamera().getxOffset()) * zoom),
				(int) ((y - handler.getGameCamera().getyOffset()) * zoom),
				(int) (width * zoom),
				(int) (height * zoom),
				null);

		healthBar.render(g, handler, x, y, width, health, 3);

	}

}
