package fractal;

import graphics.ColorInterpolation;
import math.Vector;

import java.awt.*;

public class SierpinskiTriangle3D extends Fractal {
    private Vector rotation = new Vector(0.0, 0, 0.0);

    public SierpinskiTriangle3D() {
        posX = 0;
        posY = 0;
        n = 4;
    }

    public void draw(Graphics g) {
        rotation.x += 0.0001;
        rotation.y += 0.0002;
        rotation.z += 0.0005;

        Vector v1 = rotatePoint(new Vector((0.5) / zoom * pxSize, (1.0) / zoom * pxSize, 0.5 / zoom * pxSize));
        Vector v2 = rotatePoint(new Vector((1.0) / zoom * pxSize, (0.0) / zoom * pxSize,  0.0 / zoom * pxSize));
        Vector v3 = rotatePoint(new Vector((0.0) / zoom * pxSize, (0.0) / zoom * pxSize, 0.0 / zoom * pxSize));
        Vector v4 = rotatePoint(new Vector((0.5) / zoom * pxSize, (0.0) / zoom * pxSize, 1.0 / zoom * pxSize));

        v1 = v1.add(new Vector(-posX / zoom * pxSize, -posY / zoom * pxSize, 2));
        v2 = v2.add(new Vector(-posX / zoom * pxSize, -posY / zoom * pxSize, 2));
        v3 = v3.add(new Vector(-posX / zoom * pxSize, -posY / zoom * pxSize, 2));
        v4 = v4.add(new Vector(-posX / zoom * pxSize, -posY / zoom * pxSize, 2));

        g.setColor(ColorInterpolation.getGradientColor(colorPalette, 256, 128));
        drawRecursive(g, v1, v2, v3, v4, n);

    }

    private void drawRecursive(Graphics g, Vector v1, Vector v2, Vector v3, Vector v4, int n) {
        if (n == 0) {
            // Move to center to prevent wonky scaling near edges of screen
            Vector offset = new Vector(imageWidth / 2.0, imageHeight / 2.0, 0);

            v1 = v1.project().scale(imageSize).add(offset);
            v2 = v2.project().scale(imageSize).add(offset);
            v3 = v3.project().scale(imageSize).add(offset);
            v4 = v4.project().scale(imageSize).add(offset);

            g.drawLine((int) v1.x, (int) v1.y, (int) v2.x, (int) v2.y);
            g.drawLine((int) v2.x, (int) v2.y, (int) v3.x, (int) v3.y);
            g.drawLine((int) v3.x, (int) v3.y, (int) v1.x, (int) v1.y);

            g.drawLine((int) v1.x, (int) v1.y, (int) v4.x, (int) v4.y);
            g.drawLine((int) v2.x, (int) v2.y, (int) v4.x, (int) v4.y);
            g.drawLine((int) v3.x, (int) v3.y, (int) v4.x, (int) v4.y);
        } else {
            Vector i1 = v1.average(v2);
            Vector i2 = v2.average(v3);
            Vector i3 = v1.average(v3);
            Vector i4 = v2.average(v4);
            Vector i5 = v3.average(v4);
            Vector i6 = v1.average(v4);

            drawRecursive(g, v1, i1, i3, i6, n-1);
            drawRecursive(g, i1, v2, i2, i4, n-1);
            drawRecursive(g, i3, i2, v3, i5, n-1);
            drawRecursive(g, i6, v4, i4, i5, n-1);
        }
    }


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

    public String getName() {
        return "sierpinski 3d";
    }
}
