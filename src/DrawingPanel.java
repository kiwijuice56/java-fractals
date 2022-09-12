import fractal.EscapeFractalDrawer;
import fractal.FractalDrawer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Contains button interface and controls which fractal is active
public class DrawingPanel extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener, KeyListener {
	protected FractalDrawer current;
	private boolean shiftClicked = false, ctrlClicked = false, altClicked = false;
	// Used to keep track of mouse clicks and drags for scrolling
	private double initialX = 0, initialY = 0, offsetX, offsetY;

	// Connected text outputs
	private JLabel positionLabel, zoomLabel, nLabel, dLabel, resLabel;

	public DrawingPanel() {
		addMouseMotionListener(this);
		addMouseListener(this);
		addMouseWheelListener(this);
		addKeyListener(this);
	}

	public void connect(JLabel positionLabel, JLabel zoomLabel, JLabel nLabel, JLabel dLabel, JLabel resLabel) {
		this.positionLabel = positionLabel;
		positionLabel.setText("(%.4f, %.4f), ".formatted(current.cornerX, current.cornerY));
		this.zoomLabel = zoomLabel;
		zoomLabel.setText("scale = %.4f, ".formatted(current.zoom));
		this.nLabel = nLabel;
		nLabel.setText("n = %d, ".formatted(current.n));
		this.dLabel = dLabel;
		dLabel.setText("d = %d, ".formatted(current.d));
		this.resLabel = resLabel;
		resLabel.setText("1 render px : %d px".formatted(current.resolution));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		current.draw(g, getWidth(), getHeight());
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

		current.cornerX += dragX;
		current.cornerY += dragY;

		if (positionLabel != null)
			positionLabel.setText("(%.4f, %.4f), ".formatted(current.cornerX, current.cornerY));
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
			if (nLabel != null)
				nLabel.setText("n = %d, ".formatted(current.n));
		} else if (ctrlClicked) {
			current.resolution -= e.getWheelRotation();
			current.resolution = Math.max(1, current.resolution);
			if (resLabel != null)
				resLabel.setText("1 render px : %d px".formatted(current.resolution));
		} else if (altClicked) {
			current.d -= e.getWheelRotation();
			current.d = Math.max(2, current.d);
			if (dLabel != null)
				dLabel.setText("d = %d, ".formatted(current.d));
		} else {
			// Shift to keep zoom in the center
			current.cornerX -= Math.signum(e.getWheelRotation()) * getWidth() / getHeight() * 0.1 * current.zoom / 2;
			current.cornerY -= Math.signum(e.getWheelRotation()) * 0.1 * current.zoom / 2;
			// Change zoom by 10% in either direction
			current.zoom *= e.getWheelRotation() > 0 ? 1.1 : 0.9;
			if (zoomLabel != null)
				zoomLabel.setText("scale = %.4f, ".formatted(current.zoom));
			if (positionLabel != null)
				positionLabel.setText("(%.4f, %.4f), ".formatted(current.cornerX, current.cornerY));
		}

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		double imageWidth = Math.min(getWidth(), getHeight());
		current.mouseX = current.cornerX + current.zoom * (double) e.getX() / imageWidth;
		current.mouseY = current.cornerY + current.zoom * (double) e.getY() / imageWidth;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case 16 -> shiftClicked = true;
			case 17 -> ctrlClicked = true;
			case 18 -> altClicked = true;
			case 32 -> {
				if (current instanceof EscapeFractalDrawer)
					current.julia = !current.julia;
			}
			case 49 -> current.currentMode = FractalDrawer.ColorMode.PURPLE;
			case 50 -> current.currentMode = FractalDrawer.ColorMode.RAINBOW;
			case 51 -> current.currentMode = FractalDrawer.ColorMode.WHITE;
			case 52 -> current.currentMode = FractalDrawer.ColorMode.BLACK;
			case 53 -> current.currentMode = FractalDrawer.ColorMode.CANDY;
			case 54 -> current.currentMode = FractalDrawer.ColorMode.ROYAL;
			case 55 -> current.currentMode = FractalDrawer.ColorMode.STAT;
			case 56 -> { current.randomizePalette(); current.currentMode = FractalDrawer.ColorMode.RANDOM; }
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
}