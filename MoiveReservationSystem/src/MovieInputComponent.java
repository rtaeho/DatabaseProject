import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
		JTextField movieNameField = new JTextField();
		JTextField durationField = new JTextField();
		JTextField ratingField = new JTextField();
		JTextField directorField = new JTextField();
		JTextField genreField = new JTextField();
		JTextField introductionField = new JTextField();
		JTextField releaseDateField = new JTextField();
		JTextField scoreField = new JTextField();
		JTextField actorsField = new JTextField(); // 배우명 필드 추가

		addInputFields(currentFrame, new JLabel("영화명:"), movieNameField, new JLabel("상영시간(분):"), durationField,
				new JLabel("상영등급:"), ratingField, new JLabel("감독명:"), directorField, new JLabel("장르:"), genreField,
				new JLabel("영화소개:"), introductionField, new JLabel("개봉일자:"), releaseDateField, new JLabel("평점:"),
				scoreField, new JLabel("배우명(쉼표로 구분):"), actorsField); // 배우명 필드 추가

		JPanel buttonPanel = createButtonPanel(e -> {
			String movieName = movieNameField.getText();
			int duration = Integer.parseInt(durationField.getText());
			String rating = ratingField.getText();
			String director = directorField.getText();
			String genre = genreField.getText();
			String introduction = introductionField.getText();
			String releaseDate = releaseDateField.getText();
			double score = Double.parseDouble(scoreField.getText());
			String[] actors = actorsField.getText().split(","); // 쉼표로 구분된 배우명

			try {
				int movieId = insertMovie(movieName, duration, rating, director, genre, introduction, releaseDate,
						score);

				for (String actorName : actors) {
					actorName = actorName.trim(); // 배우 이름의 앞뒤 공백 제거
					int actorId = insertActor(actorName); // 배우 정보 삽입

					insertMovieActor(movieId, actorId); // 영화-배우 관계 삽입
				}
				Utils.showMessage("저장되었습니다.");
				currentFrame.dispose();
			} catch (SQLException ex) {
				Utils.showMessage("에러: " + ex.getMessage());
			}
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
			ResultSet theaterResultSet = null;
			try {
				int screeningID = insertScreening(movieNumber, theaterNumber, startDate, screeningDate, session,
						startTime);
				theaterResultSet = getTheater(theaterNumber);
				if (theaterResultSet.next()) {
					int numSeats = theaterResultSet.getInt("NumberOfSeats"); // 상영관의 좌석 수 가져오기
					for (int i = 0; i < numSeats; i++) {
						insertSeat(theaterNumber, screeningID, false); // 상영관에 좌석 생성
					}
				} else {
					System.out.println("상영관을 찾을 수 없습니다.");
				}
				insertTheaterUse(theaterNumber, screeningID, true);
				Utils.showMessage("저장되었습니다.");
				currentFrame.dispose();
			} catch (SQLException ex) {
				Utils.showMessage("에러: " + ex.getMessage());
			}
		}, e -> currentFrame.dispose());

		currentFrame.add(buttonPanel);
	}

	private void createInputFormForTheaters() {
		JTextField theaterNumField = new JTextField();
		JTextField seatCountField = new JTextField();
		JTextField horizontalSeatsField = new JTextField();
		JTextField verticalSeatsField = new JTextField();

		addInputFields(currentFrame, new JLabel("상영관번호:"), theaterNumField, new JLabel("좌석수:"), seatCountField,
				new JLabel("가로좌석수:"), horizontalSeatsField, new JLabel("세로좌석수:"), verticalSeatsField);

		JPanel buttonPanel = createButtonPanel(e -> {
			int theaterNum = Integer.parseInt(theaterNumField.getText());
			int seatCount = Integer.parseInt(seatCountField.getText());
			int horizontalSeats = Integer.parseInt(horizontalSeatsField.getText());
			int verticalSeats = Integer.parseInt(verticalSeatsField.getText());

			try {
				insertTheater(theaterNum, seatCount, horizontalSeats, verticalSeats);
				Utils.showMessage("저장되었습니다.");
				currentFrame.dispose();
			} catch (SQLException ex) {
				// TODO Auto-generated catch block
				Utils.showMessage("에러: " + ex.getMessage());
			}
		}, e -> currentFrame.dispose());

		currentFrame.add(buttonPanel);
	}

	private void createInputFormForTickets() {
		JTextField scheduleNumberField = new JTextField();
		JTextField theaterNumberField = new JTextField();
		JTextField seatNumberField = new JTextField();
		JTextField bookingNumberField = new JTextField();
		JCheckBox ticketIssuedCheckBox = new JCheckBox();
		JTextField standardPriceField = new JTextField();
		JTextField sellingPriceField = new JTextField();

		addInputFields(currentFrame, new JLabel("상영일정번호:"), scheduleNumberField, new JLabel("상영관번호:"),
				theaterNumberField, new JLabel("좌석번호:"), seatNumberField, new JLabel("예매번호:"), bookingNumberField,
				new JLabel("발권여부:"), ticketIssuedCheckBox, new JLabel("표준가격:"), standardPriceField, new JLabel("판매가격:"),
				sellingPriceField);

		JPanel buttonPanel = createButtonPanel(e -> {
			int scheduleNumber = Integer.parseInt(scheduleNumberField.getText());
			int seatNumber = Integer.parseInt(seatNumberField.getText());
			int bookingNumber = Integer.parseInt(bookingNumberField.getText());
			boolean ticketIssued = ticketIssuedCheckBox.isSelected();
			int standardPrice = Integer.parseInt(standardPriceField.getText());
			int sellingPrice = Integer.parseInt(sellingPriceField.getText());

			try {
				insertTicket(scheduleNumber, seatNumber, bookingNumber, ticketIssued, standardPrice, sellingPrice);
			} catch (SQLException ex) {
				// TODO Auto-generated catch block
				Utils.showMessage("에러: " + ex.getMessage());
				currentFrame.dispose();
			}
		}, e -> currentFrame.dispose());

		currentFrame.add(buttonPanel);
	}

	private void createInputFormForSeats() {
		JTextField theaterNumberField = new JTextField();
		JTextField screeningDateField = new JTextField();
		JCheckBox seatUseCheckBox = new JCheckBox();

		addInputFields(currentFrame, new JLabel("상영관번호:"), theaterNumberField, new JLabel("상영일정번호:"),
				screeningDateField, new JLabel("좌석사용여부:"), seatUseCheckBox);

		JPanel buttonPanel = createButtonPanel(e -> {
			int theaterNumber = Integer.parseInt(theaterNumberField.getText());
			int screeningId = Integer.parseInt(screeningDateField.getText());
			boolean seatUse = seatUseCheckBox.isSelected();

			try {
				insertSeat(theaterNumber, screeningId, seatUse);
				Utils.showMessage("저장되었습니다.");
				currentFrame.dispose();
			} catch (SQLException ex) {
				// TODO Auto-generted catch block
				Utils.showMessage("에러: " + ex.getMessage());
			}
		}, e -> currentFrame.dispose());

		currentFrame.add(buttonPanel);
	}

	private void createInputFormForCustomers() {
		JTextField memberIdField = new JTextField();
		JTextField customerNameField = new JTextField();
		JTextField phoneNumberField = new JTextField();
		JTextField emailField = new JTextField();

		addInputFields(currentFrame, new JLabel("회원아이디:"), memberIdField, new JLabel("고객명:"), customerNameField,
				new JLabel("휴대폰번호:"), phoneNumberField, new JLabel("전자메일주소:"), emailField);
		JPanel buttonPanel = createButtonPanel(e -> {
			String memberId = memberIdField.getText();
			String customerName = customerNameField.getText();
			String phoneNumber = phoneNumberField.getText();
			String email = emailField.getText();

			try {
				insertCustomer(memberId, customerName, phoneNumber, email);
				Utils.showMessage("저장되었습니다.");
				currentFrame.dispose();
			} catch (SQLException ex) {
				// TODO Auto-generated catch block
				Utils.showMessage("에러: " + ex.getMessage());
			}
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

			try {
				insertBooking(paymentMethod, paymentStatus, paymentAmount, memberId, paymentDate);
				Utils.showMessage("저장되었습니다.");
				currentFrame.dispose();
			} catch (SQLException ex) {
				// TODO Auto-generated catch block
				Utils.showMessage("에러: " + ex.getMessage());
			}
		}, e -> currentFrame.dispose());

		currentFrame.add(buttonPanel);
	}

	// Movies 테이블에 대한 CRUD 메소드
	public int insertMovie(String title, int movieTime, String rating, String director, String genre,
			String introduction, String releaseDate, double score) throws SQLException {
		String sql = "INSERT INTO Movies (Title, MovieTime, Rating, Director, Genre, Introduction, ReleaseDate, Score) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setString(1, title);
			pstmt.setInt(2, movieTime);
			pstmt.setString(3, rating);
			pstmt.setString(4, director);
			pstmt.setString(5, genre);
			pstmt.setString(6, introduction);
			pstmt.setDate(7, java.sql.Date.valueOf(releaseDate));
			pstmt.setDouble(8, score);
			pstmt.executeUpdate();
			ResultSet generatedKeys = pstmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				return generatedKeys.getInt(1);
			} else {
				throw new SQLException("Creating movie failed, no ID obtained.");
			}
		}
	}

	public void updateMovie(int movieId, String title, int movieTime, String rating, String director, String genre,
			String introduction, String releaseDate, double score) throws SQLException {
		String sql = "UPDATE Movies SET Title = ?, MovieTime = ?, Rating = ?, Director = ?, Genre = ?, Introduction = ?, ReleaseDate = ?, Score = ? WHERE MovieID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, title);
			pstmt.setInt(2, movieTime);
			pstmt.setString(3, rating);
			pstmt.setString(4, director);
			pstmt.setString(5, genre);
			pstmt.setString(6, introduction);
			pstmt.setDate(7, java.sql.Date.valueOf(releaseDate));
			pstmt.setDouble(8, score);
			pstmt.setInt(9, movieId);
			pstmt.executeUpdate();
		}
	}

	public void deleteMovie(int movieId) throws SQLException {
		String sql = "DELETE FROM Movies WHERE MovieID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, movieId);
			pstmt.executeUpdate();
		}
	}

	public ResultSet getMovie(int movieId) throws SQLException {
		String sql = "SELECT * FROM Movies WHERE MovieID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, movieId);
			return pstmt.executeQuery();
		}
	}

	// Actors 테이블에 대한 CRUD 메소드
	public int insertActor(String actorName) throws SQLException {
		String sql = "INSERT INTO Actors (ActorName) VALUES (?) ON DUPLICATE KEY UPDATE ActorID=LAST_INSERT_ID(ActorID)";
		try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setString(1, actorName);
			pstmt.executeUpdate();
			ResultSet generatedKeys = pstmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				return generatedKeys.getInt(1);
			} else {
				throw new SQLException("Creating actor failed, no ID obtained.");
			}
		}
	}

	public void updateActor(int actorId, String actorName) throws SQLException {
		String sql = "UPDATE Actors SET ActorName = ? WHERE ActorID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, actorName);
			pstmt.setInt(2, actorId);
			pstmt.executeUpdate();
		}
	}

	public void deleteActor(int actorId) throws SQLException {
		String sql = "DELETE FROM Actors WHERE ActorID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, actorId);
			pstmt.executeUpdate();
		}
	}

	public ResultSet getActor(int actorId) throws SQLException {
		String sql = "SELECT * FROM Actors WHERE ActorID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, actorId);
			return pstmt.executeQuery();
		}
	}

	// MovieActors 테이블에 대한 CRUD 메소드
	public void insertMovieActor(int movieId, int actorId) throws SQLException {
		String sql = "INSERT INTO MovieActors (MovieID, ActorID) VALUES (?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, movieId);
			pstmt.setInt(2, actorId);
			pstmt.executeUpdate();
		}
	}

	public void deleteMovieActor(int movieId, int actorId) throws SQLException {
		String sql = "DELETE FROM MovieActors WHERE MovieID = ? AND ActorID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, movieId);
			pstmt.setInt(2, actorId);
			pstmt.executeUpdate();
		}
	}

	public ResultSet getMovieActors(int movieId) throws SQLException {
		String sql = "SELECT * FROM MovieActors WHERE MovieID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, movieId);
			return pstmt.executeQuery();
		}
	}

	// Screenings 테이블에 대한 CRUD 메소드
	public int insertScreening(int movieId, int theaterId, String startDate, String screeningDate, int session,
			String startTime) throws SQLException {
		String sql = "INSERT INTO Screenings (MovieID, TheaterID, ScreeningStartDate, ScreeningDate, SessionNumber, StartTime) VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, movieId);
			pstmt.setInt(2, theaterId);
			pstmt.setDate(3, java.sql.Date.valueOf(startDate));
			pstmt.setDate(4, java.sql.Date.valueOf(screeningDate));
			pstmt.setInt(5, session);
			pstmt.setTime(6, java.sql.Time.valueOf(startTime));
			pstmt.executeUpdate();
			ResultSet generatedKeys = pstmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				return generatedKeys.getInt(1);
			} else {
				throw new SQLException("Creating screening failed, no ID obtained.");
			}
		}
	}

	public void updateScreening(int screeningId, int movieId, int theaterId, String startDate, String screeningDate,
			int session, String startTime) throws SQLException {
		String sql = "UPDATE Screenings SET MovieID = ?, TheaterID = ?, StartDate = ?, ScreeningDate = ?, Session = ?, StartTime = ? WHERE ScreeningID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, movieId);
			pstmt.setInt(2, theaterId);
			pstmt.setDate(3, java.sql.Date.valueOf(startDate));
			pstmt.setDate(4, java.sql.Date.valueOf(screeningDate));
			pstmt.setInt(5, session);
			pstmt.setTime(6, java.sql.Time.valueOf(startTime));
			pstmt.setInt(7, screeningId);
			pstmt.executeUpdate();
		}
	}

	public void deleteScreening(int screeningId) throws SQLException {
		String sql = "DELETE FROM Screenings WHERE ScreeningID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, screeningId);
			pstmt.executeUpdate();
		}
	}

	public ResultSet getScreening(int screeningId) throws SQLException {
		String sql = "SELECT * FROM Screenings WHERE ScreeningID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, screeningId);
			return pstmt.executeQuery();
		}
	}

	// Seats 테이블에 대한 CRUD 메소드
	public void insertSeat(int theaterId, int screeningId, boolean isAvailable) throws SQLException {
		String sql = "INSERT INTO Seats (TheaterID, ScreeningID, IsActive) VALUES (?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, theaterId);
			pstmt.setInt(2, screeningId);
			pstmt.setBoolean(3, isAvailable);
			pstmt.executeUpdate();
		}

	}

	public void updateSeat(int seatId, boolean isAvailable) throws SQLException {
		String sql = "UPDATE Seats SET IsAvailable = ? WHERE SeatID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setBoolean(1, isAvailable);
			pstmt.setInt(2, seatId);
			pstmt.executeUpdate();
		}
	}

	public void deleteSeat(int seatId) throws SQLException {
		String sql = "DELETE FROM Seats WHERE SeatID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, seatId);
			pstmt.executeUpdate();
		}
	}

	public ResultSet getSeat(int seatId) throws SQLException {
		String sql = "SELECT * FROM Seats WHERE SeatID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, seatId);
			return pstmt.executeQuery();
		}
	}

	// Theaters 테이블에 대한 CRUD 메소드
	public int insertTheater(int theaterName, int totalSeats, int horizontalSeats, int verticalSeats)
			throws SQLException {
		String sql = "INSERT INTO Theaters (TheaterId, NumberOfSeats, HorizontalSeats, VerticalSeats) VALUES (?, ?, ? , ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, theaterName);
			pstmt.setInt(2, totalSeats);
			pstmt.setInt(3, horizontalSeats);
			pstmt.setInt(4, verticalSeats);
			return pstmt.executeUpdate();
		}
	}

	public void updateTheater(int theaterId, String theaterName, int totalSeats) throws SQLException {
		String sql = "UPDATE Theaters SET TheaterName = ?, TotalSeats = ? WHERE TheaterID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, theaterName);
			pstmt.setInt(2, totalSeats);
			pstmt.setInt(3, theaterId);
			pstmt.executeUpdate();
		}
	}

	public void deleteTheater(int theaterId) throws SQLException {
		String sql = "DELETE FROM Theaters WHERE TheaterID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, theaterId);
			pstmt.executeUpdate();
		}
	}

	public ResultSet getTheater(int theaterId) throws SQLException {
		String sql = "SELECT * FROM Theaters WHERE TheaterID = ?";
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, theaterId);
			resultSet = pstmt.executeQuery();
			return resultSet;
		} catch (SQLException ex) {
			throw ex;
		}
	}

	// Tickets 테이블에 대한 CRUD 메소드
	public int insertTicket(int screeningId, int seatId, int bookingId, boolean isTicketing, int standardPrice,
			int salePrice) throws SQLException {
		String sql = "INSERT INTO Tickets (ScreeningID, SeatID, BookingID, IsTicketing, StandardPrice, SalePrice) VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, screeningId);
			pstmt.setInt(2, seatId);
			pstmt.setDouble(3, bookingId);
			pstmt.setBoolean(4, isTicketing);
			pstmt.setInt(5, standardPrice);
			pstmt.setInt(6, salePrice);
			pstmt.executeUpdate();
			ResultSet generatedKeys = pstmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				return generatedKeys.getInt(1);
			} else {
				throw new SQLException("Creating ticket failed, no ID obtained.");
			}
		}
	}

	public void updateTicket(int ticketId, int screeningId, int seatId, double price, boolean isPurchased)
			throws SQLException {
		String sql = "UPDATE Tickets SET ScreeningID = ?, SeatID = ?, Price = ?, IsPurchased = ? WHERE TicketID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, screeningId);
			pstmt.setInt(2, seatId);
			pstmt.setDouble(3, price);
			pstmt.setBoolean(4, isPurchased);
			pstmt.setInt(5, ticketId);
			pstmt.executeUpdate();
		}
	}

	public void deleteTicket(int ticketId) throws SQLException {
		String sql = "DELETE FROM Tickets WHERE TicketID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, ticketId);
			pstmt.executeUpdate();
		}
	}

	public ResultSet getTicket(int ticketId) throws SQLException {
		String sql = "SELECT * FROM Tickets WHERE TicketID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, ticketId);
			return pstmt.executeQuery();
		}
	}

	// Bookings 테이블에 대한 CRUD 메소드
	public int insertBooking(String paymentMethod, String paymentStatus, int amount, String customerId,
			String bookingDate) throws SQLException {
		String sql = "INSERT INTO Bookings (Payment,PaymentStatus, Amount, CustomerID, PaymentDate) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setString(1, paymentMethod);
			pstmt.setString(2, paymentStatus);
			pstmt.setInt(3, amount);
			pstmt.setString(4, customerId);
			pstmt.setDate(5, java.sql.Date.valueOf(bookingDate));
			pstmt.executeUpdate();
			ResultSet generatedKeys = pstmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				return generatedKeys.getInt(1);
			} else {
				throw new SQLException("Creating booking failed, no ID obtained.");
			}
		}
	}

	public void updateBooking(int bookingId, int customerId, int ticketId, String bookingDate) throws SQLException {
		String sql = "UPDATE Bookings SET CustomerID = ?, TicketID = ?, BookingDate = ? WHERE BookingID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, customerId);
			pstmt.setInt(2, ticketId);
			pstmt.setDate(3, java.sql.Date.valueOf(bookingDate));
			pstmt.setInt(4, bookingId);
			pstmt.executeUpdate();
		}
	}

	public void deleteBooking(int bookingId) throws SQLException {
		String sql = "DELETE FROM Bookings WHERE BookingID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, bookingId);
			pstmt.executeUpdate();
		}
	}

	public ResultSet getBooking(int bookingId) throws SQLException {
		String sql = "SELECT * FROM Bookings WHERE BookingID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, bookingId);
			return pstmt.executeQuery();
		}
	}

	// Customers 테이블에 대한 CRUD 메소드
	public int insertCustomer(String customerId, String customerName, String phone, String email) throws SQLException {
		String sql = "INSERT INTO Customers (CustomerID, CustomerName, PhoneNumber, Email) VALUES (?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setString(1, customerId);
			pstmt.setString(2, customerName);
			pstmt.setString(3, phone);
			pstmt.setString(4, email);
			return pstmt.executeUpdate();
		}
	}

	public void updateCustomer(int customerId, String customerName, String phone, String email) throws SQLException {
		String sql = "UPDATE Customers SET CustomerName = ?, Phone = ?, Email = ? WHERE CustomerID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, customerName);
			pstmt.setString(2, phone);
			pstmt.setString(3, email);
			pstmt.setInt(4, customerId);
			pstmt.executeUpdate();
		}
	}

	public void deleteCustomer(int customerId) throws SQLException {
		String sql = "DELETE FROM Customers WHERE CustomerID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, customerId);
			pstmt.executeUpdate();
		}
	}

	public ResultSet getCustomer(int customerId) throws SQLException {
		String sql = "SELECT * FROM Customers WHERE CustomerID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, customerId);
			return pstmt.executeQuery();
		}
	}

	// TheaterUse 테이블에 대한 CRUD 메소드
	public int insertTheaterUse(int theaterId, int movieId, Boolean useDate) throws SQLException {
		String sql = "INSERT INTO TheaterUse (TheaterID, ScreeningID, TheaterUse) VALUES (?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, theaterId);
			pstmt.setInt(2, movieId);
			pstmt.setBoolean(3, useDate);
			return pstmt.executeUpdate();
		}
	}

	public void updateTheaterUse(int theaterUseId, int theaterId, int movieId, String useDate) throws SQLException {
		String sql = "UPDATE TheaterUse SET TheaterID = ?, MovieID = ?, UseDate = ? WHERE TheaterUseID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, theaterId);
			pstmt.setInt(2, movieId);
			pstmt.setDate(3, java.sql.Date.valueOf(useDate));
			pstmt.setInt(4, theaterUseId);
			pstmt.executeUpdate();
		}
	}

	public void deleteTheaterUse(int theaterUseId) throws SQLException {
		String sql = "DELETE FROM TheaterUse WHERE TheaterUseID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, theaterUseId);
			pstmt.executeUpdate();
		}
	}

	public ResultSet getTheaterUse(int theaterUseId) throws SQLException {
		String sql = "SELECT * FROM TheaterUse WHERE TheaterUseID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, theaterUseId);
			return pstmt.executeQuery();
		}
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
