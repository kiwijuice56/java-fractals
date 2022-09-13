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

        List<Fractal> fractalDrawers = new ArrayList<>();
        fractalDrawers.add(new Mandelbrot());
        fractalDrawers.add(new TricornDrawer());
        fractalDrawers.add(new BurningShip());
        fractalDrawers.add(new Newton());
        fractalDrawers.add(new Mandelbox());
        fractalDrawers.add(new Tadpole());
        fractalDrawers.add(new Flatworm());
        fractalDrawers.add(new Duck());
        fractalDrawers.add(new Astronaut());
        fractalDrawers.add(new Alfaro());

        DrawingPanel drawingPanel = new DrawingPanel();
        drawingPanel.current = fractalDrawers.get(0);
        add(drawingPanel, BorderLayout.CENTER);

        JPanel infoRow = new JPanel();

        JLabel infoLabel = new DarkJLabel();

        infoRow.setBackground(Color.BLACK);
        infoRow.add(infoLabel);

       // add(infoRow, BorderLayout.NORTH);

        JPanel buttonRow = new JPanel();
        buttonRow.setLayout(new GridLayout(2, 6));

        for (Fractal drawer: fractalDrawers) {
            JButton button = new DarkJButton(drawer.getName());
            button.addActionListener(e -> {
                drawingPanel.current = drawer;
                // Update values on each click
                infoLabel.setText(drawingPanel.current.toString());
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
