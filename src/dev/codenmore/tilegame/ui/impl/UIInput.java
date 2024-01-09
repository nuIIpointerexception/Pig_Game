package dev.codenmore.tilegame.ui.impl;

import dev.codenmore.tilegame.gfx.Assets;
import dev.codenmore.tilegame.ui.ClickListener;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

public class UIInput extends UIObject {

    private String text;

    private String emptyText;

    private boolean selected = false;

    public UIInput(float x, float y, int width, int height, String defaultText, String emptyText) {
        super(x, y, width, height);
        this.text = defaultText;
        this.emptyText = emptyText;
    }

    @Override
    public void tick() {
        if (selected) {
            // Listen for key presses and update text

        }
    }

    @Override
    public void render(Graphics g) {
        if (selected) {
            g.setColor(Color.WHITE);
            g.fillRect((int) x, (int) y, width, height);
            g.setColor(Color.BLACK);
            g.drawRect((int) x, (int) y, width, height);
            g.setColor(Color.BLACK);
            g.setFont(Assets.font28);
            g.drawString(text, (int) x + 10, (int) y + 30);
        }
        else {
            g.setColor(Color.WHITE);
            g.fillRect((int) x, (int) y, width, height);
            g.setColor(Color.BLACK);
            g.drawRect((int) x, (int) y, width, height);
            g.setColor(Color.GRAY);
            g.setFont(Assets.font28);
            g.drawString(text.isEmpty() ? emptyText : text, (int) x + 10, (int) y + 30);
        }
    }

    @Override
    public void onClick() {

    }

    @Override
    public void onMouseRelease(MouseEvent e) {
        selected = bounds.contains(e.getX(), e.getY());
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void onKeyRelease(KeyEvent e) {
        if (selected) {
            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                if (!text.isEmpty()) {
                    text = text.substring(0, text.length() - 1);
                }
            }
            else {
                text += e.getKeyChar();
            }
        }
    }
}
