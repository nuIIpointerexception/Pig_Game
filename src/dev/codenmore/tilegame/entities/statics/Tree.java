package dev.codenmore.tilegame.entities.statics;

import java.awt.Graphics;

import dev.codenmore.tilegame.Handler;
import dev.codenmore.tilegame.gfx.Assets;
import dev.codenmore.tilegame.items.Item;
import dev.codenmore.tilegame.tiles.Tile;
import dev.codenmore.tilegame.hud.HealthBar;

public class Tree extends StaticEntity {
	private HealthBar healthBar;
	public Tree(Handler handler, float x, float y) {
		super(handler, x, y, Tile.TILEWIDTH, Tile.TILEHEIGHT);

		this.setHealth(2);
		bounds.x = 10;
		bounds.y = (int) (height / 1.5f);
		bounds.width = width - 20;
		bounds.height = (int) (height - height / 1.5f);
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void hurt(int amt){
		super.hurt(amt);
	}

	@Override
	public void die(){
		handler.getWorld().getItemManager().addItem(Item.woodItem.createNew((int) x, (int) y));
	}

	@Override
	public void render(Graphics g) {
		float zoom = handler.getGameCamera().getZoomFactor();
		g.drawImage(Assets.tree,
				(int) ((x - handler.getGameCamera().getxOffset()) * zoom),
				(int) ((y - handler.getGameCamera().getyOffset()) * zoom),
				(int) (width * zoom),
				(int) (height * zoom),
				null);
	}

}
