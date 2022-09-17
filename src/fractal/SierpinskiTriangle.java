package fractal;

import graphics.ColorInterpolation;
import math.Vector;

import java.awt.*;

public class SierpinskiTriangle extends Fractal {

    public SierpinskiTriangle() {
        posX = -2;
        posY = -2;
        n = 4;
        colorPalette = ColorInterpolation.ColorMode.PURPLE;
    }

    public void draw(Graphics g) {
        g.setColor(ColorInterpolation.getGradientColor(colorPalette, 256, 128));
        drawRecursive(g,
                new Vector((0.5 - posX) / zoom * pxSize, (-Math.sqrt(0.75) - posY) / zoom * pxSize),
                new Vector((1.0 - posX) / zoom * pxSize, (0.0 - posY) / zoom * pxSize),
                new Vector((0.0 - posX) / zoom * pxSize, (0.0 - posY) / zoom * pxSize), n);

    }

    private void drawRecursive(Graphics g, Vector v1, Vector v2, Vector v3, int n) {
        if (n == 0) {
            v1 = v1.scale(imageSize); v2 = v2.scale(imageSize); v3 = v3.scale(imageSize);
            g.fillPolygon(new int[] {(int) v1.x, (int) v2.x, (int) v3.x}, new int[] {(int) v1.y, (int) v2.y, (int) v3.y}, 3);
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
