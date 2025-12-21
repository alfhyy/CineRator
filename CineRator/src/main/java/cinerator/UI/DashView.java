package cinerator.ui;

import cinerator.AppController;
import cinerator.model.Movie;
import cinerator.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class DashView extends JFrame {

    private final AppController controller;
    private final User user;

    // Search & Grid
    private JTextField searchField;
    private JPanel grid;
    private List<Movie> cachedMovies;
    private JPanel contentArea;

    // --- COLOR PALETTE ---
    private final Color SIDEBAR_BG = new Color(45, 35, 30);
    private final Color MAIN_BG    = new Color(250, 248, 245);
    private final Color ACCENT     = new Color(220, 85, 45);
    private final Color TEXT_DARK  = new Color(60, 45, 40);

    public DashView(AppController controller, User user) {
        this.controller = controller;
        this.user = user;
        initUI();
    }

    private void initUI() {
        setTitle("CineRator Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        setContentPane(root);

        // 1. Add Left Sidebar
        root.add(createSidebar(), BorderLayout.WEST);

        // 2. Main Content Area
        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(MAIN_BG);

        // Load default page
        showDiscoverPage();

        root.add(contentArea, BorderLayout.CENTER);
        setVisible(true);
    }

    private void switchPage(JPanel newPage) {
        contentArea.removeAll();
        contentArea.add(newPage, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }

    // --- PAGE 1: DISCOVER ---
    private void showDiscoverPage() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(MAIN_BG);

        // Create Header with Search
        JPanel header = createHeader("Community Movie List");
        page.add(header, BorderLayout.NORTH);

        // Live Search Listener
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterMovies(); }
            public void removeUpdate(DocumentEvent e) { filterMovies(); }
            public void changedUpdate(DocumentEvent e) { filterMovies(); }
        });

        // Cache Movies & Create Grid
        cachedMovies = controller.getAllMovies();
        grid = createGrid();
        updateGrid(cachedMovies);

        // Wrapper to prevent vertical stretching
        JPanel gridWrapper = new JPanel(new BorderLayout());
        gridWrapper.setBackground(MAIN_BG);
        gridWrapper.add(grid, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(gridWrapper);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        page.add(scroll, BorderLayout.CENTER);
        switchPage(page);
    }

    // --- SEARCH LOGIC (Local Filtering) ---
    private void filterMovies() {
        String query = searchField.getText().trim().toLowerCase();

        // If search is empty or default text, show all
        if (query.isEmpty() || query.equals("search movies...")) {
            updateGrid(cachedMovies);
            return;
        }

        // Filter the cached list
        List<Movie> filtered = new ArrayList<>();
        for (Movie m : cachedMovies) {
            if (m.getTitle().toLowerCase().contains(query) ||
                    m.getGenre().toLowerCase().contains(query)) {
                filtered.add(m);
            }
        }
        updateGrid(filtered);
    }

    private void updateGrid(List<Movie> movies) {
        grid.removeAll();
        if (movies.isEmpty()) {
            JLabel empty = new JLabel("No movies match your search.", SwingConstants.CENTER);
            empty.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            empty.setBorder(new EmptyBorder(50, 0, 0, 0));
            grid.add(empty);
        } else {
            for (Movie m : movies) {
                grid.add(createMovieCard(m));
            }
        }
        grid.revalidate();
        grid.repaint();
    }

    // --- PAGE 2: ADD MOVIE ---
    private void showAddMoviePage() {
        AddMovieView addPanel = new AddMovieView(controller, this::showDiscoverPage);
        switchPage(addPanel);
    }

    // --- SIDEBAR ---
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel logo = new JLabel("CINERATOR");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(40));

        JButton btnDiscover = createNavButton("Discover");
        btnDiscover.addActionListener(e -> showDiscoverPage());

        JButton btnRated = createNavButton("Rated Movies");
        btnRated.addActionListener(e -> switchPage(new RatedView(controller, user)));

        JButton btnAdd = createNavButton("Add Movie");
        btnAdd.addActionListener(e -> showAddMoviePage());

        sidebar.add(btnDiscover);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnRated);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnAdd);

        sidebar.add(Box.createVerticalGlue());

        JButton logoutBtn = createNavButton("Logout");
        logoutBtn.setForeground(new Color(255, 100, 100));
        logoutBtn.addActionListener(e -> {
            dispose();
            LoginView loginView = new LoginView();
            loginView.setController(controller);
            JFrame frame = new JFrame("CineRator");
            frame.setContentPane(loginView.getPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
        sidebar.add(logoutBtn);

        return sidebar;
    }

    // --- MOVIE CARD ---
    private JPanel createMovieCard(Movie movie) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(180, 320));
        card.setMaximumSize(new Dimension(180, 320));

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230,230,230), 1),
                BorderFactory.createEmptyBorder(10,10,10,10)
        ));

        JLabel poster = loadPoster(movie.getImageUrl());
        poster.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLbl = new JLabel(movie.getTitle());
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel genreLbl = new JLabel(movie.getGenre());
        genreLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        genreLbl.setForeground(Color.GRAY);
        genreLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel ratingPanel = new JPanel(new BorderLayout());
        ratingPanel.setBackground(Color.WHITE);
        ratingPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        ratingPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel rateLbl = new JLabel("\u2605 " + movie.getRating());
        rateLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        rateLbl.setForeground(new Color(255, 180, 0));

        JButton rateBtn = new JButton("Rate");
        rateBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        rateBtn.setBackground(ACCENT);
        rateBtn.setForeground(Color.WHITE);
        rateBtn.setFocusPainted(false);
        rateBtn.setMargin(new Insets(4, 10, 4, 10));

        rateBtn.addActionListener(e -> showRatingDialog(movie));

        ratingPanel.add(rateLbl, BorderLayout.WEST);
        ratingPanel.add(rateBtn, BorderLayout.EAST);

        card.add(poster);
        card.add(Box.createVerticalStrut(10));
        card.add(titleLbl);
        card.add(Box.createVerticalStrut(2));
        card.add(genreLbl);
        card.add(Box.createVerticalGlue());
        card.add(ratingPanel);

        return card;
    }

    // =================================================================================
    // --- UPDATED: RATING DIALOG WITH COMMENTS ---
    // =================================================================================
    private void showRatingDialog(Movie movie) {
        // 1. Create a nice Panel to hold inputs
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(350, 200)); // Make it spacious

        // 2. Rating Slider
        JLabel scoreLbl = new JLabel("Score: 5/10");
        scoreLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        scoreLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSlider slider = new JSlider(1, 10, 5);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setAlignmentX(Component.LEFT_ALIGNMENT);
        slider.addChangeListener(e -> scoreLbl.setText("Score: " + slider.getValue() + "/10"));

        // 3. Comment Area
        JLabel commentLbl = new JLabel("Your Review (Optional):");
        commentLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        commentLbl.setForeground(TEXT_DARK);
        commentLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea commentArea = new JTextArea(4, 20);
        commentArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);

        JScrollPane scrollComment = new JScrollPane(commentArea);
        scrollComment.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollComment.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // 4. Add components to panel
        panel.add(scoreLbl);
        panel.add(Box.createVerticalStrut(5));
        panel.add(slider);
        panel.add(Box.createVerticalStrut(20)); // Spacer
        panel.add(commentLbl);
        panel.add(Box.createVerticalStrut(5));
        panel.add(scrollComment);

        // 5. Show Dialog
        int result = JOptionPane.showConfirmDialog(this, panel, "Rate " + movie.getTitle(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            int score = slider.getValue();
            String comment = commentArea.getText().trim();

            // Save both score and comment!
            controller.saveUserRating(user.getId(), movie.getId(), (double) score, comment);
            JOptionPane.showMessageDialog(this, "Review submitted!");
        }
    }

    private JLabel loadPoster(String imageUrl) {
        JLabel imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        Dimension size = new Dimension(150, 220);
        imageLabel.setPreferredSize(size);
        imageLabel.setMaximumSize(size);
        imageLabel.setMinimumSize(size);

        if (imageUrl == null || imageUrl.isEmpty()) {
            imageLabel.setOpaque(true);
            imageLabel.setBackground(new Color(225, 225, 230));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setText("No Image");
            imageLabel.setForeground(Color.GRAY);
            return imageLabel;
        }

        try {
            ImageIcon icon = new ImageIcon(new java.net.URL(imageUrl));
            Image img = icon.getImage();
            Image newImg = img.getScaledInstance(150, 220, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(newImg));
        } catch (Exception e) {
            imageLabel.setOpaque(true);
            imageLabel.setBackground(new Color(225, 225, 230));
            imageLabel.setText("Error");
        }
        return imageLabel;
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(new Color(200, 200, 200));
        btn.setBackground(SIDEBAR_BG);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setForeground(Color.WHITE);
            }
            public void mouseExited(MouseEvent e) {
                if(!text.equals("Logout")) btn.setForeground(new Color(200, 200, 200));
            }
        });
        return btn;
    }

    private JPanel createGrid() {
        JPanel grid = new JPanel(new GridLayout(0, 4, 20, 20));
        grid.setBackground(MAIN_BG);
        grid.setBorder(new EmptyBorder(10, 30, 30, 30));
        return grid;
    }

    private JPanel createHeader(String titleText) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MAIN_BG);
        header.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel(titleText);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(TEXT_DARK);

        searchField = new JTextField(" Search movies...");
        searchField.setPreferredSize(new Dimension(250, 35));
        searchField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        searchField.setForeground(Color.GRAY);
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals(" Search movies...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText(" Search movies...");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });

        header.add(title, BorderLayout.WEST);
        header.add(searchField, BorderLayout.EAST);
        return header;
    }
}