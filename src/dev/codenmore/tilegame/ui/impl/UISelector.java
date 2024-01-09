package dev.codenmore.tilegame.ui.impl;

import dev.codenmore.tilegame.gfx.Assets;
import dev.codenmore.tilegame.ui.ClickListener;

import java.awt.*;
import java.awt.event.MouseEvent;

public class UISelector extends UIObject {

    private ClickListener clicker;

    private String[] options;

    private Font font;

    private Color textColor;

    private boolean open;

    private Rectangle[] bounds;

    private int hoveredOption = -1;

    private int currentMode = 0;

    public UISelector(float x, float y, int width, int height, String[] options, ClickListener clicker) {
        super(x, y, width, height);
        this.clicker = clicker;
        this.options = options;
        this.font = Assets.font28;
        this.textColor = Color.WHITE;
    }

    @Override
    public void tick() {
    }

    @Override
    public void render(Graphics g) {
        initBoundsIfNeeded();

        if (!open) {
            drawOption(g, options[currentMode], (int) x, (int) y, Color.DARK_GRAY);
        }
        else {
            for (int i = 0; i < options.length; i++) {
                Color backgroundColor = (i == currentMode) ? Color.DARK_GRAY : Color.GRAY;
                if (i == hoveredOption) {
                    backgroundColor = Color.LIGHT_GRAY;
                }
                drawOption(g, options[i], (int) x, (int) y + height * i, backgroundColor);
            }
        }
    }


    private void drawOption(Graphics g, String option, int x, int y, Color backgroundColor) {
        int padding = 10;

        int paddedWidth = width - 2 * padding;
        int paddedHeight = height - 2 * padding;

        g.setColor(backgroundColor);
        g.fillRect(x + padding, y + padding, paddedWidth, paddedHeight);

        g.setColor(textColor);
        g.setFont(font);

        int textWidth = g.getFontMetrics().stringWidth(option);
        int textHeight = g.getFontMetrics().getHeight();
        int textX = x + (width - textWidth) / 2;
        int textY = y + (height - textHeight) / 2 + g.getFontMetrics().getAscent();

        g.drawString(option, textX, textY);
    }

    private void initBoundsIfNeeded() {
        if (open) {
            bounds = new Rectangle[options.length];
            for (int i = 0; i < options.length; i++) {
                bounds[i] = new Rectangle((int) x, (int) y + height * i, width, height);
            }
        } else {
            bounds = new Rectangle[1];
            bounds[0] = new Rectangle((int) x, (int) y, width, height);
        }
    }

    @Override
    public void onClick() {
        clicker.onClick();
        open = true;
    }

    @Override
    public void onMouseMove(MouseEvent e) {
        hoveredOption = -1;
        boolean isHoveringOverOption = false;

        if (bounds != null) {
            for (int i = 0; i < bounds.length; i++) {
                if (bounds[i].contains(e.getX(), e.getY())) {
                    hoveredOption = i;
                    isHoveringOverOption = true;
                    break;
                }
            }
        }

        if (isHoveringOverOption && !open) {
            open = true;
            initBoundsIfNeeded();
        } else if (!isHoveringOverOption && open) {
            open = false;
            initBoundsIfNeeded();
        }
    }



    @Override
    public void onMouseRelease(MouseEvent e) {
        if (isOpen()) {
            for (int i = 0; i < bounds.length; i++) {
                if (bounds[i].contains(e.getX(), e.getY())) {
                    currentMode = i;
                    clicker.onClick();
                    break;
                }
            }
            toggleOpen();
        }
    }

    public void toggleOpen() {
        open = !open;
    }

    public boolean isOpen() {
        return open;
    }

    public String[] getOptions() {
        return options;
    }

}
