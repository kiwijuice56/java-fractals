package fractal;

import graphics.ColorInterpolation;

import java.awt.*;

public class Mandelbox extends Fractal {
	// POINT_SIZE is the resolution to iterate through the box points
	// BOX_SIZE is the radius of the cube to check
	// EXTRA_PIXEL_SCALE is an extra modifier to give more control over the pixel size
	private final static double POINT_SIZE = 0.015, BOX_SIZE = 6.0, EXTRA_PIXEL_SCALE = 0.25;
	private double z = -BOX_SIZE;

	public Mandelbox() {
		colorPalette = ColorInterpolation.ColorMode.STAT;
		posX = -1.4;
		posY = -0.8;
		zoom = 1.5;
		n = 6;
	}

	public void draw(Graphics g) {
		z += POINT_SIZE;

		if (z > BOX_SIZE)
			z = -BOX_SIZE;

		int renderPxSize = (int) (EXTRA_PIXEL_SCALE * POINT_SIZE * imageSize * pxSize);

		for (double y = -BOX_SIZE; y < BOX_SIZE; y += POINT_SIZE) {
			for (double x = -BOX_SIZE; x < BOX_SIZE; x += POINT_SIZE) {
				boolean d = escapesSet(new double[] {x, y, z}, new double[] {x, y, z}, 2.0, 0.5, n);
				if (d)
					continue;
				double pX = (x / (-z + BOX_SIZE + 1) - posX) * imageSize / zoom;
				double pY = (y / (-z + BOX_SIZE + 1) - posY) * imageSize / zoom;
				g.setColor(ColorInterpolation.getGradientColor(colorPalette, (int) (BOX_SIZE * 128), (int) (64 * (z + BOX_SIZE))));
				g.fillRect((int) (pX * pxSize), (int) (pY * pxSize), renderPxSize, renderPxSize);

			}
		}
	}

	// Performs a repeating folding function on each 3d point, v, where c is the point being tested
	public boolean escapesSet(double[] v, double[] c, double s, double r, int n) {
		if (n == 0)
			return false;
		for (int i = 0; i < v.length; i++) {
			if (v[i] > 1)
				v[i] = 2 - v[i];
			else if (v[i] < -1)
				v[i] = -2 - v[i];
		}

		double m = Math.cbrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
		if (m < r)
			for (int i = 0; i < v.length; i++) {
				v[i] *= r * r;
			}
		else if (m < 1)
			for (int i = 0; i < v.length; i++) {
				v[i] = 1.0 / v[i];
			}
		for (int i = 0; i < v.length; i++) {
			v[i] *= s;
			v[i] += c[i];
		}

		m = Math.cbrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
		if (m > 4.0)
			return true;

		return escapesSet(v, c, s, r, n - 1);
	}

	public String getName() {
		return "mandelbox";
	}
}
