import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
		currentFrame = createFrame("추가 " + tableName, 600, 600);
		currentFrame.getContentPane().requestFocusInWindow();
		switch (tableName) {
		case "Movies":
			createUpdateFormForMovies();
			break;
		case "Screenings":
			createUpdateFormForScreenings();
			break;
		case "Theaters":
			createUpdateFormForTheaters();
			break;
		case "Tickets":
			createUpdateFormForTickets();
			break;
		case "Seats":
			createUpdateFormForSeats();
			break;
		case "Customers":
			createUpdateFormForCustomers();
			break;
		case "Bookings":
			createUpdateFormForBookings();
			break;
		default:
			break;
		}
	}

	private void createUpdateFormForMovies() {
	}

	private void createUpdateFormForScreenings() {

	}

	private void createUpdateFormForTheaters() {

	}

	private void createUpdateFormForTickets() {

	}

	private void createUpdateFormForSeats() {

	}

	private void createUpdateFormForCustomers() {

	}

	private void createUpdateFormForBookings() {

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
