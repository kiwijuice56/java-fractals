package fractal;

import math.ComplexNumber;

import java.awt.*;

public class EscapeFractalDrawer extends FractalDrawer {
	public void draw(Graphics g, int width, int height) {
		double imageWidth = Math.min(width, height) / (double) resolution;

		for (int i = 0; i < height / resolution; i++) {
			for (int j = 0; j < width / resolution; j++) {
				double y = cornerY + zoom * i / imageWidth;
				double x = cornerX + zoom * j / imageWidth;

				int steps = goesToInfinity(new ComplexNumber(julia ? mouseX : x, julia ? mouseY : y), new ComplexNumber(x, y), n, d);
				if (steps == n)
					continue;
				g.setColor(getGradientColor(n, steps));
				g.fillRect(j * resolution, i * resolution, resolution, resolution);
			}
		}
	}

	public int goesToInfinity(ComplexNumber c, ComplexNumber z, int n, int d) {
		if (n == 0)
			return 0;

		z = z.pow(d).add(c);

		if (z.r * z.r + z.i * z.i >= 4.0)
			return 0;

		return 1 + goesToInfinity(c, z, n - 1, d);
	}

}
