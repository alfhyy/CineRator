package cinerator.ui;

import cinerator.AppController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class AddMovieView extends JPanel {

    private final AppController controller;
    private final Runnable onSuccessCallback;

    // Fields
    private JTextField titleField;
    private JTextField genreField;
    private JTextField posterField;
    private JSpinner ratingSpinner;
    private JLabel imagePreview;

    public AddMovieView(AppController controller, Runnable onSuccessCallback) {
        this.controller = controller;
        this.onSuccessCallback = onSuccessCallback;
        initUI();
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 247, 250));

        // --- THE CARD ---
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);

        // --- FIX: FORCE THE CARD TO BE WIDER (Width: 500px, Height: 650px) ---
        card.setPreferredSize(new Dimension(500, 650));

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(40, 50, 40, 50)
        ));

        // --- COMPONENTS ---
        JLabel header = new JLabel("Add New Movie");
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleField = createStyledField();
        genreField = createStyledField();
        posterField = createStyledField();

        ratingSpinner = new JSpinner(new SpinnerNumberModel(5.0, 0.0, 10.0, 0.1));
        ratingSpinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        ratingSpinner.setAlignmentX(Component.LEFT_ALIGNMENT);
        JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor) ratingSpinner.getEditor();
        spinnerEditor.getTextField().setHorizontalAlignment(JTextField.LEFT);

        imagePreview = new JLabel("Image Preview", SwingConstants.CENTER);
        imagePreview.setPreferredSize(new Dimension(100, 150));
        // Use MAX_VALUE for width so it doesn't shrink, but keep height 150
        imagePreview.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        imagePreview.setAlignmentX(Component.LEFT_ALIGNMENT);
        imagePreview.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // Auto-load logic
        posterField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) { updatePreview(posterField.getText()); }
        });

        JButton saveBtn = new JButton("Save Movie");
        saveBtn.setBackground(new Color(220, 85, 45));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Make button text slightly bigger
        saveBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); // Taller button
        saveBtn.addActionListener(e -> saveMovie());

        // --- ADDING TO CARD ---
        card.add(header);
        card.add(Box.createVerticalStrut(30));

        card.add(createLabel("Movie Title"));
        card.add(titleField);
        card.add(Box.createVerticalStrut(20)); // Increased spacing slightly

        card.add(createLabel("Genre"));
        card.add(genreField);
        card.add(Box.createVerticalStrut(20));

        card.add(createLabel("Rating (0-10)"));
        card.add(ratingSpinner);
        card.add(Box.createVerticalStrut(20));

        card.add(createLabel("Poster URL"));
        card.add(posterField);
        card.add(Box.createVerticalStrut(25));

        card.add(imagePreview);
        card.add(Box.createVerticalStrut(30));

        card.add(saveBtn);

        add(card);
    }

    private void saveMovie() {
        String title = titleField.getText();
        String genre = genreField.getText();
        String url = posterField.getText();
        double rating = (double) ratingSpinner.getValue();

        if (title.isEmpty() || genre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title and Genre are required.");
            return;
        }

        // Pass 4 arguments to controller
        controller.addMovie(title, genre, url, String.valueOf(rating));

        JOptionPane.showMessageDialog(this, "Movie Saved Successfully!");
        if (onSuccessCallback != null) onSuccessCallback.run();
    }

    // --- HELPERS ---

    private void updatePreview(String urlText) {
        if (urlText == null || urlText.isEmpty()) return;
        try {
            ImageIcon icon = new ImageIcon(new java.net.URL(urlText));
            Image img = icon.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
            imagePreview.setIcon(new ImageIcon(img));
            imagePreview.setText("");
        } catch (Exception ex) {
            imagePreview.setIcon(null);
            imagePreview.setText("Error");
        }
    }

    // Helper: Ensure fields align left and stretch
    private JTextField createStyledField() {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35)); // FORCE FULL WIDTH
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 0, 5, 0)
        ));
        return field;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(Color.GRAY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }
}