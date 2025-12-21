package cinerator.ui;

import cinerator.AppController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class RegisterView {

    private final JPanel mainPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmField;
    private JButton regButton;
    private AppController controller;

    // --- COLOR PALETTE (Same as LoginView) ---
    private final Color BG_COLOR = new Color(250, 248, 245);
    private final Color TEXT_PRIMARY = new Color(60, 45, 40);
    private final Color TEXT_SECONDARY = new Color(140, 120, 110);
    private final Color ACCENT_COLOR = new Color(220, 85, 45);
    private final Color ACCENT_HOVER = new Color(200, 70, 30);
    private final Color FIELD_BORDER = new Color(210, 200, 195);
    private final Color LINK_COLOR   = new Color(100, 100, 200);

    public RegisterView(AppController controller) {
        this.controller = controller;
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BG_COLOR);
        mainPanel.add(createCard());
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

        JLabel titleLabel = new JLabel("Join a CineRatorian", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subLabel = new JLabel("Create a new account", SwingConstants.CENTER);
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subLabel.setForeground(TEXT_SECONDARY);
        subLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Inputs
        JLabel userLabel = createFieldLabel("Username");
        usernameField = createFlatField();

        JLabel passLabel = createFieldLabel("Password");
        passwordField = createFlatPasswordField();

        JLabel confirmLabel = createFieldLabel("Confirm Password");
        confirmField = createFlatPasswordField();

        // Button
        regButton = new JButton("Sign Up");
        styleButton(regButton);
        regButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        regButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        regButton.addActionListener(e -> handleRegister());

        // Back Link
        JLabel loginLink = new JLabel("Already have an account? Login");
        loginLink.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        loginLink.setForeground(LINK_COLOR);
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLink.setAlignmentX(Component.LEFT_ALIGNMENT);

        loginLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { goToLogin(); }
            public void mouseEntered(MouseEvent e) { loginLink.setForeground(ACCENT_COLOR); }
            public void mouseExited(MouseEvent e) { loginLink.setForeground(LINK_COLOR); }
        });

        // --- Assembly ---
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(subLabel);
        card.add(Box.createVerticalStrut(30));

        card.add(userLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(usernameField);
        card.add(Box.createVerticalStrut(15));

        card.add(passLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(15));

        card.add(confirmLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(confirmField);
        card.add(Box.createVerticalStrut(30));

        card.add(regButton);
        card.add(Box.createVerticalStrut(15));
        card.add(loginLink);

        return card;
    }

    private void handleRegister() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword());
        String confirm = new String(confirmField.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Please fill in all fields.");
            return;
        }

        if (!pass.equals(confirm)) {
            JOptionPane.showMessageDialog(mainPanel, "Passwords do not match.");
            return;
        }

        try {
            controller.registerUser(user, pass);
            JOptionPane.showMessageDialog(mainPanel, "Account created! Please login.");
            goToLogin();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainPanel, ex.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void goToLogin() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
        LoginView login = new LoginView();
        login.setController(controller);
        frame.setContentPane(login.getPanel());
        frame.revalidate();
        frame.repaint();
    }

    // --- Helpers (Duplicated from LoginView to match style) ---

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
}