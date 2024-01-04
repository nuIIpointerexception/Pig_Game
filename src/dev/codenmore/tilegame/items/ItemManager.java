package dev.codenmore.tilegame.items;

import dev.codenmore.tilegame.Handler;
import dev.codenmore.tilegame.gfx.Assets;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class ItemManager {

    private Handler handler;
    private ArrayList<Item> items;
    public static Item woodItem = new Item(Assets.wood, "Wood", 0);

    public ItemManager(Handler handler) {
        this.handler = handler;
        items = new ArrayList<>();
    }

    public void tick() {
        Iterator<Item> it = items.iterator();
        while (it.hasNext()) {
            Item i = it.next();
            i.tick();
            if (i.isPickedUp())
                it.remove();
        }
    }

    public void render(Graphics g) {
        for (Item i : items) {
            if (i.isVisible())
                i.render(g);
        }
    }

    public void addItem(Item i) {
        i.setHandler(handler);
        items.add(i);
    }

    public void removeItem(Item i) {
        items.remove(i);
    }

    public Handler getHandler() {
        return handler;
    }

    public Item getItemById(int id) {
        for (Item i : items) {
            if (i.getId() == id)
                return i;
        }
        return null;
    }


}
