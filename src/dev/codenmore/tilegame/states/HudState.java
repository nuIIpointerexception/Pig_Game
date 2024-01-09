package dev.codenmore.tilegame.states;

import dev.codenmore.tilegame.Handler;
import dev.codenmore.tilegame.hud.HealthBar;
import dev.codenmore.tilegame.hud.HudManager;

import java.awt.*;

public class HudState extends State {
    private HudManager hudManager;

    public HudState(Handler handler) {
        super(handler);
        hudManager = new HudManager(handler);
    }

    @Override
    public void tick() {
        hudManager.tick();
    }

    @Override
    public void render(Graphics g) {
        hudManager.render(g);
    }

    public HealthBar getHealthBar() {
        return hudManager.getHealthBar();
    }
}
