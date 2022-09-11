package fractal;

import math.ComplexNumber;

public class BurningShipDrawer extends EscapeFractalDrawer {
	public int goesToInfinity(ComplexNumber c, ComplexNumber z, int n, int d) {
		if (n == 0)
			return 0;

		z = z.abs().pow(d).add(c);

		if (z.r * z.r + z.i * z.i >= 4.0)
			return 0;

		return 1 + goesToInfinity(c, z, n - 1, d);
	}

	public String toString() {
		return "[ Burning Ship ]";
	}
}