package dev.codenmore.tilegame.entities;

import java.awt.Graphics;
import java.awt.Rectangle;

import dev.codenmore.tilegame.Game;
import dev.codenmore.tilegame.Handler;

public abstract class Entity {
	
	protected Handler handler;
	protected float x, y;         //protected: restrict visibility, can be accessed by the members of its on class, float for smooth movement in game 
    protected int width, height;
	protected int health, maxHealth;
	protected boolean active = true;
    protected Rectangle bounds;
                             
	public Entity(Handler handler, float x, float y, int width, int height) {
		this.handler = handler;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		bounds = new Rectangle(0, 0, width, height);
	}
	
	public boolean checkEntityCollisions(float xOffset, float yOffset) {
		for(Entity e: handler.getWorld().getEntityManager().getEntities()) {
			if(e.equals(this))
				continue;
			if(e.getCollisionBounds(0f, 0f).intersects(getCollisionBounds(xOffset, yOffset)))
				return true;
		}
		return false;
	}
	
	public Rectangle getCollisionBounds(float xOffset, float yOffset) {
		return new Rectangle((int) (x + bounds.x + xOffset), (int) (y + bounds.y + yOffset), bounds.width, bounds.height);
	}
                       
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public abstract void tick();
	
	public abstract void render(Graphics g);

	public abstract void die();

	public void hurt(int amt){
		health -= amt;
		if(health <= 0){
			active = false;
			die();
		}
	}

	public boolean isActive() {
		return active;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
		this.maxHealth = health;
	}

	public boolean isVisible() {
		return this.y >= handler.getGameCamera().getyOffset() - this.height && this.y <= handler.getGameCamera().getyOffset() + handler.getHeight() + 32 &&
				this.x >= handler.getGameCamera().getxOffset() - this.width && this.x <= handler.getGameCamera().getxOffset() + handler.getWidth() + 32;
	}

    public float getMaxHealth() {
		return maxHealth;
    }
}
