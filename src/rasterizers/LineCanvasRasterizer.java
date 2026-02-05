package rasterizers;

import models.LineCanvas;

public class LineCanvasRasterizer {
    private final Rasterizer lineRasterizer;

    public LineCanvasRasterizer(Rasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }

    public void rasterizeCanvas(LineCanvas lineCanvas) {
        lineCanvas.getLines().forEach(lineRasterizer::rasterize);
    }
}
