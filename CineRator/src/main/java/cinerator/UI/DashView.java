package cinerator.ui;

import cinerator.AppController;
import cinerator.model.Movie; // Updated Import
import cinerator.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class DashView extends JFrame {

    private final AppController controller;
    private final User user;

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
        page.add(createHeader("Community Movie List"), BorderLayout.NORTH);

        JPanel grid = createGrid();

        // FIX 1: Use List<Movie> and getters
        List<Movie> movies = controller.getAllMovies();

        if (movies.isEmpty()) {
            JLabel empty = new JLabel("No movies added yet. Go add one!", SwingConstants.CENTER);
            empty.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            empty.setBorder(new EmptyBorder(50, 0, 0, 0));
            grid.add(empty);
        } else {
            for (Movie m : movies) {
                // FIX 2: Pass the whole Movie object so we have the ID for rating
                grid.add(createMovieCard(m));
            }
        }

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
            // Re-initialize Login View properly
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
    // FIX 3: Accept 'Movie' object to access ID, Title, Image, etc.
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

        // Use getters
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

        // FIX 4: Pass the whole movie object to the dialog
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

    // FIX 5: Use ID for saving rating
    private void showRatingDialog(Movie movie) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.add(new JLabel("How many stars for " + movie.getTitle() + "?"), BorderLayout.NORTH);

        JSlider slider = new JSlider(1, 10, 5);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        panel.add(slider, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(this, panel, "Rate Movie",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            int score = slider.getValue();
            // Call the correct method on controller: saveUserRating
            controller.saveUserRating(user.getId(), movie.getId(), (double) score, "");
            JOptionPane.showMessageDialog(this, "Rated " + score + "/10!");
        }
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

        JTextField search = new JTextField(" Search movies...");
        search.setPreferredSize(new Dimension(250, 35));
        search.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        search.setForeground(Color.GRAY);

        header.add(title, BorderLayout.WEST);
        header.add(search, BorderLayout.EAST);
        return header;
    }
}