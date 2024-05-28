import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DatabaseConnector {
	private static Connection conn;

	public static void connectToDatabase(String username, String password) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db1", username, password);
			JOptionPane.showMessageDialog(null, "DB 연결 완료: " + username);
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "JDBC 드라이버 로드 오류", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "DB 연결 오류", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static Connection getConnection() {
		return conn;
	}
}
