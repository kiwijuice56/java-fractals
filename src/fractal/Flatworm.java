package fractal;

import graphics.ColorInterpolation;
import math.ComplexNumber;

public class Flatworm extends Fractal {
	public Flatworm() {
		colorPalette = ColorInterpolation.ColorMode.CANDY;
	}

	public int escapesSet(ComplexNumber c, ComplexNumber z, int n) {
		if (n == 0)
			return 0;

		z.r = Math.sin(z.r);
		z = z.pow(exp).add(c);

		if (z.r * z.r + z.i * z.i >= 4.0)
			return 0;

		int steps = 1 + escapesSet(c, z, n - 1);
		plotPoint(z, steps, n);

		return steps;
	}

	public String getName() {
		return "flatworm";
	}
}
