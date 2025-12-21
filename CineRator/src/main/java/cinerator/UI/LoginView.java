package cinerator.ui;

import cinerator.AppController;
import cinerator.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class LoginView {

    private final JPanel mainPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private AppController controller;

    // --- COLOR PALETTE ---
    private final Color BG_COLOR = new Color(250, 248, 245);
    private final Color TEXT_PRIMARY = new Color(60, 45, 40);
    private final Color TEXT_SECONDARY = new Color(140, 120, 110);
    private final Color ACCENT_COLOR = new Color(220, 85, 45);
    private final Color ACCENT_HOVER = new Color(200, 70, 30);
    private final Color FIELD_BORDER = new Color(210, 200, 195);
    private final Color LINK_COLOR   = new Color(100, 100, 200);

    public LoginView() {
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BG_COLOR);
        mainPanel.add(createCard());
    }

    public void setController(AppController controller) {
        this.controller = controller;
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    private JPanel createCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
                g2.dispose();
            }
        };

        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(50, 60, 50, 60));

        // --- Elements ---

        // 1. Title
        JLabel titleLabel = new JLabel("Welcome to CineRator", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 2. Subtitle
        JLabel subLabel = new JLabel("Login to your account", SwingConstants.CENTER);
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subLabel.setForeground(TEXT_SECONDARY);
        subLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 3. Inputs
        JLabel userLabel = createFieldLabel("Username");
        usernameField = createFlatField();

        JLabel passLabel = createFieldLabel("Password");
        passwordField = createFlatPasswordField();

        // 4. Button
        loginButton = new JButton("Sign In");
        styleButton(loginButton);
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        loginButton.addActionListener(e -> handleLogin());

        // 5. NEW: Register Link
        JLabel registerLink = new JLabel("Don't have an account? Register");
        registerLink.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        registerLink.setForeground(LINK_COLOR);
        registerLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLink.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add hover effect and click action
        registerLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                openRegisterView();
            }
            public void mouseEntered(MouseEvent e) {
                registerLink.setForeground(ACCENT_COLOR); // Change color on hover
            }
            public void mouseExited(MouseEvent e) {
                registerLink.setForeground(LINK_COLOR);
            }
        });

        // --- Assembly ---
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(subLabel);
        card.add(Box.createVerticalStrut(40));
        card.add(userLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(usernameField);
        card.add(Box.createVerticalStrut(20));
        card.add(passLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(40));
        card.add(loginButton);
        card.add(Box.createVerticalStrut(15));
        card.add(registerLink); // Add the link at the bottom

        return card;
    }

    private void openRegisterView() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
        RegisterView registerView = new RegisterView(controller);
        frame.setContentPane(registerView.getPanel());
        frame.revalidate();
        frame.repaint();
    }

    // --- Helpers ---

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(TEXT_SECONDARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField createFlatField() {
        JTextField field = new JTextField(20);
        styleInput(field);
        return field;
    }

    private JPasswordField createFlatPasswordField() {
        JPasswordField field = new JPasswordField(20);
        styleInput(field);
        field.addActionListener(e -> handleLogin());
        return field;
    }

    private void styleInput(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(Color.WHITE);
        field.setCaretColor(ACCENT_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, FIELD_BORDER),
                BorderFactory.createEmptyBorder(5, 0, 5, 0)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void styleButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(ACCENT_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) { btn.setBackground(ACCENT_HOVER); }
            public void mouseExited(MouseEvent evt) { btn.setBackground(ACCENT_COLOR); }
        });
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        try {
            if (controller != null) {
                User user = controller.login(username, password);

                // Hide Login Frame and Open Dashboard
                JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                loginFrame.dispose();

                new DashView(controller, user);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainPanel, ex.getMessage(), "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}