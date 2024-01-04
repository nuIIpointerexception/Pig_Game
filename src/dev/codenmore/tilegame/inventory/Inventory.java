package dev.codenmore.tilegame.inventory;

import dev.codenmore.tilegame.Handler;
import dev.codenmore.tilegame.gfx.Text;
import dev.codenmore.tilegame.items.Item;
import dev.codenmore.tilegame.gfx.Assets;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Inventory {

    private Handler handler;
    private boolean active = false;
    private ArrayList<Item> inventoryItems;

    private int invX = 64, invY = 48,
            invWidth = 512, invHeight = 384,
            invListCenterX = invX + 171,
            invListCenterY = invY + invHeight / 2 + 5,
            invListSpacing = 30;

    private int invImageX = 452, invImageY = 82,
            invImageWidth = 64, invImageHeight = 64;

    private int invCountX = 484, invCountY = 172;

    private int selectedItem = 0;

    public Inventory(Handler handler) {
        this.handler = handler;
        inventoryItems = new ArrayList<Item>();
        calculateInventoryPosition();
    }

    private void calculateInventoryPosition() {
        int screenWidth = handler.getWidth();
        int screenHeight = handler.getHeight();

        invX = (screenWidth - invWidth) / 2;
        invY = (screenHeight - invHeight) / 2;

        invListCenterX = invX + 171;
        invListCenterY = invY + invHeight / 2 + 5;

        invImageX = invX + 388;
        invImageY = invY + 34;

        invCountX = invX + 420;
        invCountY = invY + 124;
    }

    public void tick() {
        if (handler.getkeyManager().keyJustPressed(KeyEvent.VK_E)) {
            active = !active;
            calculateInventoryPosition();
        }
        if (!active) {
            return;
        }

        if (handler.getkeyManager().keyJustPressed(KeyEvent.VK_W)) {
            selectedItem--;
        }
        if (handler.getkeyManager().keyJustPressed(KeyEvent.VK_S)) {
            selectedItem++;
        }

        if (selectedItem < 0) {
            selectedItem = inventoryItems.size() - 1;
        } else if (selectedItem >= inventoryItems.size()) {
            selectedItem = 0;
        }
    }

    public void render(Graphics g) {
        if (!active) {
            return;
        }

        g.drawImage(Assets.inventoryScreen, invX, invY, invWidth, invHeight, null);
        int len = inventoryItems.size();
        if (len == 0) {
            return;
        }
        for (int i = -5; i < 6; i++) {
            if (selectedItem + i < 0 || selectedItem + i >= len) {
                continue;
            }
            if (i == 0) {
                Text.drawString(g, "> [" + inventoryItems.get(selectedItem + i).getCount() + "] " + inventoryItems.get(selectedItem + i).getName() + " <", invListCenterX, invListCenterY + i * invListSpacing, true, Color.YELLOW, Assets.font28);
            } else {
                Text.drawString(g, inventoryItems.get(selectedItem + i).getName(), invListCenterX, invListCenterY + i * invListSpacing, true, Color.WHITE, Assets.font28);
            }
        }
    }

    //Inventory Methods

    public void addItem(Item item) {
        for (Item i : inventoryItems) {
            if (i.getId() == item.getId()) {
                i.setCount(i.getCount() + item.getCount());
                return;
            }
        }
        inventoryItems.add(item);
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
