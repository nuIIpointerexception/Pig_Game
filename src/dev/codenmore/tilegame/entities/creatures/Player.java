package dev.codenmore.tilegame.entities.creatures;

import java.awt.*;
import java.awt.image.BufferedImage;
import dev.codenmore.tilegame.Handler;
import dev.codenmore.tilegame.entities.Entity;
import dev.codenmore.tilegame.gfx.Animation;
import dev.codenmore.tilegame.gfx.Assets;
import dev.codenmore.tilegame.input.KeyManager;
import dev.codenmore.tilegame.inventory.Inventory;
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

    private Inventory inventory;

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


    private void checkAttacks(){
        attackTimer += System.currentTimeMillis() - lastAttackTimer;
        lastAttackTimer = System.currentTimeMillis();
        if(attackTimer < attackCooldown)
            return;

        Rectangle cb = getCollisionBounds(0, 0);
        Rectangle ar = new Rectangle();
        int arSize = 20;
        ar.width = arSize;
        ar.height = arSize;

        if(handler.getkeyManager().aUp){
            ar.x = cb.x + cb.width / 2 - arSize / 2;
            ar.y = cb.y - arSize;
        }else if(handler.getkeyManager().aDown){
            ar.x = cb.x + cb.width / 2 - arSize / 2;
            ar.y = cb.y + cb.height;
        }else if(handler.getkeyManager().aLeft){
            ar.x = cb.x - arSize;
            ar.y = cb.y + cb.height / 2 - arSize / 2;
        }else if(handler.getkeyManager().aRight){
            ar.x = cb.x + cb.width;
            ar.y = cb.y + cb.height / 2 - arSize / 2;
        }else{
            return;
        }

        attackTimer = 0;

        for(Entity e : handler.getWorld().getEntityManager().getEntities()){
            if(e.equals(this))
                continue;
            if(e.getCollisionBounds(0, 0).intersects(ar)){
                e.hurt(1);
                return;
            }
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

        if (inventory.isActive()) {
            xMove = 0;
            yMove = 0;
            return;
        }

        KeyManager keyManager = handler.getkeyManager();

        xMove = keyManager.left ? -speed : (keyManager.right ? speed : 0);
        yMove = keyManager.up ? -speed : (keyManager.down ? speed : 0);

        if (keyManager.shift) {
            xMove *= SPRINT_SPEED;
            yMove *= SPRINT_SPEED;
        }
        
        currentState = (yMove != 0 || xMove != 0) ? State.MOVING : State.STANDING;

        if (xMove != 0) {
            currentDirection = xMove < 0 ? Direction.LEFT : Direction.RIGHT;
        }
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
        
        switch (currentDirection) {
            case RIGHT:
                return isMoving ? animRight.getCurrentFrame() : animStandRight.getCurrentFrame();
            case LEFT:
                return isMoving ? animLeft.getCurrentFrame() : animStandLeft.getCurrentFrame();
            default:
                return animStandLeft.getCurrentFrame();
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

}
