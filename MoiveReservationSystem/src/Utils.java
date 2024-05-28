import javax.swing.*;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Utils {
    public static void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    public static Connection connectToDatabase(String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/db1", username, password);
        } catch (ClassNotFoundException | SQLException e) {
            showMessage("Database connection error: " + e.getMessage());
            return null;
        }
    }
    public static void switchToPanel(JComponent component, JPanel newPanel) {
        JFrame frame = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, component);
        if (frame != null) {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(newPanel, BorderLayout.CENTER);
            frame.revalidate();
            frame.repaint();
        }
    }
}
