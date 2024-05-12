import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;

public class TraceApp extends JFrame {
    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    private final int CELL_SIZE = 50;
    private final int GRID_WIDTH = 10;
    private final int GRID_HEIGHT = 10;

    private JPanel gridPanel;
    private JButton upButton, downButton, leftButton, rightButton, resetButton, saveButton, loadButton;
    private JToggleButton traceToggleButton;

    private ArrayList<Point> tracePoints;
    private boolean traceEnabled;
    private Point currentPosition;

    public TraceApp() {
        setTitle("Trace Application");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        tracePoints = new ArrayList<>();
        traceEnabled = false;
        currentPosition = new Point(0, 0);

        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel(new GridLayout(8, 1));

        upButton = new JButton("Up");
        upButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveUp();
            }
        });
        controlPanel.add(upButton);

        leftButton = new JButton("Left");
        leftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveLeft();
            }
        });
        controlPanel.add(leftButton);

        rightButton = new JButton("Right");
        rightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveRight();
            }
        });
        controlPanel.add(rightButton);

        downButton = new JButton("Down");
        downButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveDown();
            }
        });
        controlPanel.add(downButton);

        resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });
        controlPanel.add(resetButton);

        JPanel fileControlPanel = new JPanel(new GridLayout(2, 1));
        saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveTrace();
            }
        });
        fileControlPanel.add(saveButton);

        loadButton = new JButton("Load");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadTrace();
            }
        });
        fileControlPanel.add(loadButton);
        controlPanel.add(fileControlPanel);

        add(controlPanel, BorderLayout.WEST);

        traceToggleButton = new JToggleButton("Trace On/Off");
        traceToggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                traceEnabled = traceToggleButton.isSelected();
            }
        });
        add(traceToggleButton, BorderLayout.SOUTH);

        gridPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGrid(g);
                drawTrace(g);
                drawCurrentPosition(g);
            }
        };
        gridPanel.setPreferredSize(new Dimension(CELL_SIZE * GRID_WIDTH, CELL_SIZE * GRID_HEIGHT));
        add(gridPanel, BorderLayout.CENTER);
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        // Disable mouse interaction with the grid panel
        // to only allow movement via buttons
        if (e.getSource() == gridPanel) {
            return;
        }
        super.processMouseEvent(e);
    }

    private void drawGrid(Graphics g) {
        g.setColor(Color.BLACK);
        for (int x = 0; x <= GRID_WIDTH; x++) {
            g.drawLine(x * CELL_SIZE, 0, x * CELL_SIZE, GRID_HEIGHT * CELL_SIZE);
        }
        for (int y = 0; y <= GRID_HEIGHT; y++) {
            g.drawLine(0, y * CELL_SIZE, GRID_WIDTH * CELL_SIZE, y * CELL_SIZE);
        }
    }

    private void drawTrace(Graphics g) {
        g.setColor(Color.RED);
        for (Point p : tracePoints) {
            int x = p.x * CELL_SIZE;
            int y = p.y * CELL_SIZE;
            g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
        }
    }

    private void drawCurrentPosition(Graphics g) {
        g.setColor(Color.BLUE);
        int x = currentPosition.x * CELL_SIZE;
        int y = currentPosition.y * CELL_SIZE;
        g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
    }

    private void moveUp() {
        if (currentPosition.y > 0) {
            currentPosition.y--;
            if (traceEnabled) {
                tracePoints.add(new Point(currentPosition));
            }
            gridPanel.repaint();
        }
    }

    private void moveDown() {
        if (currentPosition.y < GRID_HEIGHT - 1) {
            currentPosition.y++;
            if (traceEnabled) {
                tracePoints.add(new Point(currentPosition));
            }
            gridPanel.repaint();
        }
    }

    private void moveLeft() {
        if (currentPosition.x > 0) {
            currentPosition.x--;
            if (traceEnabled) {
                tracePoints.add(new Point(currentPosition));
            }
            gridPanel.repaint();
        }
    }

    private void moveRight() {
        if (currentPosition.x < GRID_WIDTH - 1) {
            currentPosition.x++;
            if (traceEnabled) {
                tracePoints.add(new Point(currentPosition));
            }
            gridPanel.repaint();
        }
    }

    private void reset() {
        currentPosition.setLocation(0, 0);
        tracePoints.clear();
        gridPanel.repaint();
    }

    private void saveTrace() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
                outputStream.writeObject(tracePoints);
                outputStream.writeObject(currentPosition);
                JOptionPane.showMessageDialog(this, "Trace saved successfully!");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error occurred while saving trace!");
            }
        }
    }

    private void loadTrace() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
                tracePoints = (ArrayList<Point>) inputStream.readObject();
                currentPosition = (Point) inputStream.readObject();
                gridPanel.repaint();
                JOptionPane.showMessageDialog(this, "Trace loaded successfully!");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error occurred while loading trace!");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TraceApp();
            }
        });
    }
}
