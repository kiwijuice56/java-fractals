package math;

public class ComplexNumber {
	public double r, i;

	public ComplexNumber(double r, double i) {
		this.r = r;
		this.i = i;
	}

	public ComplexNumber add(ComplexNumber other) {
		return new ComplexNumber(r + other.r, i + other.i);
	}

	public ComplexNumber subtract(ComplexNumber other) {
		return new ComplexNumber(r - other.r, i - other.i);
	}

	public ComplexNumber multiply(ComplexNumber other) {
		return new ComplexNumber(r * other.r - i * other.i,  r * other.i + other.r * i);
	}

	public ComplexNumber scale(double x) {
		return new ComplexNumber(r * x, i * x);
	}

	public ComplexNumber divide(ComplexNumber other) {
		ComplexNumber numerator = multiply(other.conjugate());
		numerator.r /= other.r * other.r + other.i * other.i;
		numerator.i /= other.r * other.r + other.i * other.i;
		return numerator;
	}

	public ComplexNumber pow(int n) {
		if (n == 1)
			return new ComplexNumber(r, i);
		return multiply(pow(n-1));
	}

	public ComplexNumber abs() {
		return new ComplexNumber(Math.abs(r), Math.abs(i));
	}

	public ComplexNumber conjugate() {
		return new ComplexNumber(r, -i);
	}

	@Override
	public String toString() {
		return r + (i < 0 ?  " - " : " + ") + "%.4fi".formatted(Math.abs(i));
	}
}
