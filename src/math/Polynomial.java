package math;

import java.util.ArrayList;
import java.util.List;

public class Polynomial {
	private final List<Double> coef;

	public Polynomial(List<Double> coef) {
		this.coef = coef;
	}

	public Polynomial derivative() {
		List<Double> dCoef = new ArrayList<>();
		for (int i = 0; i < coef.size() - 1; i++) {
			dCoef.add((coef.size() - i - 2) * coef.get(i));
		}
		return  new Polynomial(dCoef);
	}

	public ComplexNumber evaluate(ComplexNumber z) {
		ComplexNumber result = new ComplexNumber(coef.get(coef.size() - 1), 0);
		for (int i = 0; i < coef.size() - 1; i++)
			result = result.add(z.pow(coef.size() - i - 1).scale(coef.get(i)));
		return result;
	}

	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < coef.size(); i++) {
			if (coef.get(i) == 0)
				continue;

			if (i != 0)
				out.append(coef.get(i) > 0 ? " + " : " - ");

			if (i == coef.size() - 1)
				out.append("%.4f".formatted(Math.abs(coef.get(i))));
			else
				out.append("%.4fx^%d".formatted(Math.abs(coef.get(i)), coef.size() - i - 1));
		}
		return out.toString();
	}
}
