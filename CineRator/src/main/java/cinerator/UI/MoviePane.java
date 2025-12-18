package cinerator.UI;

import javax.swing.*;

public class MoviePane {
    private JPanel panel1;
    private JTextArea textArea1;
    private JTextField textField1;
    private JTextField textField2;
    private JButton rateButton;

    public MoviePane() {
        // Disable borders (seamless look)
        textField1.setBorder(BorderFactory.createEmptyBorder());
        textField2.setBorder(BorderFactory.createEmptyBorder());
        textArea1.setBorder(BorderFactory.createEmptyBorder());
    }

    public JPanel getPanel() {
        return panel1;
    }

    public static void main(String[] args) {
        // Always run Swing code on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Force Windows Classic Look and Feel
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
            } catch (Exception e) {
                System.out.println("Windows Classic L&F not available, falling back to default.");
            }


            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("Movie");

            // Add MoviePane content
            MoviePane moviePane = new MoviePane();
            frame.setContentPane(moviePane.getPanel());

            // size to fit components
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null); // center on screen
            frame.setVisible(true);    // show the window
        });
    }
}