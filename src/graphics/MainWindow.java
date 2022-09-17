package graphics;

import fractal.*;

import java.awt.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

public class MainWindow extends JFrame {

    // Initialize main window
    public MainWindow() throws IOException {
        setTitle("Fractals");
        setIconImage(ImageIO.read(getClass().getResource("/icon.png")));
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        List<Fractal> fractalDrawers = new ArrayList<>();

        fractalDrawers.add(new Alfaro());
        fractalDrawers.add(new Astronaut());
        fractalDrawers.add(new BurningShip());
        fractalDrawers.add(new Duck());
        fractalDrawers.add(new Flatworm());
        fractalDrawers.add(new Mandelbox());
        fractalDrawers.add(new Mandelbrot());
        fractalDrawers.add(new Newton());
        fractalDrawers.add(new SierpinskiTriangle());
        fractalDrawers.add(new SierpinskiTriangle3D());
        fractalDrawers.add(new Tadpole());
        fractalDrawers.add(new Tricorn());

        DrawingPanel drawingPanel = new DrawingPanel();
        drawingPanel.current = fractalDrawers.get(6);
        add(drawingPanel, BorderLayout.CENTER);

        JPanel buttonRow = new JPanel();
        buttonRow.setLayout(new GridLayout(12, 1));

        for (Fractal drawer: fractalDrawers) {
            JButton button = new DarkJButton(drawer.getName());
            button.addActionListener(e -> drawingPanel.current = drawer);
            buttonRow.add(button);
        }
        buttonRow.setBackground(Color.BLACK);

        drawingPanel.grabFocus();

        add(buttonRow, BorderLayout.EAST);

        setSize(new Dimension(800, 700));

    }

        public static void main(String[] args) throws IOException {
            MainWindow m = new MainWindow();
            while (true)
                m.repaint();
    }

    static class DarkJButton extends JButton {
        public DarkJButton(String text) {
            super(text);
            setFocusable(false);
            setFont(new Font("Consolas", Font.PLAIN, 12));
            setBackground(Color.BLACK);
            setForeground(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            setPreferredSize(new Dimension(128, 14));
        }
    }
}
