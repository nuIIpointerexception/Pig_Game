package dev.codenmore.tilegame.states;

import dev.codenmore.tilegame.Handler;
import dev.codenmore.tilegame.gfx.Assets;
import dev.codenmore.tilegame.ui.UIManager;
import dev.codenmore.tilegame.ui.UITextButton;

import java.awt.*;
import java.awt.event.KeyEvent;

public class PopupState extends State {

    private UIManager uiManager;

    public PopupState(Handler handler) {
        super(handler);
        uiManager = new UIManager(handler);
        handler.getMouseManager().setUIManager(uiManager);

        int buttonWidth = 128;
        int buttonHeight = 64;
        int spacing = 20;

        int totalWidth = 2 * buttonWidth + spacing;
        int centerX = (handler.getWidth() - totalWidth) / 2;
        int centerY = (handler.getHeight() - buttonHeight) / 2;

        uiManager.addObject(new UITextButton(centerX, centerY, buttonWidth, buttonHeight, "Yes", () -> System.exit(0)));
        uiManager.addObject(new UITextButton(centerX + buttonWidth + spacing, centerY, buttonWidth, buttonHeight, "No", () -> State.setState(handler.getGame().getPreviousState())));
    }

    @Override
    public void tick() {
        uiManager.tick();
        if(handler.getkeyManager().keyJustPressed(KeyEvent.VK_ESCAPE)) {
            State.setState(handler.getGame().getPreviousState());
        }
    }

    @Override
    public void render(Graphics g) {
        g.setFont(Assets.font48);
        g.setColor(Color.BLACK);
        String quitText = "Quit?";
        int textWidth = g.getFontMetrics().stringWidth(quitText);
        int textX = (handler.getWidth() - textWidth) / 2;
        int textY = (handler.getHeight() / 2) - 100;
        g.drawString(quitText, textX, textY);

        uiManager.render(g);
    }
}
