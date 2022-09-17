package fractal;

import graphics.ColorInterpolation;
import graphics.DrawingPanel;
import math.ComplexNumber;
import math.Polynomial;

import java.util.Arrays;
import java.util.List;

public class Newton extends Fractal {
	private Polynomial function, derivative;
	private List<ComplexNumber> roots;
	private double tolerance = 0.000001;

	public Newton() {
		n = 64;
		posX = -1.5;
		posY = 0.0;

		colorPalette = ColorInterpolation.ColorMode.FROST;

		function = new Polynomial(Arrays.asList(1.0, 0.0, 0.0, -1.0));
		derivative = function.derivative();
		roots = Arrays.asList(new ComplexNumber(1, 0),
				new ComplexNumber(-0.5, Math.sqrt(3) / 2.0),
				new ComplexNumber(-0.5, -Math.sqrt(3) / 2.0));
	}

	public void draw(DrawingPanel d) {
		for (int i = 0; i < imageHeight / pxSize; i++) {
			for (int j = 0; j < imageWidth / pxSize; j++) {
				double y = posY + zoom * i / imageSize;
				double x = posX + zoom * j / imageSize;

				int steps = approachesRoots(new ComplexNumber(x, y), n);
				if (steps == n)
					continue;
				d.drawPixel(j * pxSize, i * pxSize, ColorInterpolation.getGradientColor(colorPalette, n, steps));
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

	public String getName() {
		return "newton";
	}

}
