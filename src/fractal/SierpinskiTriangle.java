package fractal;

import graphics.ColorInterpolation;
import graphics.DrawingPanel;
import math.Vector;

import java.awt.*;

public class SierpinskiTriangle extends Fractal {

    public SierpinskiTriangle() {
        posX = -2;
        posY = -2;
        n = 4;
        colorPalette = ColorInterpolation.ColorMode.PURPLE;
    }

    public void draw(DrawingPanel d) {
        drawRecursive(d,
                new Vector((0.5 - posX) / zoom * pxSize, (-Math.sqrt(0.75) - posY) / zoom * pxSize),
                new Vector((1.0 - posX) / zoom * pxSize, (0.0 - posY) / zoom * pxSize),
                new Vector((0.0 - posX) / zoom * pxSize, (0.0 - posY) / zoom * pxSize), n);

    }

    private void drawRecursive(DrawingPanel d, Vector v1, Vector v2, Vector v3, int n) {
        if (n == 0) {
            v1 = v1.scale(imageSize); v2 = v2.scale(imageSize); v3 = v3.scale(imageSize);
            fillTriangle(d, ColorInterpolation.getGradientColor(colorPalette, 256, 128), v1, v2, v3);
        } else {
            Vector i1 = v1.average(v2);
            Vector i2 = v3.average(v1);
            Vector i3 = v2.average(v3);

            drawRecursive(d, v1, i1, i2, n-1);
            drawRecursive(d, i1, i3, v2, n-1);
            drawRecursive(d, i2, i3, v3, n-1);
        }
    }


    // Taken from my 3D engine
    public void fillTriangle(DrawingPanel d, Color c, Vector p1, Vector p2, Vector p3) {
        // Sort points by y order with bubble sort
        if (p1.y > p2.y) {
            Vector temp = p1; p1 = p2; p2 = temp;
        }
        if (p2.y > p3.y) {
            Vector temp = p2; p2 = p3; p3 = temp;
        }
        if (p1.y > p2.y) {
            Vector temp = p1; p1 = p2; p2 = temp;
        }

        // Snap points to pixel space
        p1.x = (int) p1.x; p1.y = (int) p1.y;
        p2.x = (int) p2.x; p2.y = (int) p2.y;
        p3.x = (int) p3.x; p3.y = (int) p3.y;

        // Calculate starting and end points for triangle lines in screen and texture space
        double dx1 = p2.x - p1.x, dx2 = p3.x - p1.x;
        double dy1 = p2.y - p1.y, dy2 = p3.y - p1.y;

        // Calculate the slope of each line
        double xStep1 = 0, xStep2 = 0;
        if (dy1 != 0) xStep1 = dx1 / Math.abs(dy1);

        if (dy2 != 0) xStep2 = dx2 / Math.abs(dy2);

        // Iterate through each scan line and draw a horizontal line using the calculated start and end points for x, z and UV
        // Stop after reaching the end of the p1 -> p2 line, as this is where the triangle is no longer bottom flat
        for (double y = p1.y; y < p2.y; y += 1) {
            double x1 = p1.x + xStep1 * (y - p1.y);
            double x2 = p1.x + xStep2 * (y - p1.y);

            if (x2 < x1) {
                double temp = x1; x1 = x2; x2 = temp;
            }

            for (double x = x1; x < x2; x += 1)
                d.drawPixel((int) x, (int) y, c);
        }

        // Repeat the process for the other line to draw a top flat triangle

        dx1 = p3.x - p2.x;
        dy1 = p3.y - p2.y;

        xStep1 = 0;

        if (dy1 != 0) xStep1 = dx1 / Math.abs(dy1);

        for (double y = p2.y; y < p3.y; y += 1) {
            double x1 = p2.x + xStep1 * (y - p2.y);
            double x2 = p1.x + xStep2 * (y - p1.y);

            if (x2 < x1) {
                double temp = x1; x1 = x2; x2 = temp;
            }

            for (double x = x1; x < x2; x += 1)
                d.drawPixel((int) x, (int) y, c);
        }
    }

    public String getName() {
        return "sierpinski";
    }
}
