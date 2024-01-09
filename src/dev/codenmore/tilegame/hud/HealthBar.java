package dev.codenmore.tilegame.hud;

import dev.codenmore.tilegame.Handler;
import dev.codenmore.tilegame.entities.Entity;

import java.awt.Color;
import java.awt.Graphics;

public class HealthBar {

    private static final long VISIBLE_DURATION = 3000;
    private static final Color BACKGROUND_COLOR = Color.BLACK;
    private static final Color FOREGROUND_COLOR = new Color(130, 22, 22);
    private static final int BAR_HEIGHT = 5;

    private Handler handler;
    private Entity entity;

    private long lastHitTime;
    private boolean isVisible;

    public HealthBar(Handler handler) {
        this.handler = handler;
        this.isVisible = false;
    }

    public void tick() {
        if (entity != null) {
            isVisible = System.currentTimeMillis() - lastHitTime <= VISIBLE_DURATION;
        }
    }

    public void render(Graphics g) {
        if (isVisible && entity != null && entity.isActive()) {
            float healthPercent = entity.getHealth() / entity.getMaxHealth();
            float x = (entity.getX() - handler.getGameCamera().getxOffset()) * handler.getGameCamera().getZoomFactor();
            float y = (entity.getY() - handler.getGameCamera().getyOffset() - 5) * handler.getGameCamera().getZoomFactor();
            float width = entity.getWidth() * handler.getGameCamera().getZoomFactor();
            float filledWidth = width * healthPercent;

            g.setColor(BACKGROUND_COLOR);
            g.fillRect((int) x, (int) y, (int) width, BAR_HEIGHT);

            g.setColor(FOREGROUND_COLOR);
            g.fillRect((int) x, (int) y, (int) filledWidth, BAR_HEIGHT);
        }
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public void resetTimer() {
        lastHitTime = System.currentTimeMillis();
    }
}
