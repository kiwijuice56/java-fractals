package fractal;

import math.ComplexNumber;

import java.awt.*;

public class BuddhabrotDrawer extends EscapeFractalDrawer {
	private static int maxCount = 0;
	protected int points = 500000;

	public void draw(Graphics g, int width, int height) {
		int[][] count = new int[height][width];
		maxCount = 0;
		for (int p = 0; p < points; p++) {
			double x = Math.random() * 4 - 2;
			double y = Math.random() * 4 - 2;

			plotDivergencePaths(count, new ComplexNumber(x, y), new ComplexNumber(x, y), width, height, n, d);
		}
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				g.setColor(getGradientColor(maxCount, count[i][j]));
				g.fillRect(j * resolution, i * resolution, resolution, resolution);
			}
		}
	}
	
	public boolean plotDivergencePaths(int[][] count, ComplexNumber c, ComplexNumber z, int width, int height, int n, int d) {
		if (n == 0)
			return false;

		z = z.pow(d).add(c);

		if (z.r * z.r + z.i * z.i >= 4.0)
			return true;

		boolean diverges = plotDivergencePaths(count, c, z, width, height, n - 1, d);

		if (diverges) {
			double imageWidth = Math.min(width, height) / (double) resolution;
			int pX = (int) (((z.r - cornerX) * imageWidth) / zoom);
			int pY = (int) (((z.i - cornerY) * imageWidth) / zoom);
			if (pX < 0 || pY < 0 || pY >= count.length || pX >= count[pY].length)
				return true;
			count[pY][pX]++;
			maxCount = Math.max(maxCount, count[pY][pX]);
		} 
		return diverges;
	}

	public String toString() {
		return "[ Buddhabrot ]";
	}
}