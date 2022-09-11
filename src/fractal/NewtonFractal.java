package fractal;

import math.ComplexNumber;
import math.Polynomial;

import java.awt.Graphics;
import java.util.Arrays;
import java.util.List;

public class NewtonFractal extends FractalDrawer {
	private Polynomial function, derivative;
	private List<ComplexNumber> roots;
	private double tolerance = 0.000001;

	public void draw(Graphics g, int width, int height) {
		double imageWidth = Math.min(width, height) / (double) resolution;

		function = new Polynomial(Arrays.asList(1.0, 0.0, 0.0, -1.0));
		derivative = function.derivative();
		roots = Arrays.asList(new ComplexNumber(1, 0),
				new ComplexNumber(-0.5, Math.sqrt(3) / 2.0),
				new ComplexNumber(-0.5, -Math.sqrt(3) / 2.0));

		for (int i = 0; i < height / resolution; i++) {
			for (int j = 0; j < width / resolution; j++) {
				double y = cornerY + zoom * i / imageWidth;
				double x = cornerX + zoom * j / imageWidth;

				int steps = approachesRoots(new ComplexNumber(x, y), n);
				if (steps == n)
					continue;
				g.setColor(getGradientColor(n, steps));
				g.fillRect(j * resolution, i * resolution, resolution, resolution);
			}
		}
	}

	public int approachesRoots(ComplexNumber z, int n) {
		if (n == 0)
			return 0;
		z = z.subtract(function.evaluate(z).divide(derivative.evaluate(z)));
		for (ComplexNumber e : roots) {
			ComplexNumber dif = z.subtract(e);
			if (Math.abs(dif.r) < tolerance && Math.abs(dif.i) < tolerance)
				return 0;
		}
		return 1 + approachesRoots(z, n - 1);
	}

	@Override
	public String toString() {
		return "[ Newton ]";
	}

}
