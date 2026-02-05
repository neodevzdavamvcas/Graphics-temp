package rasterizers;

import models.Line;
import models.Point;
import rasters.Raster;

import java.awt.*;

public class TrivialRasterizer implements Rasterizer {

    private Color defaultColor;
    private Raster raster;

    public TrivialRasterizer(Color defaultColor, Raster raster) {
        this.defaultColor = defaultColor;
        this.raster = raster;
    }

    @Override
    public void setColor(Color color) {
        defaultColor = color;
    }

    @Override
    public void setRaster(Raster raster) {
        this.raster = raster;
    }

    @Override
    public void rasterize(Line line) {
        int color = (line.getColor() != null) ? line.getColor().getRGB() : defaultColor.getRGB();
        double k = CalculateK(line);
        double q = CalculateQ(line.getP1(), k);

        int skip = line.isDotted() ? 4 : 1;

        if (Math.abs(k) < 1) {
            if (line.getP1().getX() > line.getP2().getX()) {
                var temp = line.getP1();
                line.setP1(line.getP2());
                line.setP2(temp);
            }

            for (int x = line.getP1().getX(); x <= line.getP2().getX(); x += skip) {
                int y = (int)Math.round(k * x + q);

                raster.setPixel(x, y, color);
            }
        } else {
            if (line.getP1().getY() > line.getP2().getY()) {
                var temp = line.getP1();
                line.setP1(line.getP2());
                line.setP2(temp);
            }

            for (int y = line.getP1().getY(); y <= line.getP2().getY(); y += skip) {
                int x = (int)Math.round((y - q) / k);

                raster.setPixel(x, y, color);
            }
        }
    }

    private double CalculateK(Line line) {
        var a = (line.getP2().getY() - line.getP1().getY());
        var b = (line.getP2().getX() - line.getP1().getX());
        if (b == 0) return Integer.MAX_VALUE;

        return a / (double)b;
    }

    private double CalculateQ(Point p, double k) {
        return p.getY() - k * p.getX();
    }
}
