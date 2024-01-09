package dev.codenmore.tilegame.hud;

import dev.codenmore.tilegame.Handler;

import java.awt.*;

public class HudManager {

        private Handler handler;

        private HealthBar healthBar;

        public HudManager(Handler handler) {
            this.handler = handler;
            healthBar = new HealthBar(handler);
        }

        public void tick() {
            healthBar.tick();
        }

        public void render(Graphics g) {
            healthBar.render(g);
        }

        public HealthBar getHealthBar() {
            return healthBar;
        }
}
