package fractal;

import graphics.ColorInterpolation;
import math.ComplexNumber;

public class Astronaut extends Fractal {
	public Astronaut() {
		colorPalette = ColorInterpolation.ColorMode.RAINBOW;
	}

	public int escapesSet(ComplexNumber c, ComplexNumber z, int n) {
		if (n == 0)
			return 0;

		z.r = z.r * Math.pow(Math.E, z.r) / 2.0;
		z = z.pow(exp).conjugate().add(c);

		if (z.r * z.r + z.i * z.i >= 4.0)
			return 0;

		int steps = 1 + escapesSet(c, z, n - 1);
		plotPoint(z, steps, n);

		return steps;
	}

	public String getName() {
		return "astronaut";
	}
}
