package dev.codenmore.tilegame.ui.impl;

import dev.codenmore.tilegame.gfx.Assets;
import dev.codenmore.tilegame.ui.ClickListener;

import java.awt.*;

public class UITextButton extends UIObject {

    private String text;
    private ClickListener clicker;
    private Font font;
    private Color textColor;

    public UITextButton(float x, float y, int width, int height, String text, ClickListener clicker) {
        super(x, y, width, height);
        this.text = text;
        this.clicker = clicker;
        this.font = Assets.font28;
        this.textColor = Color.WHITE;
    }

    @Override
    public void tick() {
    }

    @Override
    public void render(Graphics g) {
        if(hovering) {
            g.setColor(Color.GRAY);
        } else {
            g.setColor(Color.DARK_GRAY);
        }
        g.fillRect((int) x, (int) y, width, height);

        g.setColor(textColor);
        g.setFont(font);
        g.drawString(text, (int) x + width / 2 - g.getFontMetrics().stringWidth(text) / 2, (int) y + height / 2 + g.getFontMetrics().getHeight() / 4);
    }

    @Override
    public void onClick() {
        clicker.onClick();
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTextColor(Color color) {
        this.textColor = color;
    }

    public void setFont(Font font) {
        this.font = font;
    }
}
