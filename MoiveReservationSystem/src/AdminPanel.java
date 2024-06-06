import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.sql.*;

public class AdminPanel extends JPanel {
	private Connection dbConnection; // 데이터베이스 연결 객체 저장을 위한 변수

	public AdminPanel(Connection dbConnection) { // 생성자에서 데이터베이스 연결 객체를 받음
		this.dbConnection = dbConnection;

		setLayout(new BorderLayout());

		JButton initButton = new JButton("초기화");
		JButton viewAllButton = new JButton("전체 테이블 조회");
		JButton insertButton = new JButton("입력");
		JButton deleteButton = new JButton("삭제");
		JButton updateButton = new JButton("변경");
		JButton backButton = new JButton("뒤로가기");

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(initButton);
		buttonPanel.add(viewAllButton);
		buttonPanel.add(insertButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(updateButton);
		buttonPanel.add(backButton);

		add(buttonPanel, BorderLayout.NORTH);

		// 각 버튼에 대한 액션 리스너 구현
		initButton.addActionListener(e -> initializeDatabase());
		viewAllButton.addActionListener(e -> viewAllTables());
		insertButton.addActionListener(e -> new MovieInputComponent(dbConnection));
		deleteButton.addActionListener(e -> new MovieDeleteComponent(dbConnection));
		updateButton.addActionListener(e -> new MovieUpdateComponent(dbConnection));
		backButton.addActionListener(e -> Utils.switchToPanel(this, new MainPanel()));
	}

	private void initializeDatabase() {
		try {
			dropTables();
			createTables();
			insertSampleData();
			JOptionPane.showMessageDialog(this, "데이터베이스 초기화가 완료되었습니다.");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "데이터베이스 초기화 중 오류가 발생했습니다: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void dropTables() throws SQLException {
		Statement stmt = null;
		ResultSet tables = null;
		try {
			stmt = dbConnection.createStatement();
			stmt.executeUpdate("SET foreign_key_checks = 0"); // 외래키 제약조건 해제

			// 데이터베이스 메타데이터를 통해 테이블 목록 가져오기
			DatabaseMetaData metaData = dbConnection.getMetaData();
			String catalog = dbConnection.getCatalog();
			Utils.showMessage(catalog);
			tables = metaData.getTables(catalog, null, "%", new String[] { "TABLE" });

			// 모든 테이블 드롭하기
			while (tables.next()) {
				String tableName = tables.getString("TABLE_NAME");
				String dropQuery = "DROP TABLE IF EXISTS " + tableName;
				Utils.showMessage(tableName); // 테스트용: 찾아낸 테이블 이름 출력
				stmt.executeUpdate(dropQuery);

			}

			stmt.executeUpdate("SET foreign_key_checks = 1"); // 외래키 제약조건 재설정
		} finally {
			if (tables != null) {
				tables.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	private void createTables() throws SQLException {
		String[] createTableQueries = {
				"CREATE TABLE Actors (" + "    ActorID INT PRIMARY KEY AUTO_INCREMENT," + "    ActorName VARCHAR(255)"
						+ ");",

				"CREATE TABLE Customers (" + "    CustomerID VARCHAR(255) PRIMARY KEY,"
						+ "    CustomerName VARCHAR(255)," + "    PhoneNumber VARCHAR(15)," + "    Email VARCHAR(255)"
						+ ");",

				"CREATE TABLE Movies (" + "    MovieID INT PRIMARY KEY AUTO_INCREMENT,"
						+ "    Title VARCHAR(255) NOT NULL," + "    MovieTime INT NOT NULL,"
						+ "    Rating VARCHAR(255)," + "    Director VARCHAR(255)," + "    Genre VARCHAR(255),"
						+ "    Introduction VARCHAR(255)," + "    ReleaseDate DATE," + "    Score DECIMAL(2,1)" + ");",

				"CREATE TABLE Theaters (" + "    TheaterID INT PRIMARY KEY," + "    NumberOfSeats INT,"
						+ "    HorizontalSeats INT," + "    VerticalSeats INT" + ");",

				"CREATE TABLE Screenings (" + "    ScreeningID INT PRIMARY KEY AUTO_INCREMENT," + "    MovieID INT,"
						+ "    TheaterID INT," + "    ScreeningStartDate DATE," + "    ScreeningDate DATE,"
						+ "    SessionNumber INT," + "    StartTime TIME,"
						+ "    FOREIGN KEY (MovieID) REFERENCES Movies(MovieID) ON DELETE CASCADE ON UPDATE CASCADE,"
						+ "    FOREIGN KEY (TheaterID) REFERENCES Theaters(TheaterID) ON DELETE CASCADE ON UPDATE CASCADE"
						+ ");",

				"CREATE TABLE Seats (" + "    SeatID INT PRIMARY KEY AUTO_INCREMENT," + "    TheaterID INT,"
						+ "    ScreeningID INT," + "    IsActive BOOLEAN,"
						+ "    FOREIGN KEY (TheaterID) REFERENCES Theaters(TheaterID) ON DELETE CASCADE ON UPDATE CASCADE,"
						+ "    FOREIGN KEY (ScreeningID) REFERENCES Screenings(ScreeningID) ON DELETE CASCADE ON UPDATE CASCADE"
						+ ");",

				"CREATE TABLE TheaterUse (" + "    TheaterID INT," + "    ScreeningID INT," + "    TheaterUse BOOLEAN,"
						+ "    PRIMARY KEY (TheaterID, ScreeningID),"
						+ "    FOREIGN KEY (TheaterID) REFERENCES Theaters(TheaterID) ON DELETE CASCADE ON UPDATE CASCADE,"
						+ "    FOREIGN KEY (ScreeningID) REFERENCES Screenings(ScreeningID) ON DELETE CASCADE ON UPDATE CASCADE"
						+ ");",

				"CREATE TABLE Bookings (" + "    BookingID INT PRIMARY KEY AUTO_INCREMENT," + "    Payment VARCHAR(50),"
						+ "    PaymentStatus VARCHAR(50)," + "    Amount INT," + "    CustomerID VARCHAR(255),"
						+ "    PaymentDate DATE,"
						+ "    FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID) ON DELETE CASCADE ON UPDATE CASCADE"
						+ ");",

				"CREATE TABLE Tickets (" + "    TicketID INT PRIMARY KEY AUTO_INCREMENT," + "    ScreeningID INT,"
						+ "    SeatID INT," + "    BookingID INT," + "    IsTicketing BOOLEAN,"
						+ "    StandardPrice INT," + "    SalePrice INT,"
						+ "    FOREIGN KEY (ScreeningID) REFERENCES Screenings(ScreeningID) ON DELETE CASCADE ON UPDATE CASCADE, "
						+ "    FOREIGN KEY (SeatID) REFERENCES Seats(SeatID) ON DELETE CASCADE ON UPDATE CASCADE,"
						+ "    FOREIGN KEY (BookingID) REFERENCES Bookings(BookingID) ON DELETE CASCADE ON UPDATE CASCADE"
						+ ");",

				"CREATE TABLE MovieActors (" + "    MovieID INT," + "    ActorID INT,"
						+ "    PRIMARY KEY (MovieID, ActorID),"
						+ "    FOREIGN KEY (MovieID) REFERENCES Movies(MovieID) ON DELETE CASCADE ON UPDATE CASCADE,"
						+ "    FOREIGN KEY (ActorID) REFERENCES Actors(ActorID) ON DELETE CASCADE ON UPDATE CASCADE"
						+ ");" };

<<<<<<< Updated upstream
		Statement stmt = null;
		try {
			stmt = dbConnection.createStatement();
			for (String query : createTableQueries) {
				stmt.executeUpdate(query);
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}
=======
    private void insertSampleData() throws SQLException {
        String insertActors = 
        	    "INSERT INTO Actors (ActorName) VALUES " +
        	    "('마동석'), ('윤계상'), ('손석구'), ('최민식'), " +
        	    "('류승룡'), ('이정재'), ('전지현'), ('강동원'), " +
        	    "('황정민'), ('유아인'), ('배두나'), ('박보영')";
>>>>>>> Stashed changes

	private void insertSampleData() throws SQLException {
		String insertActors = "INSERT INTO Actors (ActorName) VALUES " + "('마동석'), ('윤계상'), ('손석구'), ('최민식'), "
				+ "('류승룡'), ('이정재'), ('전지현'), ('강동원'), " + "('황정민'), ('유아인'), ('배두나'), ('박보영')";

<<<<<<< Updated upstream
		String insertCustomers = "INSERT INTO Customers (CustomerID, CustomerName, PhoneNumber, Email) VALUES "
				+ "('user1', 'Customer 1', '123-456-7890', 'customer1@example.com'), "
				+ "('user2', 'Customer 2', '123-456-7891', 'customer2@example.com'), "
				+ "('user3', 'Customer 3', '123-456-7892', 'customer3@example.com'), "
				+ "('user4', 'Customer 4', '123-456-7893', 'customer4@example.com'), "
				+ "('user5', 'Customer 5', '123-456-7894', 'customer5@example.com'), "
				+ "('user6', 'Customer 6', '123-456-7895', 'customer6@example.com'), "
				+ "('user7', 'Customer 7', '123-456-7896', 'customer7@example.com'), "
				+ "('user8', 'Customer 8', '123-456-7897', 'customer8@example.com'), "
				+ "('user9', 'Customer 9', '123-456-7898', 'customer9@example.com'), "
				+ "('user10', 'Customer 10', '123-456-7899', 'customer10@example.com'), "
				+ "('user11', 'Customer 11', '123-456-7800', 'customer11@example.com'), "
				+ "('user12', 'Customer 12', '123-456-7801', 'customer12@example.com')";
=======
        String insertMovies = 
        	    "INSERT INTO Movies (Title, MovieTime, Rating, Director, Genre, Introduction, ReleaseDate, Score) VALUES " +
        	    "('범죄도시1', 120, 'PG-13', '마동석', 'Action', 'Introduction 1', '2023-01-01', 8.5), " +
        	    "('범죄도시2', 110, 'R', '마동석', 'Action', 'Introduction 2', '2023-02-01', 7.5), " +
        	    "('명량', 130, 'PG', '김한민', 'Action', 'Introduction 3', '2023-03-01', 6.5), " +
        	    "('도둑들', 140, 'PG-13', '최동훈', 'Comedy', 'Introduction 4', '2023-04-01', 8.0), " +
        	    "('엽기적인 그녀', 150, 'R', '곽재용', 'Drama', 'Introduction 5', '2023-05-01', 9.0), " +
        	    "('골든 슬럼버', 95, 'PG', '노동석', 'Drama', 'Introduction 6', '2023-06-01', 7.0), " +
        	    "('전우치', 105, 'PG-13', '최동훈', 'Comedy', 'Introduction 7', '2023-07-01', 6.0), " +
        	    "('검사외전', 115, 'R', '이일형', 'Comedy', 'Introduction 8', '2023-08-01', 5.5), " +
        	    "('신세계', 125, 'PG', '박훈정', 'Action', 'Introduction 9', '2023-09-01', 8.0), " +
        	    "('베테랑', 135, 'PG-13', '류승완', 'Action', 'Introduction 10', '2023-10-01', 7.5), " +
        	    "('괴물', 145, 'R', '봉준호', 'Horror', 'Introduction 11', '2023-11-01', 9.5), " +
        	    "('과속스캔들', 155, 'PG', '강형철', 'Drama', 'Introduction 12', '2023-12-01', 6.5)";
>>>>>>> Stashed changes

		String insertMovies = "INSERT INTO Movies (Title, MovieTime, Rating, Director, Genre, Introduction, ReleaseDate, Score) VALUES "
				+ "('범죄도시1', 120, 'PG-13', '마동석', 'Action', 'Introduction 1', '2023-01-01', 8.5), "
				+ "('범죄도시2', 110, 'R', '마동석', 'Action', 'Introduction 2', '2023-02-01', 7.5), "
				+ "('명량', 130, 'PG', '김한민', 'Action', 'Introduction 3', '2023-03-01', 6.5), "
				+ "('도둑들', 140, 'PG-13', '최동훈', 'Comedy', 'Introduction 4', '2023-04-01', 8.0), "
				+ "('엽기적인 그녀', 150, 'R', '곽재용', 'Drama', 'Introduction 5', '2023-05-01', 9.0), "
				+ "('골든 슬럼버', 95, 'PG', '노동석', 'Drama', 'Introduction 6', '2023-06-01', 7.0), "
				+ "('전우치', 105, 'PG-13', '최동훈', 'Comedy', 'Introduction 7', '2023-07-01', 6.0), "
				+ "('검사외전', 115, 'R', '이일형', 'Comedy', 'Introduction 8', '2023-08-01', 5.5), "
				+ "('신세계', 125, 'PG', '박훈정', 'Action', 'Introduction 9', '2023-09-01', 8.0), "
				+ "('베테랑', 135, 'PG-13', '류승완', 'Action', 'Introduction 10', '2023-10-01', 7.5), "
				+ "('괴물', 145, 'R', '봉준호', 'Horror', 'Introduction 11', '2023-11-01', 9.5), "
				+ "('과속스캔들', 155, 'PG', '강형철', 'Drama', 'Introduction 12', '2023-12-01', 6.5)";

<<<<<<< Updated upstream
		String insertTheaters = "INSERT INTO Theaters (TheaterID, NumberOfSeats, HorizontalSeats, VerticalSeats) VALUES "
				+ "(1, 20, 5, 4), (2, 20, 5, 4), (3, 20, 5, 4), (4, 20, 5, 4), "
				+ "(5, 20, 5, 4), (6, 20, 5, 4), (7, 20, 5, 4), (8, 20, 5, 4), "
				+ "(9, 20, 5, 4), (10, 20, 5, 4), (11, 20, 5, 4), (12, 20, 5, 4)";
=======
        String insertScreenings = 
        	    "INSERT INTO Screenings (MovieID, TheaterID, ScreeningStartDate, ScreeningDate, SessionNumber, StartTime) VALUES " +
        	    "(1, 1, '2023-01-01', '2024-01-02', 1, '10:00:00'), " +
        	    "(2, 2, '2023-02-01', '2024-02-02', 1, '11:00:00'), " +
        	    "(3, 3, '2023-03-01', '2024-03-02', 1, '12:00:00'), " +
        	    "(4, 4, '2023-04-01', '2024-04-02', 1, '13:00:00'), " +
        	    "(5, 5, '2023-05-01', '2024-05-02', 1, '14:00:00'), " +
        	    "(6, 6, '2023-06-01', '2024-06-02', 1, '15:00:00'), " +
        	    "(7, 7, '2023-07-01', '2024-07-02', 1, '16:00:00'), " +
        	    "(8, 8, '2023-08-01', '2024-08-02', 1, '17:00:00'), " +
        	    "(9, 9, '2023-09-01', '2024-09-02', 1, '18:00:00'), " +
        	    "(10, 10, '2023-10-01', '2024-10-02', 1, '19:00:00'), " +
        	    "(11, 11, '2023-11-01', '2024-11-02', 1, '20:00:00'), " +
        	    "(12, 12, '2023-12-01', '2024-12-02', 1, '21:00:00')";
        
        String insertTheaterUse = 
        	    "INSERT INTO TheaterUse (TheaterID, ScreeningID, TheaterUse) " +
        	    "SELECT t.TheaterID, s.ScreeningID, FALSE " +
        	    "FROM Theaters t " +
        	    "CROSS JOIN Screenings s";
>>>>>>> Stashed changes

		String insertScreenings = "INSERT INTO Screenings (MovieID, TheaterID, ScreeningStartDate, ScreeningDate, SessionNumber, StartTime) VALUES "
				+ "(1, 1, '2023-01-01', '2024-01-02', 1, '10:00:00'), "
				+ "(2, 2, '2023-02-01', '2024-02-02', 1, '11:00:00'), "
				+ "(3, 3, '2023-03-01', '2024-03-02', 1, '12:00:00'), "
				+ "(4, 4, '2023-04-01', '2024-04-02', 1, '13:00:00'), "
				+ "(5, 5, '2023-05-01', '2024-05-02', 1, '14:00:00'), "
				+ "(6, 6, '2023-06-01', '2024-06-02', 1, '15:00:00'), "
				+ "(7, 7, '2023-07-01', '2024-07-02', 1, '16:00:00'), "
				+ "(8, 8, '2023-08-01', '2024-08-02', 1, '17:00:00'), "
				+ "(9, 9, '2023-09-01', '2024-09-02', 1, '18:00:00'), "
				+ "(10, 10, '2023-10-01', '2024-10-02', 1, '19:00:00'), "
				+ "(11, 11, '2023-11-01', '2024-11-02', 1, '20:00:00'), "
				+ "(12, 12, '2023-12-01', '2024-12-02', 1, '21:00:00')";

<<<<<<< Updated upstream
		String insertTheaterUse = "INSERT INTO TheaterUse (TheaterID, ScreeningID, TheaterUse) "
				+ "SELECT t.TheaterID, s.ScreeningID, FALSE " + "FROM Theaters t " + "CROSS JOIN Screenings s";
=======
        String insertBookings = 
        	    "INSERT INTO Bookings (Payment, PaymentStatus, Amount, CustomerID, PaymentDate) VALUES " +
        	    "('신한카드', '일시불', 13500, 'user1', '2023-01-15'), " +
        	    "('국민카드', '2개월 할부', 15000, 'user1', '2023-02-20'), " +
        	    "('무통장입금', '일시불', 15000, 'user3', '2023-03-10'), " +
        	    "('신한카드', '일시불', 13500, 'user4', '2023-04-05'), " +
        	    "('삼성카드', '일시불', 15000, 'user5', '2023-05-25'), " +
        	    "('삼성카드', '3개월 할부', 15000, 'user6', '2023-06-15'), " +
        	    "('신한카드', '일시불', 13500, 'user7', '2023-07-30'), " +
        	    "('무통장입금', '일시불', 15000, 'user8', '2023-08-18'), " +
        	    "('영화관람권', '일시불', 15000, 'user9', '2023-09-22'), " +
        	    "('농협카드', '2개월 할부', 15000, 'user10', '2023-10-05'), " +
        	    "('계좌이체', '일시불', 15000, 'user11', '2023-11-11'), " +
        	    "('영화관람권', '일시불', 15000, 'user12', '2023-12-31')";
>>>>>>> Stashed changes

		String updateTheaterUse = "UPDATE TheaterUse tu "
				+ "JOIN Screenings s ON tu.TheaterID = s.TheaterID AND tu.ScreeningID = s.ScreeningID "
				+ "SET tu.TheaterUse = TRUE";

<<<<<<< Updated upstream
		String insertBookings = "INSERT INTO Bookings (Payment, PaymentStatus, Amount, CustomerID, PaymentDate) VALUES "
				+ "('신한카드', '일시불', 13500, 'user1', '2023-01-15'), "
				+ "('국민카드', '2개월 할부', 15000, 'user1', '2023-02-20'), "
				+ "('무통장입금', '일시불', 15000, 'user3', '2023-03-10'), " + "('신한카드', '일시불', 13500, 'user4', '2023-04-05'), "
				+ "('삼성카드', '일시불', 15000, 'user5', '2023-05-25'), "
				+ "('삼성카드', '3개월 할부', 15000, 'user6', '2023-06-15'), "
				+ "('신한카드', '일시불', 13500, 'user7', '2023-07-30'), " + "('무통장입금', '일시불', 15000, 'user8', '2023-08-18'), "
				+ "('영화관람권', '일시불', 15000, 'user9', '2023-09-22'), "
				+ "('농협카드', '2개월 할부', 15000, 'user10', '2023-10-05'), "
				+ "('계좌이체', '일시불', 15000, 'user11', '2023-11-11'), "
				+ "('영화관람권', '일시불', 15000, 'user12', '2023-12-31')";
=======
        String insertTickets = 
        	    "INSERT INTO Tickets (ScreeningID, SeatID, BookingID, IsTicketing, StandardPrice, SalePrice) VALUES " +
        	    "(1, 1, 1, TRUE, 15000, 13500), " +
        	    "(2, 21, 2, TRUE, 15000, 10000), " +
        	    "(3, 41, 3, TRUE, 15000, 10000), " +
        	    "(4, 61, 4, TRUE, 15000, 13500), " +
        	    "(5, 81, 5, TRUE, 15000, 10000), " +
        	    "(6, 101, 6, TRUE, 15000, 10000), " +
        	    "(7, 121, 7, TRUE, 15000, 13500), " +
        	    "(8, 141, 8, TRUE, 15000, 10000), " +
        	    "(9, 161, 9, TRUE, 15000, 10000), " +
        	    "(10, 181, 10, TRUE, 15000, 10000), " +
        	    "(11, 201, 11, TRUE, 15000, 10000), " +
        	    "(12, 221, 12, TRUE, 15000, 10000)";
        
        String updateSeats = 
                "UPDATE Seats " +
                "SET IsActive = TRUE " +
                "WHERE SeatID IN (SELECT SeatID FROM Tickets)";
        
        String insertMovieActors = 
        	    "INSERT INTO MovieActors (MovieID, ActorID) VALUES " +
        	    "(1, 1), (2, 1), " +
        	    "(1, 2), (6, 2), " +
        	    "(2, 3), " +
        	    "(3, 4), " +
        	    "(3, 5), " +
        	    "(4, 6), " +
        	    "(4, 7), (5, 7), " +
        	    "(6, 8), (7, 8), (8, 8), " +
        	    "(8, 9), (9, 9), (10, 9), " +
        	    "(10, 10), " +
        	    "(11, 11), " +
        	    "(12, 12)";
>>>>>>> Stashed changes

		String insertTickets = "INSERT INTO Tickets (ScreeningID, SeatID, BookingID, IsTicketing, StandardPrice, SalePrice) VALUES "
				+ "(1, 1, 1, TRUE, 15000, 13500), " + "(2, 21, 2, TRUE, 15000, 10000), "
				+ "(3, 41, 3, TRUE, 15000, 10000), " + "(4, 61, 4, TRUE, 15000, 13500), "
				+ "(5, 81, 5, TRUE, 15000, 10000), " + "(6, 101, 6, TRUE, 15000, 10000), "
				+ "(7, 121, 7, TRUE, 15000, 13500), " + "(8, 141, 8, TRUE, 15000, 10000), "
				+ "(9, 161, 9, TRUE, 15000, 10000), " + "(10, 181, 10, TRUE, 15000, 10000), "
				+ "(11, 201, 11, TRUE, 15000, 10000), " + "(12, 221, 12, TRUE, 15000, 10000)";

		String updateSeats = "UPDATE Seats " + "SET IsActive = TRUE " + "WHERE SeatID IN (SELECT SeatID FROM Tickets)";

		String insertMovieActors = "INSERT INTO MovieActors (MovieID, ActorID) VALUES " + "(1, 1), (2, 1), "
				+ "(1, 2), (6, 2), " + "(2, 3), " + "(3, 4), " + "(3, 5), " + "(4, 6), " + "(4, 7), (5, 7), "
				+ "(6, 8), (7, 8), (8, 8), " + "(8, 9), (9, 9), (10, 9), " + "(10, 10), " + "(11, 11), " + "(12, 12)";

		Statement stmt = null;

		try {
			stmt = dbConnection.createStatement();
			stmt.executeUpdate(insertActors);
			stmt.executeUpdate(insertCustomers);
			stmt.executeUpdate(insertMovies);
			stmt.executeUpdate(insertTheaters);
			stmt.executeUpdate(insertScreenings);

			insertSeats();

			stmt.executeUpdate(insertTheaterUse);
			stmt.executeUpdate(updateTheaterUse);

			stmt.executeUpdate(insertBookings);

			/*
			 * Ticket 샘플데이터 입력 시 Ticket에 있는 SeatID -> 활성화, Seats테이블에서 해당 하는 SeatID의
			 * IsActive를 true로 업데이트
			 */
			stmt.executeUpdate(insertTickets);
			stmt.executeUpdate(updateSeats);

			stmt.executeUpdate(insertMovieActors);

		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	private void insertSeats() throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = dbConnection.createStatement();
			String query = "SELECT ScreeningID, TheaterID FROM Screenings";
			rs = stmt.executeQuery(query);

			Map<Integer, Integer> screeningToTheaterMap = new HashMap<>();
			while (rs.next()) {
				int screeningID = rs.getInt("ScreeningID");
				int theaterID = rs.getInt("TheaterID");
				screeningToTheaterMap.put(screeningID, theaterID);
			}

			// Now, for each ScreeningID, get the number of seats from the Theater table
			for (Map.Entry<Integer, Integer> entry : screeningToTheaterMap.entrySet()) {
				int screeningID = entry.getKey();
				int theaterID = entry.getValue();

				// Query to get the seat count for the given TheaterID
				String seatQuery = "SELECT NumberOfSeats FROM Theaters WHERE TheaterID = " + theaterID;
				rs = stmt.executeQuery(seatQuery);

				if (rs.next()) {
					int seatCount = rs.getInt("NumberOfSeats");
					for (int i = 0; i < seatCount; i++) {
						String insertQuery = "INSERT INTO Seats (TheaterID, ScreeningID, IsActive) VALUES (" + theaterID
								+ ", " + screeningID + ", false)";
						stmt.executeUpdate(insertQuery);
					}
				}
			}

		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	private void viewAllTables() {
		Statement stmt = null;
		ResultSet tables = null;

		try {
			stmt = dbConnection.createStatement();

			// 데이터베이스 메타데이터를 통해 테이블 목록 가져오기
			DatabaseMetaData metaData = dbConnection.getMetaData();
			String catalog = dbConnection.getCatalog();
			tables = metaData.getTables(catalog, null, "%", new String[] { "TABLE" });

			// 새로운 JFrame 생성
			JFrame frame = new JFrame("전체 테이블 조회");
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setLayout(new BorderLayout());

			// 모든 테이블 데이터를 담을 JPanel
			JPanel panel = new JPanel();
			panel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.insets = new Insets(5, 5, 5, 5); // 상하좌우 간격 조정
			gbc.weightx = 1.0; // 수평으로 더 많은 공간을 차지하도록 설정
			gbc.weighty = 1.0; // 수직으로 더 많은 공간을 차지하도록 설정
			gbc.fill = GridBagConstraints.BOTH; // 사용 가능한 모든 공간을 채우도록 설정

			while (tables.next()) {
				String tableName = tables.getString("TABLE_NAME");

				// 각 테이블에 대한 데이터를 가져오기 위한 쿼리 실행
				ResultSet tableData = stmt.executeQuery("SELECT * FROM " + tableName);
				ResultSetMetaData tableMetaData = tableData.getMetaData();
				int columnCount = tableMetaData.getColumnCount();

				// 테이블의 컬럼 이름을 가져와서 JTable의 헤더로 사용
				String[] columnNames = new String[columnCount];
				for (int i = 1; i <= columnCount; i++) {
					columnNames[i - 1] = tableMetaData.getColumnName(i);
				}

				// 테이블의 데이터를 가져와서 JTable의 데이터로 사용
				DefaultTableModel model = new DefaultTableModel(columnNames, 0);
				while (tableData.next()) {
					Object[] rowData = new Object[columnCount];
					for (int i = 1; i <= columnCount; i++) {
						rowData[i - 1] = tableData.getObject(i);
					}
					model.addRow(rowData);
				}

				// JTable 생성 및 JScrollPane에 추가
				JTable table = new JTable(model);

				// 셀 렌더러를 중앙 정렬로 설정
				DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
				centerRenderer.setHorizontalAlignment(JLabel.CENTER);
				for (int i = 0; i < table.getColumnCount(); i++) {
					table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
				}

				JScrollPane scrollPane = new JScrollPane(table);
				scrollPane.setBorder(BorderFactory.createTitledBorder(tableName));

				// JPanel에 JScrollPane 추가
				panel.add(scrollPane, gbc);
				gbc.gridy++; // 다음 행으로 이동
			}

			// JFrame에 JPanel 추가
			frame.add(new JScrollPane(panel), BorderLayout.CENTER);
			frame.setSize(800, 600);
			frame.setVisible(true);

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "테이블 조회 중 오류가 발생했습니다: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} finally {
			try {
				if (tables != null) {
					tables.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// MovieInputComponent, MovieDeleteComponent, MovieUpdateComponent 클래스의
	// 생성자 및 메서드는 실제 구현에 따라 내용을 추가해야 함.
}
