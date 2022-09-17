package fractal;

import graphics.ColorInterpolation;
import graphics.DrawingPanel;
import math.ComplexNumber;

public class Fractal {
	// Due to the small scale of the program, most variables are public/protected for convenience

	// Used to position and scale fractals
	public double posX = -3.0, posY = -2.5, zoom = 4.5;
	public double mouseX = 0, mouseY = 0;

	public int n = 12, pxSize = 1, exp = 2;

	public boolean useMousePos = false;

	public enum DrawMode { NORMAL, PLOT, PLOT_INVERSE }
	public DrawMode currentDrawMode = DrawMode.NORMAL;

	// Stores data to create a histogram colorization of the plots
	private int[][] visitedCount;
	private int highestVisitCount = 0;
	private static final int POINT_PLOT_COUNT = 500000;

	// Stores the dimensions of the fractal drawing window
	protected int imageWidth, imageHeight;
	protected double imageSize;

	public ColorInterpolation.ColorMode colorPalette = ColorInterpolation.ColorMode.ROYAL;

	// Called by external panels to paint the fractal
	public void draw(DrawingPanel d) {
		if (currentDrawMode == DrawMode.NORMAL)
			drawNormal(d);
		else
			drawProbabilityPlot(d);
	}

	// Normal rendering method that iterates through each px and uses the # steps to escape the set for coloring
	private void drawNormal(DrawingPanel d) {
		for (int i = 0; i < imageHeight / pxSize; i++) {
			for (int j = 0; j < imageWidth / pxSize; j++) {
				double y = posY + zoom * i / imageSize;
				double x = posX + zoom * j / imageSize;

				int steps = escapesSet(new ComplexNumber(useMousePos ? mouseX : x, useMousePos ? mouseY : y), new ComplexNumber(x, y), n);
				// Don't color steps that did not escape
				if (steps == n)
					continue;
				d.drawPixel(j * pxSize, i * pxSize, ColorInterpolation.getGradientColor(colorPalette, n, steps));
			}
		}
	}

	// Plot rendering method that takes a random sample of points and plots their trajectories
	private void drawProbabilityPlot(DrawingPanel d) {
		highestVisitCount = 0;
		visitedCount = new int[imageHeight][imageWidth];
		for (int p = 0; p < POINT_PLOT_COUNT; p++) {
			double x = Math.random() * 4 - 2;
			double y = Math.random() * 4 - 2;

			escapesSet(new ComplexNumber(useMousePos ? mouseX : x, useMousePos ? mouseY : y), new ComplexNumber(x, y), n);
		}

		for (int i = 0; i < imageHeight; i++)
			for (int j = 0; j < imageWidth; j++)
				d.drawPixel(j * pxSize, i * pxSize, ColorInterpolation.getGradientColor(colorPalette, highestVisitCount, visitedCount[i][j]));
	}

	// Recursive function to check if the fractal goes to infinity
	// Mandelbrot by default
	// Will also plot points along the way, depending on the DrawingMode
	protected int escapesSet(ComplexNumber c, ComplexNumber z, int n) {
		if (n == 0)
			return 0;

		z = z.pow(exp).add(c);

		if (z.r * z.r + z.i * z.i >= 4.0)
			return 0;

		int steps =  1 + escapesSet(c, z, n - 1);
		plotPoint(z, steps, n);

		return steps;
	}

	// Checks the drawing mode and adds point to the histogram plot if the point does/does not escape the set (depends on mode)
	protected void plotPoint(ComplexNumber z, int steps, int n) {
		if (	(steps != n && currentDrawMode == DrawMode.PLOT) ||
				(steps == n && currentDrawMode == DrawMode.PLOT_INVERSE)) {
			int pX = (int) (((z.r - posX) * imageSize) / zoom);
			int pY = (int) (((z.i - posY) * imageSize) / zoom);

			if (pX < 0 || pY < 0 || pY >= visitedCount.length || pX >= visitedCount[pY].length)
				return;

			visitedCount[pY][pX]++;
			highestVisitCount = Math.max(highestVisitCount, visitedCount[pY][pX]);
		}
	}

	public void setDimensions(int width, int height) {
		this.imageWidth = width;
		this.imageHeight = height;
		this.imageSize = Math.min(imageHeight, imageWidth) / (double) pxSize;
	}

	public String getName() {
		return "Fractal";
	}

	// Returns a document of information about the fractal settings
	public String toString() {
		return 	"[location]\n"+
				"(%.6f, %.6fi)\n".formatted(posX + (imageWidth / imageSize / pxSize / 2) * zoom,- (posY + (imageHeight / imageSize / pxSize / 2) * zoom)) +
				"%.6f scale\n".formatted(zoom) +
				"\n[fractal]\n"+
				getName() + "\n" +
				"n = %d\n".formatted(n) +
				"power = %d\n".formatted(exp) +
				"\n[render]\n"+
				"mode: %s\n".formatted(currentDrawMode.name()) +
				"palette: %s\n".formatted(colorPalette.name()) +
				"pixel size = %d\n".formatted(pxSize) +
				"use mouse ? = %s\n".formatted(useMousePos) +
				"\n[controls]\n" +
				"wheel & left drag: navigation\n" +
				"wheel + ctrl: pixel size\n" +
				"wheel + alt: power\n" +
				"wheel + shift: n\n" +
				"0 - 9: palette\n" +
				"Q/W/E: render mode\n" +
				"M: use mouse\n" +
				"L: lock mouse\n" +
				"H: hide help";
	}
}