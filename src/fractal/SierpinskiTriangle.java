package fractal;

import graphics.ColorInterpolation;
import math.Vector;

import java.awt.*;

public class SierpinskiTriangle extends Fractal {

    public SierpinskiTriangle() {
        posX = -2;
        posY = -2;
        n = 4;
    }

    public void draw(Graphics g) {
        g.setColor(ColorInterpolation.getGradientColor(colorPalette, 256, 128));
        drawRecursive(g,
                new Vector((0.5 - posX) / zoom * pxSize, (1.0 - posY) / zoom * pxSize, 0),
                new Vector((1.0 - posX) / zoom * pxSize, (0.0 - posY) / zoom * pxSize, 0),
                new Vector((0.0 - posX) / zoom * pxSize, (0.0 - posY) / zoom * pxSize, 0), n);

    }

    private void drawRecursive(Graphics g, Vector v1, Vector v2, Vector v3, int n) {
        if (n == 0) {
            g.drawLine(
                    (int) (v1.x * imageSize), (int) (v1.y * imageSize),
                    (int) (v2.x * imageSize), (int) (v2.y * imageSize));
            g.drawLine(
                    (int) (v2.x * imageSize), (int) (v2.y * imageSize),
                    (int) (v3.x * imageSize), (int) (v3.y * imageSize));
            g.drawLine(
                    (int) (v3.x * imageSize), (int) (v3.y * imageSize),
                    (int) (v1.x * imageSize), (int) (v1.y * imageSize));
        } else {
            Vector i1 = v1.average(v2);
            Vector i2 = v3.average(v1);
            Vector i3 = v2.average(v3);

            drawRecursive(g, v1, i1, i2, n-1);
            drawRecursive(g, i1, i3, v2, n-1);
            drawRecursive(g, i2, i3, v3, n-1);
        }
    }

    public String getName() {
        return "sierpinski";
    }
}
