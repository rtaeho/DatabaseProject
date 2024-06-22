import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MovieUpdateComponent extends JFrame {
	private Connection connection;
	private JFrame currentFrame;

	public MovieUpdateComponent(Connection connection) {
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
		setLocationRelativeTo(null); // 창을 화면 중앙에 배치
		setVisible(true);
	}

	private void addButton(String tableName) {
		JButton button = new JButton(tableName);
		button.addActionListener(e -> openUpdateForm(tableName));
		add(button);
	}

	private void openUpdateForm(String tableName) {
		currentFrame = createFrame("변경 " + tableName, 600, 200);
		currentFrame.getContentPane().requestFocusInWindow();
		createUpdateForm(tableName);
	}

	private void createUpdateForm(String tableName) {
		currentFrame.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;

		JLabel instructionLabel = new JLabel("테이블 '" + tableName + "'에 대한 SQL UPDATE 입력 : ");
		currentFrame.add(instructionLabel, gbc);

		gbc.gridy++;
		gbc.gridwidth = 2;
		JTextField sqlField = new JTextField(20);
		currentFrame.add(sqlField, gbc);

		gbc.gridy++;
		JLabel conditionLabel = new JLabel("조건 입력 : ");
		currentFrame.add(conditionLabel, gbc);

		gbc.gridy++;
		JTextField conditionField = new JTextField(20);
		currentFrame.add(conditionField, gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.gridx = 0;

		JButton saveButton = new JButton("저장");
		saveButton.addActionListener(e -> {
			String sql = sqlField.getText();
			String condition = conditionField.getText();
			try {
				String updateQuery = "UPDATE " + tableName + " SET " + sql + " WHERE " + condition;
				PreparedStatement stmt = connection.prepareStatement(updateQuery);
				int rowsAffected = stmt.executeUpdate();
				JOptionPane.showMessageDialog(currentFrame, "변경된 행의 수: " + rowsAffected);
				stmt.close();
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(currentFrame, "SQL 실행 오류: " + ex.getMessage(), "오류",
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
