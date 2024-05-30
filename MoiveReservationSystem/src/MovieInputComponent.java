import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MovieInputComponent extends JFrame {
	private JFrame currentFrame;

	public MovieInputComponent() {
		setTitle("Select Table");
		setSize(400, 400);
		setLayout(new GridLayout(5, 2));

		JButton movieButton = new JButton("Movies");
		JButton movieActorsButton = new JButton("MovieActors");
		JButton actorsButton = new JButton("Actors");
		JButton screeningsButton = new JButton("Screenings");
		JButton theatersButton = new JButton("Theaters");
		JButton ticketsButton = new JButton("Tickets");
		JButton seatsButton = new JButton("Seats");
		JButton customersButton = new JButton("Customers");
		JButton bookingsButton = new JButton("Bookings");

		movieButton.addActionListener(this::openMovieInputForm);
		movieActorsButton.addActionListener(this::openMovieActorsInputForm);
		actorsButton.addActionListener(this::openActorsInputForm);
		screeningsButton.addActionListener(this::openScreeningsInputForm);
		theatersButton.addActionListener(this::openTheatersInputForm);
		ticketsButton.addActionListener(this::openTicketsInputForm);
		seatsButton.addActionListener(this::openSeatsInputForm);
		customersButton.addActionListener(this::openCustomersInputForm);
		bookingsButton.addActionListener(this::openBookingsInputForm);

		add(movieButton);
		add(movieActorsButton);
		add(actorsButton);
		add(screeningsButton);
		add(theatersButton);
		add(ticketsButton);
		add(seatsButton);
		add(customersButton);
		add(bookingsButton);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	private void openMovieInputForm(ActionEvent e) {
		openForm("Add Movie", 400, 400, new JLabel("MovieID:"), new JTextField(15), new JLabel("Title:"),
				new JTextField(15), new JLabel("MovieTime:"), new JTextField(15), new JLabel("Rating:"),
				new JTextField(15), new JLabel("Director:"), new JTextField(15), new JLabel("Genre:"),
				new JTextField(15), new JLabel("Introduction:"), new JTextArea(5, 15), new JLabel("ReleaseDate:"),
				new JTextField(15), new JLabel("Score:"), new JTextField(15));
	}

	private void openMovieActorsInputForm(ActionEvent e) {
		openForm("Add MovieActors", 400, 200, new JLabel("MovieID:"), new JTextField(15), new JLabel("ActorID:"),
				new JTextField(15));
	}

	private void openActorsInputForm(ActionEvent e) {
		openForm("Add Actor", 400, 200, new JLabel("ActorID:"), new JTextField(15), new JLabel("ActorName:"),
				new JTextField(15));
	}

	private void openScreeningsInputForm(ActionEvent e) {
		openForm("Add Screening", 400, 300, new JLabel("ScreeningID:"), new JTextField(15), new JLabel("MovieID:"),
				new JTextField(15), new JLabel("TheaterID:"), new JTextField(15), new JLabel("ScreeningDate:"),
				new JTextField(15), new JLabel("SessionNumber:"), new JTextField(15), new JLabel("StartTime:"),
				new JTextField(15), new JLabel("EndTime:"), new JTextField(15));
	}

	private void openTheatersInputForm(ActionEvent e) {
		openForm("Add Theater", 400, 300, new JLabel("TheaterID:"), new JTextField(15), new JLabel("ScreeningDate:"),
				new JTextField(15), new JLabel("NumberOfSeats:"), new JTextField(15), new JLabel("TheaterUse:"),
				new JCheckBox(), new JLabel("HorizontalSeats:"), new JTextField(15), new JLabel("VerticalSeats:"),
				new JTextField(15));
	}

	private void openTicketsInputForm(ActionEvent e) {
		openForm("Add Ticket", 400, 300, new JLabel("TicketID:"), new JTextField(15), new JLabel("ScreeningID:"),
				new JTextField(15), new JLabel("ScreeningDate:"), new JTextField(15), new JLabel("TheaterID:"),
				new JTextField(15), new JLabel("SeatID:"), new JTextField(15), new JLabel("BookingID:"),
				new JTextField(15), new JLabel("IsTicketing:"), new JCheckBox(), new JLabel("StandardPrice:"),
				new JTextField(15), new JLabel("SalePrice:"), new JTextField(15));
	}

	private void openSeatsInputForm(ActionEvent e) {
		openForm("Add Seat", 400, 200, new JLabel("SeatID:"), new JTextField(15), new JLabel("TheaterID:"),
				new JTextField(15), new JLabel("ScreeningDate:"), new JTextField(15), new JLabel("IsActive:"),
				new JCheckBox());
	}

	private void openCustomersInputForm(ActionEvent e) {
		openForm("Add Customer", 400, 200, new JLabel("CustomerID:"), new JTextField(15), new JLabel("CustomerName:"),
				new JTextField(15), new JLabel("PhoneNumber:"), new JTextField(15), new JLabel("Email:"),
				new JTextField(15));
	}

	private void openBookingsInputForm(ActionEvent e) {
		openForm("Add Booking", 400, 300, new JLabel("BookingID:"), new JTextField(15), new JLabel("Payment:"),
				new JTextField(15), new JLabel("PaymentStatus:"), new JTextField(15), new JLabel("Amount:"),
				new JTextField(15), new JLabel("CustomerID:"), new JTextField(15), new JLabel("PaymentDate:"),
				new JTextField(15));
	}

	private void openForm(String title, int width, int height, JComponent... components) {
		currentFrame = createFrame(title, width, height);
		addInputFields(currentFrame, components);
	}

	private void saveData(ActionEvent e) {
		JOptionPane.showMessageDialog(currentFrame, "Data saved successfully!");
		closeFrame(e);
	}

	private void closeFrame(ActionEvent e) {
		currentFrame.dispose();
	}

	private JFrame createFrame(String title, int width, int height) {
		JFrame frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setLayout(new GridLayout(0, 2));
		return frame;
	}

	private void addInputFields(JFrame frame, JComponent... components) {
		for (JComponent component : components) {
			frame.add(component);
		}
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(this::saveData);
		frame.add(saveButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this::closeFrame);
		frame.add(cancelButton);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
}