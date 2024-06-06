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
		setLocationRelativeTo(null); // 창을 화면 중앙에 배치
		setVisible(true);
	}

	private void addButton(String tableName) {
		JButton button = new JButton(tableName);
		button.addActionListener(e -> openDeleteForm(tableName));
		add(button);
	}

	private void openDeleteForm(String tableName) {
		currentFrame = createFrame("삭제 " + tableName, 600, 200);
		currentFrame.getContentPane().requestFocusInWindow();
		createDeleteForm(tableName);
	}

	private void createDeleteForm(String tableName) {
		JLabel instructionLabel = new JLabel("WHERE 절 입력 (예: movie_id = 1):");
		JTextField whereField = new JTextField();

		ActionListener saveAction = e -> {
			String whereClause = whereField.getText();
			String sql = "DELETE FROM " + tableName + " WHERE " + whereClause;
			try (PreparedStatement stmt = connection.prepareStatement(sql)) {
				int rowsAffected = stmt.executeUpdate();
				JOptionPane.showMessageDialog(currentFrame, "삭제된 행의 수: " + rowsAffected);
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(currentFrame, "SQL 실행 오류: " + ex.getMessage(), "오류",
						JOptionPane.ERROR_MESSAGE);
			}
		};

		ActionListener cancelAction = e -> currentFrame.dispose();

		JPanel buttonPanel = createButtonPanel(saveAction, cancelAction);

		currentFrame.setLayout(new GridLayout(3, 1));
		addInputFields(currentFrame, instructionLabel, whereField, buttonPanel);
	}

	private void addInputFields(JFrame frame, JComponent... components) {
		for (JComponent component : components) {
			frame.add(component);
		}
	}

	private JPanel createButtonPanel(ActionListener saveAction, ActionListener cancelAction) {
		JPanel buttonPanel = new JPanel();

		JButton saveButton = new JButton("저장");
		saveButton.addActionListener(saveAction);

		JButton cancelButton = new JButton("취소");
		cancelButton.addActionListener(cancelAction);

		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);

		return buttonPanel;
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
