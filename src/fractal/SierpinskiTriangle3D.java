package fractal;

import graphics.ColorInterpolation;
import graphics.DrawingPanel;
import math.Vector;

import java.awt.*;

public class SierpinskiTriangle3D extends Fractal {
    private final Vector rotation = new Vector(0.0, .5, 0.0);
    private final Vector light = new Vector(-1, -1.5, 0.5).normalized();

    private double[][] depthBuffer;

    public SierpinskiTriangle3D() {
        posX = 0;
        posY = 0;
        zoom = 0.3;

        n = 4;
        colorPalette = ColorInterpolation.ColorMode.PURPLE;
    }

    public void setDimensions(int width, int height) {
        super.setDimensions(width, height);

        depthBuffer = new double[height][width];

        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                depthBuffer[i][j] = Integer.MAX_VALUE;

    }

    public void draw(DrawingPanel d) {
        // Slowly rotate the pyramid
        rotation.x += 0.0004;
        rotation.y += 0.0007;
        rotation.z += 0.0015;

        // Get the initial triangle points
        Vector v1 = rotatePoint(new Vector(0.5, Math.sqrt(0.75), Math.sqrt(0.75) /2));
        Vector v2 = rotatePoint(new Vector(1.0, 0.0, 0.0));
        Vector v3 = rotatePoint(new Vector(0.0, 0.0, 0.0));
        Vector v4 = rotatePoint(new Vector(0.5, 0.0, Math.sqrt(0.75)));

        v1 = v1.add(new Vector(-posX, -posY, 3));
        v2 = v2.add(new Vector(-posX, -posY, 3));
        v3 = v3.add(new Vector(-posX, -posY, 3));
        v4 = v4.add(new Vector(-posX, -posY, 3));

        drawRecursive(d, v1, v2, v3, v4, n - 1);
    }

    private void drawRecursive(DrawingPanel d, Vector v1, Vector v2, Vector v3, Vector v4, int n) {
        if (n == 0) {
            // Move to center to prevent wonky scaling near edges of screen
            // Does make coordinates inaccurate, but that's okay
            Vector offset = new Vector(imageWidth / 2.0, imageHeight / 2.0, 0);

            Vector pv1 = v1.project().scale(imageSize / zoom).add(offset);
            Vector pv2 = v2.project().scale(imageSize / zoom).add(offset);
            Vector pv3 = v3.project().scale(imageSize / zoom).add(offset);
            Vector pv4 = v4.project().scale(imageSize / zoom).add(offset);

            // Set color depending on direction of light and triangle normal
            Color c = ColorInterpolation.getGradientColor(colorPalette, 256, 128 + (int) (128 * calculateNormal(v1, v2, v3).dot(light)));
            fillTriangle(d, c, pv1, pv2, pv3);
            c = ColorInterpolation.getGradientColor(colorPalette, 256, 128 + (int) (128 * calculateNormal(v1, v2, v4).dot(light)));
            fillTriangle(d, c, pv1, pv2, pv4);
            c = ColorInterpolation.getGradientColor(colorPalette, 256, 128 + (int) (128 * calculateNormal(v1, v3, v4).dot(light)));
            fillTriangle(d, c, pv1, pv3, pv4);
            c = ColorInterpolation.getGradientColor(colorPalette, 256, 128 + (int) (128 * calculateNormal(v4, v3, v2).dot(light)));
            fillTriangle(d, c, pv2, pv3, pv4);
        } else {
            Vector i1 = v1.average(v2);
            Vector i2 = v2.average(v3);
            Vector i3 = v1.average(v3);

            Vector i4 = v3.average(v4);
            Vector i5 = v1.average(v4);
            Vector i6 = v2.average(v4);

            drawRecursive(d, i1, v2, i2, i6, n-1);
            drawRecursive(d, i3, i2, v3, i4, n-1);
            drawRecursive(d, v1, i1, i3, i5, n-1);
            drawRecursive(d, i5, i6, i4, v4, n-1);
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
        double dz1 = p2.z - p1.z, dz2 = p3.z - p1.z;

        // Calculate the slope of each line
        double xStep1 = 0, xStep2 = 0, zStep1 = 0, zStep2 = 0;

        if (dy1 != 0) xStep1 = dx1 / Math.abs(dy1);
        if (dy1 != 0) zStep1 = dz1 / Math.abs(dy1);

        if (dy2 != 0) xStep2 = dx2 / Math.abs(dy2);
        if (dy2 != 0) zStep2 = dz2 / Math.abs(dy2);

        // Iterate through each scan line and draw a horizontal line using the calculated start and end points for x, z and UV
        // Stop after reaching the end of the p1 -> p2 line, as this is where the triangle is no longer bottom flat
        for (double y = p1.y; y < p2.y; y += 1) {
            double x1 = p1.x + xStep1 * (y - p1.y);
            double x2 = p1.x + xStep2 * (y - p1.y);

            double z1 = p1.z + zStep1 * (y - p1.y);
            double z2 = p1.z + zStep2 * (y - p1.y);

            if (x2 < x1) {
                double temp = x1; x1 = x2; x2 = temp;
                temp = z1; z1 = z2; z2 = temp;
            }

            for (double x = x1; x < x2; x += 1) {
                double progress = (x - x1) / (x2 - x1);
                double z = z1 + (z2 - z1) * progress;

                if (x < 0 || y < 0 || y >= depthBuffer.length || x  >= depthBuffer[0].length || depthBuffer[(int) y][(int) x] <= z)
                    continue;

                depthBuffer[(int) y][(int) x] = z;

                d.drawPixel((int) x, (int) y, c);
            }
        }

        // Repeat the process for the other line to draw a top flat triangle

        dx1 = p3.x - p2.x;
        dy1 = p3.y - p2.y;
        dz1 = p3.z - p2.z;

        xStep1 = 0; zStep1 = 0;

        if (dy1 != 0) xStep1 = dx1 / Math.abs(dy1);
        if (dy1 != 0) zStep1 = dz1 / Math.abs(dy1);

        for (double y = p2.y; y < p3.y; y += 1) {
            double x1 = p2.x + xStep1 * (y - p2.y);
            double x2 = p1.x + xStep2 * (y - p1.y);

            double z1 = p2.z + zStep1 * (y - p2.y);
            double z2 = p1.z + zStep2 * (y - p1.y);

            if (x2 < x1) {
                double temp = x1; x1 = x2; x2 = temp;
                temp = z1; z1 = z2; z2 = temp;
            }

            for (double x = x1; x < x2; x += 1) {
                double progress = (x - x1) / (x2 - x1);
                double z = z1 + (z2 - z1) * progress;

                if (x < 0 || y < 0 || y >= depthBuffer.length || x  >= depthBuffer[0].length || depthBuffer[(int) y][(int) x] <= z)
                    continue;

                depthBuffer[(int) y][(int) x] = z;

                d.drawPixel((int) x, (int) y, c);
            }
        }
    }

    // Apply rotation matrix to a point
    private Vector rotatePoint(Vector pt) {
        Vector rPt = new Vector(pt.x, pt.y, pt.z);
        double rotY = rPt.y * Math.cos(rotation.x) + rPt.z * Math.sin(rotation.x);
        double rotZ = rPt.y * -1 * Math.sin(rotation.x) + rPt.z * Math.cos(rotation.x);

        rPt.z = rotZ;
        rPt.y = rotY;

        double rotX = rPt.x * Math.cos(rotation.y) + rPt.z * -1 * Math.sin(rotation.y);
        rotZ = rPt.x * Math.sin(rotation.y) + rPt.z * Math.cos(rotation.y);

        rPt.x = rotX;
        rPt.z = rotZ;

        rotX = rPt.x * Math.cos(rotation.z) + rPt.y * Math.sin(rotation.z);
        rotY = rPt.x * -1 * Math.sin(rotation.z) + rPt.y * Math.cos(rotation.z);

        rPt.x = rotX;
        rPt.y = rotY;
        return rPt;
    }

    // Find the normal of a triangle given 3 points
    public static Vector calculateNormal(Vector p1, Vector p2, Vector p3) {
        Vector u = p2.subtract(p1);
        Vector v = p3.subtract(p2);
        return new Vector((u.y * v.z) - (u.z * v.y), (u.z * v.x) - (u.x * v.z), (u.x * v.y) - (u.y * v.x)).normalized();
    }

    public String getName() {
        return "sierpinski 3d";
    }
}
