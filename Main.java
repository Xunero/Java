import javax.swing.*;
import java.awt.*;
import java.util.concurrent.*;

class Animation extends JPanel {
    private final int width;
    private final int height;
    private final ScheduledExecutorService executorService;
    private final int priority;
    private final int maxIterations = 1000;
    private double moveX = -0.5;
    private double moveY = 0;
    private double zoom = 150;
    private int offsetX = 0;
    private int direction = 1;

    public Animation(int width, int height, int priority) {
        this.width = width;
        this.height = height;
        this.priority = priority;
        executorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMandelbrot(g);
    }

    private void drawMandelbrot(Graphics g) {
        double zx, zy, cX, cY, tmp;
        double escapeRadius = 2;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                zx = zy = 0;
                cX = (x - width / 2) / zoom + moveX;
                cY = (y - height / 2) / zoom + moveY;
                int iter = maxIterations;
                while (zx * zx + zy * zy < escapeRadius * escapeRadius && iter > 0) {
                    tmp = zx * zx - zy * zy + cX;
                    zy = 2.0 * zx * zy + cY;
                    zx = tmp;
                    iter--;
                }
                // Rysowanie fraktala i jego odbicia
                if (iter == 0) {
                    g.setColor(Color.BLACK);
                } else {
                    g.setColor(Color.getHSBColor((maxIterations / (float) iter) % 1, 1, 1));
                }
                g.fillRect(x + offsetX, y, 1, 1);
                // Odbicie fraktala względem osi pionowej
                g.fillRect(width - x - offsetX, y, 1, 1);
            }
        }
    }

    public void startAnimation() {
        executorService.scheduleAtFixedRate(this::moveFractal, 0, 50, TimeUnit.MILLISECONDS);
    }

    private void moveFractal() {
        offsetX += direction * 2; // Przesunięcie fraktala
        if (offsetX >= width || offsetX <= 0) {
            direction *= -1; // Odwrócenie kierunku po osiągnięciu krawędzi obszaru rysowania
        }
        repaint();
    }

    public int getPriority() {
        return priority;
    }

    public void stopAnimation() {
        executorService.shutdown();
    }
}

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new GridLayout(2, 5));

        // Ustalanie priorytetów animacji
        int[] priorities = {Thread.MIN_PRIORITY, Thread.MIN_PRIORITY, Thread.NORM_PRIORITY, Thread.NORM_PRIORITY,
                            Thread.NORM_PRIORITY, Thread.MAX_PRIORITY, Thread.MAX_PRIORITY, Thread.MAX_PRIORITY,
                            Thread.MAX_PRIORITY, Thread.MAX_PRIORITY};

        Animation[] animations = new Animation[10];
        for (int i = 0; i < 10; i++) {
            Animation animation = new Animation(800, 600, priorities[i]);
            frame.add(animation);
            animations[i] = animation;
            // Rozpoczęcie animacji po utworzeniu
            animation.startAnimation();
        }

        frame.setVisible(true);
    }
}