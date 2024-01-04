package dev.codenmore.tilegame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class Launcher {

	static ArrayList<String> resolutions = new ArrayList<>();

	public static void main(String[] args) {
		JFrame frame = new JFrame("Schweinchen Settings");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 200);
		frame.setLayout(new FlowLayout());

		JButton playButton = new JButton("Play");
		frame.add(playButton);

		for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
			DisplayMode[] displayModes = gd.getDisplayModes();
			for (DisplayMode displayMode : displayModes) {
				String resolution = displayMode.getWidth() + "x" + displayMode.getHeight();
				resolutions.add(resolution);
			}
		}

		JComboBox<String> resolutionBox = new JComboBox<>(resolutions.toArray(new String[0]));
		frame.add(resolutionBox);

		String[] mode = {"Fullscreen", "Windowed"};
		JComboBox<String> modeBox = new JComboBox<>(mode);
		frame.add(modeBox);

		// Einmal für den anfang.
		resolutionBox.setVisible(!Objects.equals(modeBox.getSelectedItem(), "Fullscreen"));
		modeBox.addActionListener(e -> {
			resolutionBox.setVisible(!Objects.equals(modeBox.getSelectedItem(), "Fullscreen"));
		});


		playButton.addActionListener(e -> {
            boolean fullscreen = Objects.equals(modeBox.getSelectedItem(), "Fullscreen");
			String selectedResolution = (String) resolutionBox.getSelectedItem();

            startGame(selectedResolution, fullscreen);

            frame.dispose();
        });

		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private static void startGame(String resolution, boolean fullscreen) {
		System.out.println("Starting game with " + resolution + " resolution, Fullscreen: " + fullscreen);

		Game game = new Game("Schweinchen!", resolution, fullscreen);
		game.start();
	}
}
