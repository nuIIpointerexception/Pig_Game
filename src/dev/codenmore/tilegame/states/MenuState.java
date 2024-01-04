package dev.codenmore.tilegame.states;

import dev.codenmore.tilegame.Handler;
import dev.codenmore.tilegame.gfx.Assets;
import dev.codenmore.tilegame.ui.UIImageButton;
import dev.codenmore.tilegame.ui.UIManager;
import dev.codenmore.tilegame.ui.UITextButton;

import java.awt.*;

public class MenuState extends State {
	
	private UIManager uiManager;

	public MenuState(Handler handler) {
		super(handler);
		uiManager = new UIManager(handler);

		handler.getMouseManager().setUIManager(uiManager);

		int buttonWidth = 200;
		int buttonHeight = 100;

		int centerX = (handler.getWidth() - buttonWidth) / 2;
		int centerY = (handler.getHeight() - buttonHeight) / 2;

		uiManager.addObject(new UITextButton(centerX, centerY - 100, buttonWidth, buttonHeight, "Play", () -> {
			handler.getMouseManager().setUIManager(null);
			State.setState(handler.getGame().gameState);
		}));
	}


	@Override
	public void tick() {
		uiManager.tick();
	}

	@Override
	public void render(Graphics g) {
		uiManager.render(g);
	}
}
	
	
