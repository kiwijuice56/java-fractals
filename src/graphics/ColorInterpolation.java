package graphics;

import java.awt.*;

public class ColorInterpolation {
	private static final Color[] CANDY_PALETTE = {
			new Color(115,90,190),
			new Color(156, 119, 241),
			new Color(237, 113, 173),
			new Color(254, 241, 244),
			Color.WHITE};
	private static final float[] CANDY_POS = {0.0f, 0.15f, 0.4f, .8f, 1.0f};

	private static final Color[] FROST_PALETTE = {
			Color.WHITE,
			new Color(215,235,255),
			new Color(5, 64, 125),
			new Color(215,235,255),
			Color.WHITE};
	private static final float[] FROST_POS = {0.0f, 0.15f, 0.4f, .8f, 1.0f};

	private static final Color[] TREE_PALETTE = {
			new Color(1,71, 53),
			new Color(10, 105, 43),
			new Color(20, 150, 35),
			new Color(86, 186, 34),
			new Color(227, 250, 126),
			Color.WHITE };

	private static final float[] TREE_POS = {0.0f, 0.15f, 0.3f, .6f, .8f, 1.0f};

	private static final Color[] ROYAL_PALETTE = {
			Color.BLACK,
			new Color(  0,   7, 100),
			new Color( 32, 107, 203),
			new Color(237, 255, 255),
			new Color(255, 170,   0),
			new Color(  86,   0,   5)};
	private static final float[] ROYAL_POS = {0.0f, 0.16f, 0.42f, 0.6425f, 0.8575f, 1.0f};

	private static final Color[] RANDOM_PALETTE = {
			Color.BLACK,
			new Color(  0,   7, 100),
			new Color( 32, 107, 203),
			new Color(237, 255, 255),
			new Color(255, 170,   0),
			new Color(  86,   0,   5)};
	private static final float[] RANDOM_POS = {0.0f, 0.16f, 0.42f, 0.6425f, 0.8575f, 1.0f};

	private static final Color[] STAT_PALETTE = {
			new Color(62, 25, 110),
			new Color(212, 108, 118),
			new Color(255, 192, 124),
			Color.WHITE
	};
	private static final float[] STAT_POS = {0.0f, 0.5f, 0.9f, 1.0f};

	public enum ColorMode { PURPLE, RAINBOW, WHITE, BLACK, CANDY, ROYAL, STAT, FROST, TREE, RANDOM }
	public static Color getGradientColor(ColorMode mode, int n, int max) {
		float x = (float) (n - max) / n;
		x *= x;
		switch (mode) {
			case PURPLE -> { return new Color(Color.HSBtoRGB(1.0f - (x/2.5f), x, max == n ? 0 : 1.0f - x)); }
			case RAINBOW -> { return new Color(Color.HSBtoRGB(1.0f - (x), 1.0f, max == n ? 0 : 1.0f));}
			case WHITE -> { return new Color(Color.HSBtoRGB(0.0f, 0.0f, max == n ? 0 : x));}
			case BLACK -> { return new Color(Color.HSBtoRGB(0.0f, 0.0f, max == n ? 0 : 1.0f - x));}
			case CANDY -> { return getPaletteColor(CANDY_POS, x, CANDY_PALETTE); }
			case FROST -> { return getPaletteColor(FROST_POS, x, FROST_PALETTE); }
			case ROYAL -> { return getPaletteColor(ROYAL_POS, x, ROYAL_PALETTE); }
			case STAT -> { return getPaletteColor(STAT_POS, x, STAT_PALETTE); }
			case TREE -> { return getPaletteColor(TREE_POS, x, TREE_PALETTE); }
			case RANDOM -> { return getPaletteColor(RANDOM_POS, x, RANDOM_PALETTE); }
		}
		return Color.BLACK;
	}

	public static Color getPaletteColor(float[] keys, float x, Color[] palette) {
		x = 1.0f - x;

		for (int i = 0; i < keys.length; i++) {
			if (x >= keys[i])
				continue;
			x = (x - keys[i-1]) * (1 / (keys[i] - keys[i-1]));
			return new Color(
					(int) (x * palette[i].getRed() + (1.0 - x) * palette[i - 1].getRed()),
					(int) (x * palette[i].getGreen() + (1.0 - x) * palette[i - 1].getGreen()),
					(int) (x * palette[i].getBlue() + (1.0 - x) * palette[i - 1].getBlue()));
		}
		return Color.BLACK;
	}

	public static void randomizePalette() {
		for (int i = 0; i < RANDOM_PALETTE.length; i++)
			RANDOM_PALETTE[i] = new Color((int)(Math.random() * 0x1000000));
	}
}
