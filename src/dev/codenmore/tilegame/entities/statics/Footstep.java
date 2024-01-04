package dev.codenmore.tilegame.entities.statics;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Random;

public class Footstep {
    private int x, y;
    private int tileWidth, tileHeight;
    private float rotation;
    private static final Random random = new Random();

    private int lifetime = 100;

    private boolean dead = false;

    public Footstep(int x, int y, int tileWidth, int tileHeight) {
        this.x = x;
        this.y = y;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.rotation = (float) (random.nextFloat() * Math.PI * 2);
    }

    public void tick() {
        lifetime -= 1;
        if (lifetime <= 0) {
            dead = true;
        }
    }

    public void render(Graphics g, float zoom, int xOffset, int yOffset) {
        Graphics2D g2d = (Graphics2D) g.create();
        Color footstepColor = new Color(0, 0, 0, 0.2f);
        g2d.setColor(footstepColor);

        int rectWidth = (int)(tileWidth * zoom / 12);
        int rectHeight = (int)(tileHeight * zoom / 12);
        int baseX = (int) ((x * tileWidth - xOffset + 0.5) * zoom);
        int baseY = (int) ((y * tileHeight - yOffset + 0.5) * zoom);
        AffineTransform old = g2d.getTransform();
        g2d.rotate(rotation, baseX, baseY);

        g2d.fillRect(baseX + rectWidth, baseY + rectHeight, rectWidth, rectHeight);
        g2d.fillRect(baseX + 2 * rectWidth, baseY + 2 * rectHeight, rectWidth, rectHeight);
        g2d.setTransform(old);
        g2d.dispose();
    }

    public boolean isDead() {
        return dead;
    }

}