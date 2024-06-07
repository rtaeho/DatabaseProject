import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class UserPanel extends JPanel {
	private Connection dbConnection;

	public UserPanel(Connection dbConnection) {
		this.dbConnection = dbConnection;

		setLayout(new BorderLayout());

		JButton viewMoviesButton = new JButton("영화 조회");
		JButton viewBookingsButton = new JButton("예매한 영화 조회");
		JButton backButton = new JButton("뒤로가기");

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(viewMoviesButton);
		buttonPanel.add(viewBookingsButton);
		buttonPanel.add(backButton);

		add(buttonPanel, BorderLayout.NORTH);

		viewMoviesButton.addActionListener(e -> new MovieSearchComponent(dbConnection));
		viewBookingsButton.addActionListener(e -> new ViewUserBookings(dbConnection));
		backButton.addActionListener(e -> Utils.switchToPanel(this, new MainPanel()));
	}
}
