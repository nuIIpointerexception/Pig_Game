package dev.codenmore.tilegame.ui.impl;

import dev.codenmore.tilegame.gfx.Assets;
import dev.codenmore.tilegame.ui.ClickListener;

import java.awt.*;

public class UIText extends UIObject {

    private String text;
    private Font font;
    private Color textColor;

    public UIText(float x, float y, int width, int height, String text) {
        super(x, y, width, height);
        this.text = text;
        this.font = Assets.font28;
        this.textColor = Color.WHITE;
    }

    @Override
    public void tick() {
    }

    @Override
    public void render(Graphics g) {
        g.setColor(textColor);
        g.setFont(font);
        g.drawString(text, (int) x + width / 2 - g.getFontMetrics().stringWidth(text) / 2, (int) y + height / 2 + g.getFontMetrics().getHeight() / 4);
    }

    @Override
    public void onClick() {
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
