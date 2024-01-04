package dev.codenmore.tilegame.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {
	
public static BufferedImage loadImage(String path) {  //Location of image
	try {
		return ImageIO.read(ImageLoader.class.getResource(path)); //returns buffered image
	} catch (IOException e) {
		e.printStackTrace();
		System.exit(1); 
	}
	return null;
	
}
	

}
