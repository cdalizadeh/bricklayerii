import java.awt.image.*;

import javax.imageio.*;

public class Background
{
	public static BufferedImage background = loadImage("Blue.jpg");

	private static BufferedImage loadImage(String name)
	{
		try {
			return ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream(name));
		}
		catch (Throwable t)
		{
			System.err.println("Failed to load " + name);
			return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		}
	}

	public static void changeImage(int level)
	{
		switch (level)
		{
		case 2: background = loadImage("Green.jpg");
			break;
		case 3: background = loadImage("Yellow.jpg");
			break;
		case 4: background = loadImage("Maroon.jpg");
			break;
		case 5: background = loadImage("Navy.jpg");
			break;
		case 6: background = loadImage("Orange.jpg");
			break;
		case 7: background = loadImage("Silver.jpg");
			break;
		case 8: background = loadImage("Lemon.jpg");
			break;
		case 9: background = loadImage("Red.jpg");
			break;
		case 10: background = loadImage("Purple.jpg");
			break;
		}
	}
}