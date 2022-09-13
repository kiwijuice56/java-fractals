package fractal;

import graphics.ColorInterpolation;
import math.ComplexNumber;

import java.awt.*;

public class Fractal {
	// Due to the small scale of the program, most variables are public for convenience

	// Used to position and scale fractals
	public double posX, posY, zoom = 3.5;
	public double mouseX = 0, mouseY = 0;

	public int n = 12, pxSize = 2, exp = 2;

	public boolean useMousePos = false;

	public enum DrawMode { NORMAL, PLOT, PLOT_INVERSE }
	public DrawMode currentDrawMode = DrawMode.NORMAL;

	// stores data for plots
	protected int[][] visitedCount;
	protected int highestVisitCount = 0;
	protected int plottedPoints = 500000;

	protected int imageWidth, imageHeight;
	protected double imageSize;

	public ColorInterpolation.ColorMode colorPalette = ColorInterpolation.ColorMode.ROYAL;

	public Fractal() {
		posX = -2.5;
		posY = -2.0;
	}

	public void draw(Graphics g) {
		if (currentDrawMode == DrawMode.NORMAL)
			drawNormal(g);
		else
			drawProbabilityPlot(g);
	}

	private void drawNormal(Graphics g) {
		for (int i = 0; i < imageHeight / pxSize; i++) {
			for (int j = 0; j < imageWidth / pxSize; j++) {
				double y = posY + zoom * i / imageSize;
				double x = posX + zoom * j / imageSize;

				int steps = escapesSet(new ComplexNumber(useMousePos ? mouseX : x, useMousePos ? mouseY : y), new ComplexNumber(x, y), n);
				if (steps == n)
					continue;
				if (currentDrawMode == DrawMode.NORMAL) {
					g.setColor(ColorInterpolation.getGradientColor(colorPalette, n, steps));
					g.fillRect(j * pxSize, i * pxSize, pxSize, pxSize);
				}
			}
		}
	}

	private void drawProbabilityPlot(Graphics g) {
		highestVisitCount = 0;
		visitedCount = new int[imageHeight][imageWidth];
		for (int p = 0; p < plottedPoints; p++) {
			double x = Math.random() * 4 - 2;
			double y = Math.random() * 4 - 2;

			escapesSet(new ComplexNumber(useMousePos ? mouseX : x, useMousePos ? mouseY : y), new ComplexNumber(x, y), n);
		}

		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				g.setColor(ColorInterpolation.getGradientColor(colorPalette, highestVisitCount, visitedCount[i][j]));
				g.fillRect(j * pxSize, i * pxSize, pxSize, pxSize);
			}
		}
	}

	/**
	 * Recursive function to check if the fractal goes to infinity.
	 * Will also plot points along the way, depending on the DrawingMode
	 */
	public int escapesSet(ComplexNumber c, ComplexNumber z, int n) {
		if (n == 0)
			return 0;

		z = z.pow(exp).add(c);

		if (z.r * z.r + z.i * z.i >= 4.0)
			return 0;

		int steps =  1 + escapesSet(c, z, n - 1);
		plotPoint(z, steps, n);

		return steps;
	}

	public void plotPoint(ComplexNumber z, int steps, int n) {
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
		return "";
	}

	public String toString() {
		return 	"[location]\n"+
				"(%.6f, %.6fi)\n".formatted(posX + (imageWidth / imageSize / pxSize / 2) * zoom,- (posY + (imageHeight / imageSize / pxSize / 2) * zoom)) +
				"%.6f scale\n".formatted(zoom) +
				"\n[fractal]\n"+
				getName() + "\n" +
				"n = %d, power = %d\n".formatted(n, exp) +
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
				"1 - 8: palette\n" +
				"Q W E keys: render mode\n" +
				"M: use mouse\n" +
				"L: lock mouse\n" +
				"H: hide help";
	}
}