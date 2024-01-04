package dev.codenmore.tilegame.hud;

import dev.codenmore.tilegame.Handler;

import java.awt.Color;
import java.awt.Graphics;

public class HealthBar {
    private long lastHitTime;
    private static final long VISIBLE_DURATION = 3000;

    public void render(Graphics g, Handler handler, float x, float y, int width, int health, int maxHealth) {
        if (System.currentTimeMillis() - lastHitTime < VISIBLE_DURATION) {
            float healthPercentage = (float) health / maxHealth;
            float healthBarX = (x - handler.getGameCamera().getxOffset()) * handler.getGameCamera().getZoomFactor();
            float healthBarY = (y - handler.getGameCamera().getyOffset() - 5) * handler.getGameCamera().getZoomFactor();
            float healthBarWidth = width * handler.getGameCamera().getZoomFactor();
            float filledWidth = healthBarWidth * healthPercentage;

            g.setColor(Color.BLACK);
            g.fillRect((int) healthBarX, (int) healthBarY, (int) healthBarWidth, 5);

            g.setColor(new Color(130, 22, 22));
            g.fillRect((int) healthBarX, (int) healthBarY, (int) (filledWidth), 5);
        }
    }

    public void registerHit() {
        lastHitTime = System.currentTimeMillis();
    }
}