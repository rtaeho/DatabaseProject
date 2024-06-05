import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class MovieInputComponent extends JFrame {
	private Connection connection;
	private JFrame currentFrame;

	public MovieInputComponent(Connection connection) {
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
		button.addActionListener(e -> openInputForm(tableName));
		add(button);
	}

	private void openInputForm(String tableName) {
		currentFrame = createFrame("추가 " + tableName, 600, 600);
		currentFrame.getContentPane().requestFocusInWindow();
		switch (tableName) {
		case "Movies":
			createInputFormForMovies();
			break;
		case "Screenings":
			createInputFormForScreenings();
			break;
		case "Theaters":
			createInputFormForTheaters();
			break;
		case "Tickets":
			createInputFormForTickets();
			break;
		case "Seats":
			createInputFormForSeats();
			break;
		case "Customers":
			createInputFormForCustomers();
			break;
		case "Bookings":
			createInputFormForBookings();
			break;
		default:
			break;
		}
	}

	private void createInputFormForMovies() {
		// 필드 초기화
		JTextField movieNameField = new JTextField();
		JTextField durationField = new JTextField();
		JTextField ratingField = new JTextField();
		JTextField directorField = new JTextField();
		JTextField actorsField = new JTextField();
		JTextField genreField = new JTextField();
		JTextField synopsisField = new JTextField();
		JTextField releaseDateField = new JTextField();
		JTextField ratingValueField = new JTextField();

		// 입력 필드 추가
		addInputFields(currentFrame, new JLabel("영화명:"), movieNameField, new JLabel("상영시간(분):"), durationField,
				new JLabel("상영등급:"), ratingField, new JLabel("감독명:"), directorField, new JLabel("배우:"), actorsField,
				new JLabel("장르:"), genreField, new JLabel("영화소개:"), synopsisField, new JLabel("개봉일자:"),
				releaseDateField, new JLabel("평점:"), ratingValueField);

		// 버튼 추가
		JPanel buttonPanel = createButtonPanel(e -> {
			// 저장 버튼 액션
			String movieName = movieNameField.getText();
			int duration = Integer.parseInt(durationField.getText());
			String rating = ratingField.getText();
			String director = directorField.getText();
			String actors = actorsField.getText();
			String genre = genreField.getText();
			String synopsis = synopsisField.getText();
			String releaseDate = releaseDateField.getText();
			double ratingValue = Double.parseDouble(ratingValueField.getText());

			// 영화 정보 처리 로직 추가
		}, e -> currentFrame.dispose());

		currentFrame.add(buttonPanel);
	}

	private void createInputFormForScreenings() {
		JTextField movieNumberField = new JTextField();
		JTextField theaterNumberField = new JTextField();
		JTextField startDateField = new JTextField();
		JTextField screeningDateField = new JTextField();
		JTextField sessionField = new JTextField();
		JTextField startTimeField = new JTextField();

		addInputFields(currentFrame, new JLabel("영화번호:"), movieNumberField, new JLabel("상영관번호:"), theaterNumberField,
				new JLabel("상영시작일:"), startDateField, new JLabel("상영일:"), screeningDateField, new JLabel("상영회차:"),
				sessionField, new JLabel("상영시작시간:"), startTimeField);

		JPanel buttonPanel = createButtonPanel(e -> {
			int movieNumber = Integer.parseInt(movieNumberField.getText());
			int theaterNumber = Integer.parseInt(theaterNumberField.getText());
			String startDate = startDateField.getText();
			String screeningDate = screeningDateField.getText();
			int session = Integer.parseInt(sessionField.getText());
			String startTime = startTimeField.getText();

			// 상영 정보 처리 로직 추가
		}, e -> currentFrame.dispose());

		currentFrame.add(buttonPanel);
	}

	private void createInputFormForTheaters() {
		JTextField theaterNumField = new JTextField();
		JTextField seatCountField = new JTextField();
		JCheckBox theaterUseCheckBox = new JCheckBox();
		JTextField horizontalSeatsField = new JTextField();
		JTextField verticalSeatsField = new JTextField();

		addInputFields(currentFrame, new JLabel("상영관번호:"), theaterNumField, new JLabel("좌석수:"), seatCountField,
				new JLabel("상영관사용여부:"), theaterUseCheckBox, new JLabel("가로좌석수:"), horizontalSeatsField,
				new JLabel("세로좌석수:"), verticalSeatsField);

		JPanel buttonPanel = createButtonPanel(e -> {
			int theaterNum = Integer.parseInt(theaterNumField.getText());
			int seatCount = Integer.parseInt(seatCountField.getText());
			boolean theaterUse = theaterUseCheckBox.isSelected();
			int horizontalSeats = Integer.parseInt(horizontalSeatsField.getText());
			int verticalSeats = Integer.parseInt(verticalSeatsField.getText());

			// 상영관 정보 처리 로직 추가
		}, e -> currentFrame.dispose());

		currentFrame.add(buttonPanel);
	}

	private void createInputFormForTickets() {
		JTextField scheduleNumberField = new JTextField();
		JTextField screeningDateField = new JTextField();
		JTextField theaterNumberField = new JTextField();
		JTextField seatNumberField = new JTextField();
		JTextField bookingNumberField = new JTextField();
		JCheckBox ticketIssuedCheckBox = new JCheckBox();
		JTextField standardPriceField = new JTextField();
		JTextField sellingPriceField = new JTextField();

		addInputFields(currentFrame, new JLabel("상영일정번호:"), scheduleNumberField, new JLabel("상영일:"), screeningDateField,
				new JLabel("상영관번호:"), theaterNumberField, new JLabel("좌석번호:"), seatNumberField, new JLabel("예매번호:"),
				bookingNumberField, new JLabel("발권여부:"), ticketIssuedCheckBox, new JLabel("표준가격:"), standardPriceField,
				new JLabel("판매가격:"), sellingPriceField);

		JPanel buttonPanel = createButtonPanel(e -> {
			int scheduleNumber = Integer.parseInt(scheduleNumberField.getText());
			String screeningDate = screeningDateField.getText();
			int theaterNumber = Integer.parseInt(theaterNumberField.getText());
			int seatNumber = Integer.parseInt(seatNumberField.getText());
			int bookingNumber = Integer.parseInt(bookingNumberField.getText());
			boolean ticketIssued = ticketIssuedCheckBox.isSelected();
			int standardPrice = Integer.parseInt(standardPriceField.getText());
			int sellingPrice = Integer.parseInt(sellingPriceField.getText());

			// 티켓 정보 처리 로직 추가
		}, e -> currentFrame.dispose());

		currentFrame.add(buttonPanel);
	}

	private void createInputFormForSeats() {
		JTextField theaterNumberField = new JTextField();
		JTextField screeningDateField = new JTextField();
		JCheckBox seatUseCheckBox = new JCheckBox();

		addInputFields(currentFrame, new JLabel("상영관번호:"), theaterNumberField, new JLabel("상영일:"), screeningDateField,
				new JLabel("좌석사용여부:"), seatUseCheckBox);

		JPanel buttonPanel = createButtonPanel(e -> {
			int theaterNumber = Integer.parseInt(theaterNumberField.getText());
			String screeningDate = screeningDateField.getText();
			boolean seatUse = seatUseCheckBox.isSelected();

			// 좌석 정보 처리 로직 추가
		}, e -> currentFrame.dispose());

		currentFrame.add(buttonPanel);
	}

	private void createInputFormForCustomers() {
		JTextField memberIdField = new JTextField();
		JTextField customerNameField = new JTextField();
		JTextField phoneNumberField = new JTextField();
		JTextField emailField = new JTextField();

		addInputFields(currentFrame, new JLabel("회원아이디:"), memberIdField, new JLabel("고객:"), customerNameField,
				new JLabel("휴대폰번호:"), phoneNumberField, new JLabel("전자메일주소:"), emailField);
		JPanel buttonPanel = createButtonPanel(e -> {
			String memberId = memberIdField.getText();
			String customerName = customerNameField.getText();
			String phoneNumber = phoneNumberField.getText();
			String email = emailField.getText();

			// 고객 정보 처리 로직 추가
		}, e -> currentFrame.dispose());

		currentFrame.add(buttonPanel);
	}

	private void createInputFormForBookings() {
		JTextField paymentMethodField = new JTextField();
		JTextField paymentStatusField = new JTextField();
		JTextField paymentAmountField = new JTextField();
		JTextField memberIdField = new JTextField();
		JTextField paymentDateField = new JTextField();

		addInputFields(currentFrame, new JLabel("결제방법:"), paymentMethodField, new JLabel("결제상태:"), paymentStatusField,
				new JLabel("결제금액:"), paymentAmountField, new JLabel("회원아이디:"), memberIdField, new JLabel("결제일자:"),
				paymentDateField);

		JPanel buttonPanel = createButtonPanel(e -> {
			String paymentMethod = paymentMethodField.getText();
			String paymentStatus = paymentStatusField.getText();
			int paymentAmount = Integer.parseInt(paymentAmountField.getText());
			String memberId = memberIdField.getText();
			String paymentDate = paymentDateField.getText();

			// 예약 정보 처리 로직 추가
		}, e -> currentFrame.dispose());

		currentFrame.add(buttonPanel);
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
