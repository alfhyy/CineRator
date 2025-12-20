package cinerator;

import cinerator.model.User;
import cinerator.storage.ExcelStorage;
import cinerator.ui.LoginView;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        // 1. Setup Error Catcher (Safety Net)
        // This forces any hidden UI crashes to print to the console
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            System.err.println("!!! GUI ERRO !!!");
            e.printStackTrace();
        });

        // 2. Setup Look and Feel
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.out.println("FlatLaf not found. Using default style.");
        }

        // 3. Initialize Backend
        System.out.println("Initializing Storage...");
        ExcelStorage storage = new ExcelStorage();

        if (storage.findUserByUsername("admin") == null) {
            System.out.println("Creating admin user...");
            storage.saveUser(new User(UUID.randomUUID().toString(), "admin", "12345"));
            System.out.println(">> User Created: 'admin' / '12345'");
        }

        AppController controller = new AppController(storage);

        // 4. Launch UI
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("Creating LoginView...");
                LoginView view = new LoginView();
                view.setController(controller);

                JFrame frame = new JFrame("CineRator");
                frame.setContentPane(view.getPanel());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(900, 600);
                frame.setLocationRelativeTo(null);

                System.out.println("Displaying Window...");
                frame.setVisible(true);

            } catch (Exception e) {
                System.err.println("!!! CRASH WHILE STARTING UI !!!");
                e.printStackTrace();
            }
        });
    }
}