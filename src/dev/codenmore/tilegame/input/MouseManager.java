package dev.codenmore.tilegame.input;

import java.awt.event.*;

import dev.codenmore.tilegame.gfx.GameCamera;
import dev.codenmore.tilegame.ui.UIManager;

public class MouseManager implements MouseListener, MouseMotionListener, MouseWheelListener {
	
	private boolean leftPressed, rightPressed;
	private int mouseX, mouseY;
	private UIManager uiManager;
	private float targetZoom = 1.0f;
	
	public MouseManager() {
		
	}
	
	public void setUIManager(UIManager uiManager) {
		this.uiManager = uiManager;
	}
	
	//Getters
	
	public boolean isLeftPressed() {
		return leftPressed;
	}
	
	public boolean isRightPressed() {
		return rightPressed;
	}
	
	public int getMouseX() {
		return mouseX;
	}
	
	public int getMouseY() {
		return mouseY;
	}
	
	//Implemented methods

	@Override
	public void mouseDragged(MouseEvent e) {
	
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		
		if (uiManager != null)
			uiManager.onMouseMove(e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {

		if(e.getButton() == MouseEvent.BUTTON1) 
			leftPressed = false;
			else if (e.getButton() == MouseEvent.BUTTON3)
				rightPressed = false;
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		if(e.getButton() == MouseEvent.BUTTON1) 
			leftPressed = true;
			else if (e.getButton() == MouseEvent.BUTTON3)
				rightPressed = true;
		
		if (uiManager != null)
			uiManager.onMouseRelease(e);
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		targetZoom -= e.getWheelRotation() * 0.05f;
		targetZoom = Math.max(1.0f, Math.min(2.0f, targetZoom));
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public float getTargetZoom() {
		return targetZoom;
	}

	private float zoomTransitionTime = 0f;

	public void tick() {
		float difference = Math.abs(targetZoom - GameCamera.zoomFactor);

		if (difference > 0.01f) {
			zoomTransitionTime += 0.01f;
			if (zoomTransitionTime > 1f) {
				zoomTransitionTime = 1f;
			}

			float t = zoomTransitionTime;
			float c = targetZoom - GameCamera.zoomFactor;
			float b = GameCamera.zoomFactor;

			GameCamera.zoomFactor = cubicEaseOut(t, b, c, 1f);
		} else {
			zoomTransitionTime = 0f;
		}
	}

	private float cubicEaseOut(float t, float b, float c, float d) {
		t /= d;
		t--;
		return c * (t * t * t + 1) + b;
	}

	public void setLeftPressed(boolean b) {
		leftPressed = b;
	}
}
