package cinerator.ui;

import cinerator.AppController;
import cinerator.model.User;
import cinerator.model.Movie;
import cinerator.model.Rating;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.List;

public class RatedView extends JPanel {

    private final AppController controller;
    private final User user;

    // --- COLOR PALETTE ---
    private final Color MAIN_BG    = new Color(250, 248, 245);
    private final Color ACCENT     = new Color(220, 85, 45); // Burnt Orange
    private final Color TEXT_DARK  = new Color(60, 45, 40);
    private final Color TEXT_GRAY  = new Color(120, 120, 120);
    private final Color RED_ACTION = new Color(200, 60, 60);

    public RatedView(AppController controller, User user) {
        this.controller = controller;
        this.user = user;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(MAIN_BG);

        // Header
        add(createHeader("Your Rated Movies"), BorderLayout.NORTH);

        // Grid Content
        JPanel grid = new JPanel(new GridLayout(0, 4, 20, 20));
        grid.setBackground(MAIN_BG);
        grid.setBorder(new EmptyBorder(10, 30, 30, 30));

        // FIX 1: Pass the whole 'user' object, not just the ID
        List<Movie> movies = controller.getRatedMovies(user);

        if (movies == null || movies.isEmpty()) {
            JLabel empty = new JLabel("You haven't rated any movies yet.", SwingConstants.CENTER);
            empty.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            empty.setForeground(TEXT_GRAY);

            JPanel emptyPanel = new JPanel(new BorderLayout());
            emptyPanel.setBackground(MAIN_BG);
            emptyPanel.add(empty, BorderLayout.CENTER);
            add(emptyPanel, BorderLayout.CENTER);
            return;
        }

        for (Movie m : movies) {
            grid.add(createRatedCard(m));
        }

        JPanel gridWrapper = new JPanel(new BorderLayout());
        gridWrapper.setBackground(MAIN_BG);
        gridWrapper.add(grid, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(gridWrapper);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(scroll, BorderLayout.CENTER);
    }

    // =================================================================================
    // --- THE MODAL (EDIT RATING) ---
    // =================================================================================
    private void showEditDialog(Movie movie) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Manage Rating", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(550, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.getContentPane().setBackground(Color.WHITE);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(30, 30, 30, 30));

        // --- SECTION 1: MOVIE DETAILS (READ ONLY) ---
        JPanel topSection = new JPanel(new BorderLayout(20, 0));
        topSection.setBackground(Color.WHITE);
        topSection.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        topSection.setAlignmentX(Component.LEFT_ALIGNMENT);

        // FIX 2: Use getters (getPosterUrl or getImageUrl depending on your model)
        JLabel poster = loadPoster(movie.getImageUrl(), new Dimension(110, 160));
        poster.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        // FIX 3: Use getters for Title, Genre, Rating
        JLabel titleLbl = new JLabel("<html>" + movie.getTitle() + "</html>");
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLbl.setForeground(TEXT_DARK);

        JLabel genreLbl = new JLabel(movie.getGenre());
        genreLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        genreLbl.setForeground(TEXT_GRAY);

        JLabel globalRateLbl = new JLabel("Global Rating: " + movie.getRating());
        globalRateLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        globalRateLbl.setForeground(ACCENT);

        infoPanel.add(titleLbl);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(genreLbl);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(globalRateLbl);
        infoPanel.add(Box.createVerticalGlue());

        topSection.add(poster, BorderLayout.WEST);
        topSection.add(infoPanel, BorderLayout.CENTER);

        // --- SECTION 2: USER INPUTS (EDITABLE) ---

        // FIX 4: Ensure controller has this method and Movie has getId()
        Rating userRecord = controller.getUserRating(user.getId(), movie.getId());

        // FIX 5: Use getters on Rating object
        double currentRating = (userRecord != null) ? userRecord.getRating() : 5.0;
        String currentComment = (userRecord != null) ? userRecord.getComment() : "";

        JLabel editHeader = new JLabel("Your Review");
        editHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        editHeader.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel rateLbl = new JLabel("Rating (0-10)");
        rateLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        rateLbl.setForeground(TEXT_GRAY);
        rateLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSpinner ratingSpin = new JSpinner(new SpinnerNumberModel(currentRating, 0.0, 10.0, 0.1));
        ratingSpin.setMaximumSize(new Dimension(100, 35));
        ratingSpin.setAlignmentX(Component.LEFT_ALIGNMENT);
        styleSpinner(ratingSpin);

        JLabel commentLbl = new JLabel("Comment");
        commentLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        commentLbl.setForeground(TEXT_GRAY);
        commentLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea commentArea = new JTextArea(currentComment);
        commentArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);

        JScrollPane commentScroll = new JScrollPane(commentArea);
        commentScroll.setPreferredSize(new Dimension(400, 100));
        commentScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        commentScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        commentScroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JPanel btnPanel = new JPanel(new BorderLayout());
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        JButton deleteBtn = new JButton("Delete Review");
        deleteBtn.setBackground(Color.WHITE);
        deleteBtn.setForeground(RED_ACTION);
        deleteBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        deleteBtn.setBorder(BorderFactory.createLineBorder(RED_ACTION));
        deleteBtn.setFocusPainted(false);
        deleteBtn.setPreferredSize(new Dimension(120, 35));
        deleteBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(dialog,
                    "Remove your rating for this movie?",
                    "Delete Rating", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                // FIX 6: Use getId()
                controller.deleteUserRating(user.getId(), movie.getId());
                dialog.dispose();
                refreshUI();
            }
        });

        JButton saveBtn = new JButton("Update Review");
        saveBtn.setBackground(ACCENT);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveBtn.setFocusPainted(false);
        saveBtn.setBorderPainted(false);
        saveBtn.setPreferredSize(new Dimension(140, 35));
        saveBtn.addActionListener(e -> {
            double r = (double) ratingSpin.getValue();
            String c = commentArea.getText();
            // FIX 7: Use getId()
            controller.saveUserRating(user.getId(), movie.getId(), r, c);
            dialog.dispose();
            refreshUI();
        });

        btnPanel.add(deleteBtn, BorderLayout.WEST);
        btnPanel.add(saveBtn, BorderLayout.EAST);

        content.add(topSection);
        content.add(Box.createVerticalStrut(20));
        content.add(new JSeparator());
        content.add(Box.createVerticalStrut(20));
        content.add(editHeader);
        content.add(Box.createVerticalStrut(15));
        content.add(rateLbl);
        content.add(Box.createVerticalStrut(5));
        content.add(ratingSpin);
        content.add(Box.createVerticalStrut(15));
        content.add(commentLbl);
        content.add(Box.createVerticalStrut(5));
        content.add(commentScroll);
        content.add(Box.createVerticalGlue());
        content.add(btnPanel);

        dialog.add(content);
        dialog.setVisible(true);
    }

    private JPanel createRatedCard(Movie movie) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(180, 330));
        card.setMaximumSize(new Dimension(180, 330));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { showEditDialog(movie); }
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(ACCENT, 1),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }
        });

        // FIX 8: Use getImageUrl()
        JLabel poster = loadPoster(movie.getImageUrl(), new Dimension(158, 220));
        poster.setAlignmentX(Component.LEFT_ALIGNMENT);

        // FIX 9: Use getTitle()
        JLabel titleLbl = new JLabel(movie.getTitle());
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        // FIX 10: Use getId() and getRating()
        Rating r = controller.getUserRating(user.getId(), movie.getId());
        String userScore = (r != null) ? String.valueOf(r.getRating()) : "-";

        JLabel myRateLbl = new JLabel("You: \u2605 " + userScore);
        myRateLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        myRateLbl.setForeground(ACCENT);
        myRateLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(poster);
        card.add(Box.createVerticalStrut(10));
        card.add(titleLbl);
        card.add(Box.createVerticalStrut(5));
        card.add(myRateLbl);
        card.add(Box.createVerticalGlue());

        return card;
    }

    private void refreshUI() {
        removeAll();
        initUI();
        revalidate();
        repaint();
    }

    private JPanel createHeader(String titleText) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MAIN_BG);
        header.setBorder(new EmptyBorder(25, 35, 25, 35));
        JLabel title = new JLabel(titleText);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(TEXT_DARK);
        header.add(title, BorderLayout.WEST);
        return header;
    }

    private void styleSpinner(JSpinner spinner) {
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor) editor;
            JTextField tf = spinnerEditor.getTextField();
            tf.setHorizontalAlignment(JTextField.LEFT);
            tf.setBackground(new Color(250, 250, 250));
            tf.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        }
        spinner.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
    }

    private JLabel loadPoster(String imageUrl, Dimension size) {
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(size);
        imageLabel.setMaximumSize(size);
        imageLabel.setMinimumSize(size);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setOpaque(true);
        imageLabel.setBackground(new Color(230, 230, 235));

        if (imageUrl == null || imageUrl.isEmpty()) {
            imageLabel.setText("No Image");
            imageLabel.setForeground(TEXT_GRAY);
            return imageLabel;
        }
        try {
            ImageIcon icon = new ImageIcon(new URL(imageUrl));
            // FIX 11: Corrected Image.SCALE_SMOOTH
            Image img = icon.getImage().getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            imageLabel.setText("Error");
        }
        return imageLabel;
    }
}