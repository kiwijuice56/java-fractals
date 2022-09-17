package graphics;

import fractal.Fractal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;

// Contains button interface and controls which fractal is active
public class DrawingPanel extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener, KeyListener {
	protected Fractal current;
	private static final Font FONT = new Font("Consolas", Font.PLAIN, 12);

	// Toggles to keep status of state
	private boolean shiftClicked = false, ctrlClicked = false, altClicked = false, hideText = false, lockMouse = false;

	// Used to keep track of mouse clicks and drags for scrolling
	private double initialX = 0, initialY = 0, offsetX, offsetY;

	private byte[] out;

	public DrawingPanel() {
		addMouseMotionListener(this);
		addMouseListener(this);
		addMouseWheelListener(this);
		addKeyListener(this);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		out = new byte[(getWidth() * 3) * (getHeight() * 3)];

		current.setDimensions(getWidth(), getHeight());
		current.draw(this);

		BufferedImage outImage = new BufferedImage(getWidth(), getHeight(),  BufferedImage.TYPE_3BYTE_BGR);
		outImage.setData(Raster.createRaster(outImage.getSampleModel(), new DataBufferByte(out, out.length), new Point()));
		g.drawImage(outImage, 0, 0, null);

		if (hideText)
			return;

		g.setColor(new Color(0, 0, 0, 190));
		g.fillRect(0, 0, 228, 410);

		g.setColor(Color.WHITE);
		g.setFont(FONT);

		String out = current.toString();
		int lineStart = 16;
		for (String line : out.split("\n")) {
			g.drawString(line, 16, lineStart);
			lineStart += 16;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		double dragSpeed = 0.05;
		double dragX = current.zoom * dragSpeed * (initialX - (double) e.getX() / getWidth());
		double dragY = current.zoom * dragSpeed * (initialY - (double) e.getY() / getHeight());

		offsetX += dragX;
		offsetY += dragY;

		// Resets dragging origin when too far to prevent slippery navigation
		if (Math.abs(offsetX) > current.zoom * 0.5 || Math.abs(offsetY) > current.zoom * 0.5) {
			initialX = (double) e.getX() / getWidth();
			initialY = (double) e.getY() / getHeight();
		}

		current.posX += dragX;
		current.posY += dragY;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		initialX = e.getX() / (double) getWidth();
		initialY = e.getY() / (double) getHeight();
		offsetX = 0; offsetY = 0;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (shiftClicked) {
			current.n -= e.getWheelRotation();
			current.n = Math.max(1, current.n);
		} else if (ctrlClicked) {
			current.pxSize -= e.getWheelRotation();
			current.pxSize = Math.max(1, current.pxSize);
		} else if (altClicked) {
			current.exp -= e.getWheelRotation();
			current.exp = Math.max(2, current.exp);
		} else {
			// Shift to keep zoom in the center
			double imageSize = Math.min(getWidth(), getHeight());
			current.posX -= Math.signum(e.getWheelRotation()) * getWidth() / imageSize * 0.1 * current.zoom / 2;
			current.posY -= Math.signum(e.getWheelRotation()) * getHeight() / imageSize * 0.1 * current.zoom / 2;
			// Change zoom by 10% in either direction
			current.zoom *= e.getWheelRotation() > 0 ? 1.1 : 0.9;
		}

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (lockMouse)
			return;
		double imageWidth = Math.min(getWidth(), getHeight());
		current.mouseX = current.posX + current.zoom * (double) e.getX() / imageWidth;
		current.mouseY = current.posY + current.zoom * (double) e.getY() / imageWidth;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case 16 -> shiftClicked = true;
			case 17 -> ctrlClicked = true;
			case 18 -> altClicked = true;
			// Toggles
			case 76 -> lockMouse = !lockMouse;
			case 77 -> current.useMousePos = !current.useMousePos;
			case 72 -> hideText = !hideText;
			// Keys for draw modes
			case 81 -> current.currentDrawMode = Fractal.DrawMode.NORMAL;
			case 87 -> current.currentDrawMode = Fractal.DrawMode.PLOT;
			case 69 -> current.currentDrawMode = Fractal.DrawMode.PLOT_INVERSE;
			// Keys for color palettes
			case 48 -> { ColorInterpolation.randomizePalette(); current.colorPalette = ColorInterpolation.ColorMode.RANDOM; }
			case 49 -> current.colorPalette = ColorInterpolation.ColorMode.PURPLE;
			case 50 -> current.colorPalette = ColorInterpolation.ColorMode.RAINBOW;
			case 51 -> current.colorPalette = ColorInterpolation.ColorMode.WHITE;
			case 52 -> current.colorPalette = ColorInterpolation.ColorMode.BLACK;
			case 53 -> current.colorPalette = ColorInterpolation.ColorMode.CANDY;
			case 54 -> current.colorPalette = ColorInterpolation.ColorMode.ROYAL;
			case 55 -> current.colorPalette = ColorInterpolation.ColorMode.STAT;
			case 56 -> current.colorPalette = ColorInterpolation.ColorMode.FROST;
			case 57 -> current.colorPalette = ColorInterpolation.ColorMode.TREE;

		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
			case 16 -> shiftClicked = false;
			case 17 -> ctrlClicked = false;
			case 18 -> altClicked = false;
		}
	}
	public void mouseClicked(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void keyTyped(KeyEvent e) {}

	public void drawPixel(int x, int y, Color c) {
		if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight())
			return;
		int idx = (y) * (getWidth() * 3) + (x * 3);
		out[idx] = (byte) c.getBlue();
		out[idx + 1] = (byte) c.getGreen();
		out[idx + 2] = (byte) c.getRed();
	}
}