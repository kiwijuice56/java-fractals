package fractal;

import java.awt.*;

public class MandelBoxDrawer extends FractalDrawer {
	private double pointSize = 0.01, boxSize = 6.0;
	double z = -boxSize;

	@Override
	public void draw(Graphics g, int width, int height) {
		double imageWidth = Math.min(width, height) / (double) resolution;

		z += pointSize;

		if (z > boxSize)
			z = -boxSize;

		for (double y = -boxSize; y < boxSize; y += pointSize) {
			for (double x = -boxSize; x < boxSize; x += pointSize) {
				boolean d = diverges(
						new double[] {x, y, z},
						new double[] {x, y, z},
						2.0, 0.5, n);
				if (d)
					continue;
				double pX = (x / (-z + boxSize + 1) - cornerX) * imageWidth / zoom;
				double pY = (y / (-z + boxSize + 1) - cornerY) * imageWidth / zoom;
				g.setColor(getGradientColor((int) (boxSize * 128), (int) (64 * (z + boxSize))));
				g.fillRect((int) pX * resolution, (int) pY * resolution,
						(int) (pointSize * imageWidth) * resolution, (int) (pointSize * imageWidth) * resolution);

			}
		}
	}

	// Performs a repeating folding function on each 3d point, v, where c is the point being tested
	public boolean diverges(double[] v, double[] c, double s, double r, int n) {
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

		return diverges(v, c, s, r, n - 1);
	}

	@Override
	public String toString() {
		return "[ Mandelbox ]";
	}
}
