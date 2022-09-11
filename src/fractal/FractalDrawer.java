package fractal;

import java.awt.*;

public class FractalDrawer {
	// Used to position and scale fractals
	public double cornerX = -2.5, cornerY = -2.0, zoom = 3.5;
	public double mouseX = 0, mouseY = 0;
	// n is the number of iterations, while resolution is the ratio of the size of a rendered pixel to an actual pixel
	public int n = 8, resolution = 2;
	// number of dimensions for the escape fractals
	public int d = 2;

	public enum ColorMode { PURPLE, RAINBOW, WHITE, BLACK, CANDY, ROYAL, STAT }
	public ColorMode currentMode = ColorMode.PURPLE;

	private static final Color[] candyPalette = {
			new Color(115,90,190),
			new Color(156, 119, 241),
			new Color(237, 113, 173),
			new Color(254, 241, 244),
			Color.WHITE};
	private static final float[] candyPos = {0.0f, 0.15f, 0.4f, .8f, 1.0f};

	private static final Color[] royalPalette = {
			Color.BLACK,
			new Color(  0,   7, 100),
			new Color( 32, 107, 203),
			new Color(237, 255, 255),
			new Color(255, 170,   0),
			new Color(  86,   0,   5)};
	private static final float[] royalPos = {0.0f, 0.16f, 0.42f, 0.6425f, 0.8575f, 1.0f};

	private static final Color[] statPalette = {
			new Color(62, 25, 110),
			new Color(212, 108, 118),
			new Color(255, 192, 124),
			Color.WHITE
	};
	private static final float[] statPos = {0.0f, 0.5f, 0.9f, 1.0f};

	public void draw(Graphics g, int width, int height) {}

	public Color getGradientColor(int n, int steps) {
		// Get a value from 0 to 1, scaled with a log function for prettier visuals
		float x = (float) (n - steps) / n;
		x *= x * x;
		switch (currentMode) {
			case PURPLE -> { return new Color(Color.HSBtoRGB(1.0f - (x/2.5f), x, steps == n ? 0 : 1.0f - x)); }
			case RAINBOW -> { return new Color(Color.HSBtoRGB(1.0f - (x), 1.0f, steps == n ? 0 : 1.0f));}
			case WHITE -> { return new Color(Color.HSBtoRGB(0.0f, 0.0f, steps == n ? 0 : x));}
			case BLACK -> { return new Color(Color.HSBtoRGB(0.0f, 0.0f, steps == n ? 0 : 1.0f - x));}
			case CANDY -> { return getPaletteColor(candyPos, x, candyPalette); }
			case ROYAL -> { return getPaletteColor(royalPos, x, royalPalette); }
			case STAT -> { return getPaletteColor(statPos, x, statPalette); }
		}
		return Color.BLACK;
	}

	public Color getPaletteColor(float[] pos, float x, Color[] col) {
		x = (1.0f - x);
		for (int i = 0; i < pos.length; i++) {
			if (x >= pos[i])
				continue;
			x = (x - pos[i-1]) * (1 / (pos[i] - pos[i-1]));
			return new Color(
					(int) (x * col[i].getRed() + (1.0 - x) * col[i - 1].getRed()),
					(int) (x * col[i].getGreen() + (1.0 - x) * col[i - 1].getGreen()),
					(int) (x * col[i].getBlue() + (1.0 - x) * col[i - 1].getBlue()));
		}
		return Color.BLACK;
	}
}