package dev.codenmore.tilegame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import dev.codenmore.tilegame.display.Display;
import dev.codenmore.tilegame.gfx.Assets;
import dev.codenmore.tilegame.gfx.GameCamera;
import dev.codenmore.tilegame.gfx.ImageLoader;
import dev.codenmore.tilegame.gfx.SpriteSheet;
import dev.codenmore.tilegame.input.KeyManager;
import dev.codenmore.tilegame.input.MouseManager;
import dev.codenmore.tilegame.states.*;

public class Game implements Runnable {
	
	private Display display;
	public String title;
	
	private boolean running = false;
	private Thread thread;
	
	private BufferStrategy bs;
	private Graphics g;
	
	//States
	public State gameState;
	public State menuState;
	public State popupState;
	public State hudState;
	
	//Input
	private KeyManager keyManager;
	private MouseManager mouseManager;
	
	//Camera
	private GameCamera gameCamera;
	
	//Handler
	private Handler handler;

	private State previousState;

	private boolean fullscreen;
	private int width, height;

	
	public Game(String title, String resolution , boolean fullscreen) {
		this.title = title;
		keyManager = new KeyManager();
		mouseManager = new MouseManager();
		this.fullscreen = fullscreen;
		String[] res = resolution.split("x");
		width = Integer.parseInt(res[0]);
		height = Integer.parseInt(res[1]);
	}
	
	private void init() {
		display = new Display(title, width, height, fullscreen);
		display.getCanvas().addKeyListener(keyManager);
		display.getCanvas().addMouseListener(mouseManager);
		display.getCanvas().addMouseMotionListener(mouseManager);
		display.getCanvas().addMouseWheelListener(mouseManager);
		Assets.init();
		
		handler = new Handler(this);
		gameCamera = new GameCamera(handler, 0, 0);
		
		
		gameState = new GameState(handler);
		menuState = new MenuState(handler);
		hudState = new HudState(handler);

		State.setState(menuState);
		
	}
	
	private void tick() {
		keyManager.tick();
		mouseManager.tick();
		hudState.tick();

		if(State.getState() != null) {
			State.getState().tick();
		}

		if(keyManager.keyJustPressed(KeyEvent.VK_ESCAPE)) {
			if(State.getState() instanceof PopupState) {
				State.setState(null);
			} else {
				handler.getGame().setPreviousState(State.getState());
				State.setState(new PopupState(handler));
			}
		}

	}

	private void setPreviousState(State state) {
		this.previousState = state;
	}

	private void render() {
		bs = display.getCanvas().getBufferStrategy();
		if (bs == null) {
			display.getCanvas().createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();
		//Clear Screen
		g.clearRect(0, 0, display.getFrame().getWidth(), display.getFrame().getHeight());
		//Draw Here!
		
		if(State.getState() != null)
			State.getState().render(g);

		hudState.render(g);
		
		//End Drawing!
		bs.show();
		g.dispose();
	}
	
	public void run() {
		
		init();
		
		int fps = 60;  // max amount of time we're allowed to have to run the tick and render methods
		double timePerTick = 1000000000 / fps;    //measuring time in nano seconds
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		int ticks = 0;
		
		while(running) {
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;  //when and when not to call the render methods
			timer += now - lastTime;
			lastTime = now;
			
			if(delta >= 1) {
			    tick();
			    render();
			    ticks++;
			    delta--;
			}    
			
			if(timer >= 1000000000) {
				ticks = 0;
				timer = 0;
			}
		}
		
		stop(); // im fall, dass es noch nicht stoppt
		
	}

	public int getWidth() {
		return display.getFrame().getWidth();
	}

	public int getHeight() {
		return display.getFrame().getHeight();
	}
	
	public KeyManager getKeyManager() {
		return keyManager;
	}
	
	public MouseManager getMouseManager() {
		return mouseManager;
	}
	
	public GameCamera getGameCamera() {
		return gameCamera;
	}
	
	public synchronized void start() {
		if(running)
			return;  // falls es schon läuft
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
    public synchronized void stop() {
    	if(!running)
    		return;
    	running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	public State getPreviousState() {
		return previousState;
	}
}
