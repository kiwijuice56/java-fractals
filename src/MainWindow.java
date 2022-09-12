import fractal.*;

import java.awt.*;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class MainWindow extends JFrame {

    // Initialize main window
    public MainWindow() {
        setTitle("Alfaro Fractals");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        List<FractalDrawer> fractalDrawers = new ArrayList<>();
        fractalDrawers.add(new MandelbrotDrawer());
        fractalDrawers.add(new TricornDrawer());
        fractalDrawers.add(new BurningShipDrawer());
        fractalDrawers.add(new BuddhabrotDrawer());
        fractalDrawers.add(new NewtonFractal());
        fractalDrawers.add(new MandelBoxDrawer());
        fractalDrawers.add(new TadpoleFractal());
        fractalDrawers.add(new ChoppedFractal());
        fractalDrawers.add(new DuckFractalDrawer());
        fractalDrawers.add(new AstronautDrawer());
        fractalDrawers.add(new AlfaroFractalDrawer());

        DrawingPanel drawingPanel = new DrawingPanel();
        drawingPanel.current = fractalDrawers.get(0);
        add(drawingPanel, BorderLayout.CENTER);

        JPanel infoRow = new JPanel();

        JLabel positionLabel = new DarkJLabel();
        JLabel zoomLabel = new DarkJLabel();
        JLabel nLabel = new DarkJLabel();
        JLabel dLabel = new DarkJLabel();
        JLabel resLabel = new DarkJLabel();
        infoRow.setBackground(Color.BLACK);

        infoRow.add(positionLabel); infoRow.add(zoomLabel); infoRow.add(nLabel); infoRow.add(dLabel); infoRow.add(resLabel);

        drawingPanel.connect(positionLabel, zoomLabel, nLabel, dLabel, resLabel);

        add(infoRow, BorderLayout.NORTH);

        JPanel buttonRow = new JPanel();
        buttonRow.setLayout(new GridLayout(2, 6));

        for (FractalDrawer drawer: fractalDrawers) {
            JButton button = new DarkJButton(drawer.toString());
            button.addActionListener(e -> {
                drawingPanel.current = drawer;
                // Update values on each click
                drawingPanel.connect(positionLabel, zoomLabel, nLabel, dLabel, resLabel);
                });
            buttonRow.add(button);
        }
        buttonRow.setBackground(Color.BLACK);

        drawingPanel.grabFocus();

        add(buttonRow, BorderLayout.SOUTH);



        setSize(new Dimension(800, 600));
    }

   public static void main(String[] args) {
		MainWindow m = new MainWindow();
        while (true)
            m.repaint();
   }

   class DarkJLabel extends JLabel {
       public DarkJLabel() {
           setForeground(Color.WHITE);
           setFont(new Font("Consolas", Font.BOLD, 12));
       }
   }

    class DarkJButton extends JButton {
        public DarkJButton(String text) {
            super(text);
            setFocusable(false);
            setFont(new Font("Consolas", Font.BOLD, 12));
            setBackground(Color.BLACK);
            setForeground(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            setPreferredSize(new Dimension(256, 24));
        }
    }
}
