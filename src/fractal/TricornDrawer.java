package fractal;

import graphics.ColorInterpolation;
import math.ComplexNumber;

public class TricornDrawer extends Fractal {
	public TricornDrawer() {
		colorPalette = ColorInterpolation.ColorMode.BLACK;
	}

	public int escapesSet(ComplexNumber c, ComplexNumber z, int n) {
		if (n == 0)
			return 0;

		z = z.conjugate().pow(exp).add(c);

		if (z.r * z.r + z.i * z.i >= 4.0)
			return 0;

		int steps = 1 + escapesSet(c, z, n - 1);
		plotPoint(z, steps, n);

		return steps;
	}

	public String getName() {
		return "tricorn";
	}
}