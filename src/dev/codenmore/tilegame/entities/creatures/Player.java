package dev.codenmore.tilegame.entities.creatures;

import java.awt.*;
import java.awt.image.BufferedImage;
import dev.codenmore.tilegame.Handler;
import dev.codenmore.tilegame.entities.Entity;
import dev.codenmore.tilegame.gfx.Animation;
import dev.codenmore.tilegame.gfx.Assets;
import dev.codenmore.tilegame.input.KeyManager;
import dev.codenmore.tilegame.inventory.Inventory;
import dev.codenmore.tilegame.states.HudState;
import dev.codenmore.tilegame.tiles.Tile;

public class Player extends Creature {

    private static final int ANIM_DURATION = 500;
    private static final int ANIM_STAND_DURATION = 1000;
    private Animation animLeft, animRight, animUpLeft, animUpRight, animDownLeft, animDownRight, animStandLeft, animStandRight;
    private enum Direction {
        LEFT, RIGHT
    }
    private enum State {
        MOVING, STANDING
    }
    private static final float SPRINT_SPEED = 1.5f;
    private Direction currentDirection = Direction.RIGHT;
    private State currentState = State.STANDING;
    private long lastAttackTimer;
    private final long attackCooldown = 800;
    private long attackTimer = attackCooldown;
    private long lastFootstepTime;
    private static final long FOOTSTEP_DELAY = 600;
    private float xVelocity, yVelocity;
    private float acceleration = 1.0f;
    private final float deceleration = 0.2f;
    private float maxSpeed = speed;
    private float friction = 0.15f;
    private Inventory inventory;
    private static final int ATTACK_RADIUS = 100;

    public Player(Handler handler, float x, float y) {
        super(handler, x, y, Creature.DEFAULT_CREATURE_WIDTH, Creature.DEFAULT_CREATURE_HEIGHT);
        bounds.x = 6;
        bounds.y = 32;
        bounds.width = 48;
        bounds.height = 31;
        initAnimations();
    }

    private void initAnimations() {
        animLeft = new Animation(ANIM_DURATION, Assets.player_left);
        animRight = new Animation(ANIM_DURATION, Assets.player_right);
        animUpLeft = new Animation(ANIM_DURATION, Assets.player_up_left);
        animUpRight = new Animation(ANIM_DURATION, Assets.player_up_right);
        animDownLeft = new Animation(ANIM_DURATION, Assets.player_down_left);
        animDownRight = new Animation(ANIM_DURATION, Assets.player_down_right);
        animStandLeft = new Animation(ANIM_STAND_DURATION, Assets.player_stand_left);
        animStandRight = new Animation(ANIM_STAND_DURATION, Assets.player_stand_right);
        inventory = new Inventory(handler);
    }

    @Override
    public void tick() {
        tickAnimations();
        getInput();
        applyFriction();
        move();
        handler.getGameCamera().centerOnEntity(this);
        createFootsteps();
        checkAttacks();
        inventory.tick();
    }

    private void createFootsteps() {
        if (currentState == State.MOVING && handler.getkeyManager().shift) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastFootstepTime > FOOTSTEP_DELAY) {
                int tileX = (int) (x / Tile.TILEWIDTH) + 1;
                int tileY = (int) (y / Tile.TILEHEIGHT) + 1;
                if (!handler.getWorld().getTile(tileX, tileY).isSolid()) {
                    handler.getWorld().addFootstep(tileX, tileY);
                    lastFootstepTime = currentTime;
                }
            }
        }
    }

    private void checkAttacks() {
        attackTimer = System.currentTimeMillis() - lastAttackTimer;
        if(attackTimer < attackCooldown) {
            return;
        }

        if (handler.getMouseManager().isLeftPressed()) {
            float zoom = handler.getGameCamera().getZoomFactor();
            float xOffset = handler.getGameCamera().getxOffset();
            float yOffset = handler.getGameCamera().getyOffset();

            int mouseX = handler.getMouseManager().getMouseX();
            int mouseY = handler.getMouseManager().getMouseY();

            // Convert display coordinates to game world coordinates
            float gameWorldX = (mouseX / zoom) + xOffset;
            float gameWorldY = (mouseY / zoom) + yOffset;

            // Check if click is in the direction of an entity and close enough
            Entity closestEntity = null;
            double minDistance = Double.MAX_VALUE;

            for (Entity e : handler.getWorld().getEntityManager().getEntities()) {
                if (e.equals(this)) {
                    continue;
                }

                double distanceToEntity = Math.sqrt(Math.pow(e.getX() - x, 2) + Math.pow(e.getY() - y, 2));
                if (distanceToEntity <= ATTACK_RADIUS) {
                    // Check if the entity is in the direction of the click
                    float ex = e.getX() + e.getWidth() / 2;
                    float ey = e.getY() + e.getHeight() / 2;
                    float dx = gameWorldX - (x + width / 2);
                    float dy = gameWorldY - (y + height / 2);
                    float dotProduct = (ex - x) * dx + (ey - y) * dy;

                    if (dotProduct > 0 && distanceToEntity < minDistance) {
                        closestEntity = e;
                        minDistance = distanceToEntity;
                    }
                }
            }

            if (closestEntity != null) {
                closestEntity.hurt(1);
                ((HudState) handler.getGame().hudState).getHealthBar().setEntity(closestEntity);
                ((HudState) handler.getGame().hudState).getHealthBar().resetTimer();
            }

            lastAttackTimer = System.currentTimeMillis();
            handler.getMouseManager().setLeftPressed(false);
        }
    }



    private void tickAnimations() {
        animLeft.tick();
        animRight.tick();
        animUpLeft.tick();
        animUpRight.tick();
        animDownLeft.tick();
        animDownRight.tick();
        animStandLeft.tick();
        animStandRight.tick();
    }

    private void getInput() {
        KeyManager keyManager = handler.getkeyManager();
        if (inventory.isActive()) {
            xVelocity = yVelocity = 0;
            return;
        }
        if (keyManager.shift) {
            maxSpeed = speed * SPRINT_SPEED;
        } else {
            maxSpeed = speed;
        }
        if (keyManager.left) {
            xVelocity -= acceleration;
            if (xVelocity < -maxSpeed) xVelocity = -maxSpeed;
        } else if (keyManager.right) {
            xVelocity += acceleration;
            if (xVelocity > maxSpeed) xVelocity = maxSpeed;
        }
        if (keyManager.up) {
            yVelocity -= acceleration;
            if (yVelocity < -maxSpeed) yVelocity = -maxSpeed;
        } else if (keyManager.down) {
            yVelocity += acceleration;
            if (yVelocity > maxSpeed) yVelocity = maxSpeed;
        }
        currentState = (yVelocity != 0 || xVelocity != 0) ? State.MOVING : State.STANDING;
        if (xVelocity != 0) {
            currentDirection = xVelocity < 0 ? Direction.LEFT : Direction.RIGHT;
        }
    }

    private void applyFriction() {
        KeyManager keyManager = handler.getkeyManager();
        if (!keyManager.left && !keyManager.right) {
            xVelocity += (xVelocity > 0) ? -friction : (xVelocity < 0) ? friction : 0;
            xVelocity = (Math.abs(xVelocity) < friction) ? 0 : xVelocity;
        }
        if (!keyManager.up && !keyManager.down) {
            yVelocity += (yVelocity > 0) ? -friction : (yVelocity < 0) ? friction : 0;
            yVelocity = (Math.abs(yVelocity) < friction) ? 0 : yVelocity;
        }
    }

    @Override
    public void move() {
        xMove = xVelocity;
        yMove = yVelocity;
        super.move();
    }

    @Override
    public void render(Graphics g) {
        float zoom = handler.getGameCamera().getZoomFactor();
        g.drawImage(getCurrentAnimationFrame(),
                (int) ((x - handler.getGameCamera().getxOffset()) * zoom),
                (int) ((y - handler.getGameCamera().getyOffset()) * zoom),
                (int) (width * zoom),
                (int) (height * zoom),
                null);

        // TODO: Remove this later, only for debugging
        g.drawOval(handler.getMouseManager().getMouseX() - ATTACK_RADIUS, handler.getMouseManager().getMouseY() - ATTACK_RADIUS, ATTACK_RADIUS * 2, ATTACK_RADIUS * 2);
        g.setColor(Color.red);
        for (Entity e : handler.getWorld().getEntityManager().getEntities()) {
            if (e.equals(this))
                continue;

            int x = (int) ((e.getX() - handler.getGameCamera().getxOffset()) * zoom);
            int y = (int) ((e.getY() - handler.getGameCamera().getyOffset()) * zoom);
            int width = (int) (e.getWidth() * zoom);
            int height = (int) (e.getHeight() * zoom);
            g.drawRect(x, y, width, height);
        }
    }

    public void postRender(Graphics g){
        inventory.render(g);
    }

    @Override
    public void die(){
        System.out.println("You lose");
    }

    private BufferedImage getCurrentAnimationFrame() {
        boolean isMoving = currentState == State.MOVING;
        return switch (currentDirection) {
            case RIGHT -> isMoving ? animRight.getCurrentFrame() : animStandRight.getCurrentFrame();
            case LEFT -> isMoving ? animLeft.getCurrentFrame() : animStandLeft.getCurrentFrame();
            default -> animStandLeft.getCurrentFrame();
        };
    }

    public Inventory getInventory() {
        return inventory;
    }
}
