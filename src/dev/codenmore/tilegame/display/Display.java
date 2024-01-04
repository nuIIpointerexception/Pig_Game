package dev.codenmore.tilegame.display;


import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;

public class Display {

	private JFrame frame;
	private Canvas canvas;

	public Display(String title, int width, int height, boolean fullscreen) {
		frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setUndecorated(fullscreen);

		canvas = new Canvas();

		if (fullscreen) {
			GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			if (gd.isFullScreenSupported()) {
				frame.setUndecorated(true);
				gd.setFullScreenWindow(frame);
			} else {
				System.err.println("Fullscreen mode not supported");
				setWindowedMode(width, height);
			}
		} else {
			setWindowedMode(width, height);
		}

		frame.add(canvas);
		frame.setVisible(true);
		frame.pack();
		canvas.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

	private void setWindowedMode(int width, int height) {
		canvas.setPreferredSize(new Dimension(width, height));
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public JFrame getFrame() {
		return frame;
	}

	public boolean getFocus() {
		return frame.isFocused();
	}
}