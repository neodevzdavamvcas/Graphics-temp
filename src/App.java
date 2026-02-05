import utils.Config;
import models.Line;
import models.LineCanvas;
import org.jetbrains.annotations.Nullable;
import rasterizers.LineCanvasRasterizer;
import rasterizers.Rasterizer;
import rasterizers.TrivialRasterizer;
import rasters.Raster;
import models.Point;
import rasters.RasterBufferedImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serial;
import java.util.HashSet;

public class App {
    private final JPanel panel;
    private final Raster raster;
    private MouseAdapter mouseAdapter;
    private KeyAdapter keyAdapter;
    private Point initialMouse;
    private @Nullable Point currentMouse = null;
    private final Rasterizer rasterizer;
    private final LineCanvas lineCanvas;
    private final LineCanvasRasterizer lineCanvasRasterizer;
    private final HashSet<Integer> keysHeld = new HashSet<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App(Config.windowSize.x, Config.windowSize.y).start());
    }

    public void clear() {
        raster.clear();
    }

    public void present(Graphics graphics) {
        raster.repaint(graphics);
    }

    public void start() {
        clear();
        panel.repaint();
    }

    public App(int width, int height) {
        JFrame frame = new JFrame();

        frame.setLayout(new BorderLayout());

        frame.setTitle("Delta : " + this.getClass().getName());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        raster = new RasterBufferedImage(width, height);
        raster.setClearColor(0xaaaaaa);

        panel = new JPanel() {
            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                present(g);
            }
        };
        panel.setPreferredSize(new Dimension(width, height));

        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        panel.requestFocus();
        panel.requestFocusInWindow();

        CreateAdapters();
        rasterizer = new TrivialRasterizer(Color.CYAN, raster);
        lineCanvas = new LineCanvas();
        lineCanvasRasterizer = new LineCanvasRasterizer(rasterizer);
        panel.addMouseMotionListener(mouseAdapter);
        panel.addMouseListener(mouseAdapter);
        panel.addKeyListener(keyAdapter);
    }

    private void CreateAdapters() {
        mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                initialMouse = new Point(e.getX(), e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                drawLine(new MyMouseEvent(e), true);
                currentMouse = null;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                drawLine(new MyMouseEvent(e));
            }
        };
        keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keysHeld.add(e.getKeyCode());
                if (currentMouse == null) return;
                drawLine(null);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keysHeld.remove(e.getKeyCode());
                if (currentMouse == null) return;
                drawLine(null);
            }

            @Override
            public void keyTyped(KeyEvent e) {
                if (Character.toLowerCase(e.getKeyChar()) == 'c') {
                    lineCanvas.clear();
                    clear();
                    panel.repaint();
                }
            }
        };
    }

    private void drawLine(@Nullable MyMouseEvent e) {
        drawLine(e, false);
    }

    private void drawLine(@Nullable MyMouseEvent e, boolean handleReleased) {
        if (e != null) currentMouse = new Point(e.X, e.Y);
        if (currentMouse == null) return;
        Line line = new Line(new Point(initialMouse), new Point(currentMouse), keysHeld.contains(KeyEvent.VK_CONTROL), keysHeld.contains(KeyEvent.VK_SHIFT));
        if (handleReleased) lineCanvas.addLine(line);

        clear();
        lineCanvasRasterizer.rasterizeCanvas(lineCanvas);
        if (!handleReleased) rasterizer.rasterize(line);
        panel.repaint();
    }

    private static class MyMouseEvent {
        public int X;
        public int Y;

        public MyMouseEvent(MouseEvent mouseEvent) {
            X = Math.clamp(mouseEvent.getX(), 0, Config.windowSize.x - 1);
            Y = Math.clamp(mouseEvent.getY(), 0, Config.windowSize.y - 1);
        }
    }
}
