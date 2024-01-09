package dev.codenmore.tilegame.states;

import dev.codenmore.tilegame.Handler;
import dev.codenmore.tilegame.ui.UIManager;
import dev.codenmore.tilegame.ui.impl.UIInput;
import dev.codenmore.tilegame.ui.impl.UISelector;
import dev.codenmore.tilegame.ui.impl.UIText;
import dev.codenmore.tilegame.ui.impl.UITextButton;

import java.awt.*;

public class MenuState extends State {
	
	private UIManager uiManager;

	public MenuState(Handler handler) {
		super(handler);
		uiManager = new UIManager(handler);

		handler.getMouseManager().setUIManager(uiManager);
		handler.getkeyManager().setUIManager(uiManager);

		int buttonWidth = 200;
		int buttonHeight = 100;

		int centerX = (handler.getWidth() - buttonWidth) / 2;
		int centerY = (handler.getHeight() - buttonHeight) / 2;

		uiManager.addObject(new UITextButton(centerX - 110, centerY - 100, buttonWidth, buttonHeight, "Play", () -> {
			handler.getMouseManager().setUIManager(null);
			State.setState(handler.getGame().gameState);
		}));

		uiManager.addObject(new UITextButton(centerX + 110, centerY - 100, buttonWidth, buttonHeight, "Quit", () -> {
			System.exit(0);
		}));

		uiManager.addObject(new UIText(centerX, centerY + 60, buttonWidth, buttonHeight / 2, "Difficulty"));

		uiManager.addObject(new UISelector(centerX, centerY + 100, buttonWidth, buttonHeight / 2, new String[]{"Easy", "Medium", "Hard"}, () -> {

		}));

		// TODO: use seed.
		uiManager.addObject(new UIInput(centerX, centerY + 350, buttonWidth, buttonHeight / 2, "", "Seed"));
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
	
	
