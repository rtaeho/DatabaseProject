import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MovieDeleteComponent extends JFrame {
	private Connection connection;
	private JFrame currentFrame;

	public MovieDeleteComponent(Connection connection) {
		this.connection = connection;
		setTitle("테이블 선택");
		setSize(400, 400);
		setLayout(new GridLayout(5, 2));
		addButton("Movies");
		addButton("Screenings");
		addButton("Theaters");
		addButton("Tickets");
		addButton("Seats");
		addButton("Customers");
		addButton("Bookings");

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void addButton(String tableName) {
		JButton button = new JButton(tableName);
		button.addActionListener(e -> openDeleteForm(tableName));
		add(button);
	}

	private void openDeleteForm(String tableName) {
		currentFrame = createFrame("수정 " + tableName, 600, 200);
		currentFrame.getContentPane().requestFocusInWindow();
		createDeleteForm(tableName);
	}

	private void createDeleteForm(String tableName) {
		currentFrame.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;

		JLabel instructionLabel = new JLabel("SQL 입력 : ");
		currentFrame.add(instructionLabel, gbc);

		gbc.gridy++;
		gbc.gridwidth = 2;
		JTextField whereField = new JTextField(20);
		currentFrame.add(whereField, gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.gridx = 0;

		JButton saveButton = new JButton("저장");
		saveButton.addActionListener(e -> {
			String whereClause = whereField.getText();
			String sql = "DELETE FROM " + tableName + " WHERE " + whereClause;
			try (PreparedStatement stmt = connection.prepareStatement(sql)) {
				int rowsAffected = stmt.executeUpdate();
				JOptionPane.showMessageDialog(currentFrame, "수정된 행의 수: " + rowsAffected);
			} catch (SQLException ex) {
				JOptionPane
						.showMessageDialog(currentFrame, "SQL 실행 오류: " + ex.getMessage(), "오류",
						JOptionPane.ERROR_MESSAGE);
			}
		});
		currentFrame.add(saveButton, gbc);

		gbc.gridx++;
		JButton cancelButton = new JButton("취소");
		cancelButton.addActionListener(e -> currentFrame.dispose());
		currentFrame.add(cancelButton, gbc);
	}

	private JFrame createFrame(String title, int width, int height) {
		JFrame frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setLayout(new GridLayout(0, 1));
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		return frame;
	}
}
