package dev.codenmore.tilegame.items;

import dev.codenmore.tilegame.Handler;
import dev.codenmore.tilegame.gfx.Assets;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Item {

    // Handler
    public static Item[] items = new Item[256];

    public static Item woodItem = new Item(Assets.wood, "Wood", 0);
    public static Item pebbleItem = new Item(Assets.pebble, "Pebble", 1);
    public static Item stickItem = new Item(Assets.stick, "Stick", 2);

    public static Item berryItem = new Item(Assets.berry, "Berry", 3);


    // Class

    public static final int ITEMWIDTH = 32, ITEMHEIGHT = 32;

    protected Handler handler;
    protected BufferedImage texture;
    protected String name;
    protected final int id;

    protected Rectangle bounds;

    protected int x, y, count;

    protected boolean pickedUp = false;

    public Item(BufferedImage texture, String name, int id) {
        this.texture = texture;
        this.name = name;
        this.id = id;
        count = 1;

        bounds = new Rectangle(x, y, ITEMWIDTH, ITEMHEIGHT);

        items[id] = this;
    }

    public void tick() {
        if (handler.getWorld().getEntityManager().getPlayer().getCollisionBounds(0f, 0f).intersects(bounds)) {
            pickedUp = true;
            handler.getWorld().getEntityManager().getPlayer().getInventory().addItem(this);
        }
    }

    public void render(Graphics g) {
        if (handler == null)
            return;

        float zoom = handler.getGameCamera().getZoomFactor();
        g.drawImage(texture,                 (int) ((x - handler.getGameCamera().getxOffset()) * zoom),
                (int) ((y - handler.getGameCamera().getyOffset()) * zoom),
                (int) (ITEMWIDTH * zoom),
                (int) (ITEMHEIGHT * zoom),
                null);
    }

    public Item createNew(int count) {
        Item i = new Item(texture, name, id);
        i.setPickedUp(true);
        i.setCount(count);
        return i;
    }

    public Item createNew(int x, int y) {
        Item i = new Item(texture, name, id);
        i.setPosition(x, y);
        return i;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        bounds.x = x;
        bounds.y = y;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public BufferedImage getTexture() {
        return texture;
    }

    public void setTexture(BufferedImage texture) {
        this.texture = texture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setPickedUp(boolean pickedUp) {
        this.pickedUp = pickedUp;
    }

    public boolean isPickedUp() {
        return pickedUp;
    }

    public boolean isVisible() {
        return this.getY() >= handler.getGameCamera().getyOffset() - 32 && this.getY() <= handler.getGameCamera().getyOffset() + handler.getHeight() + 32 &&
                this.getX() >= handler.getGameCamera().getxOffset() - 32 && this.getX() <= handler.getGameCamera().getxOffset() + handler.getWidth() + 32;
    }
}
