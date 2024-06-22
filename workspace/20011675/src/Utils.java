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
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL 드라이버 로드
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db1", username, password); // JDBC 연결
			System.out.println("DB 연결 완료");
		} catch (ClassNotFoundException e) {
			showMessage("Database connection error: " + e.getMessage());
			System.out.println("JDBC 드라이버 로드 오류");
		} catch (SQLException e) {
			showMessage("Database connection error: " + e.getMessage());
			System.out.println("DB 연결 오류");
		}
		return conn;
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
