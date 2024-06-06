import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class ViewUserBookings extends JFrame {
	private Connection dbConnection;
	private JTable bookingsTable;
	private String userId = "user1"; // 현재 사용자 ID, 예시로 "user1" 사용

	public ViewUserBookings(Connection dbConnection) {
		this.dbConnection = dbConnection;
		setTitle("본인이 예매한 영화 조회");
		setSize(800, 600);
		setLayout(new BorderLayout());

		JPanel buttonPanel = new JPanel();
		JButton deleteButton = new JButton("선택된 예매 삭제");
		JButton changeMovieButton = new JButton("선택된 예매 영화 변경");
		JButton changeScheduleButton = new JButton("선택된 예매 일정 변경");
		JButton viewDetailsButton = new JButton("추가 정보 조회");

		buttonPanel.add(deleteButton);
		buttonPanel.add(changeMovieButton);
		buttonPanel.add(changeScheduleButton);
		buttonPanel.add(viewDetailsButton);

		add(buttonPanel, BorderLayout.SOUTH);

		deleteButton.addActionListener(new DeleteBookingAction());
		changeMovieButton.addActionListener(new ChangeMovieAction());
		changeScheduleButton.addActionListener(new ChangeScheduleAction());
		viewDetailsButton.addActionListener(new ViewDetailsAction());

		loadBookings();

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void loadBookings() {
		try {
			String query = "SELECT b.BookingID, m.Title, s.ScreeningDate, s.TheaterID, t.SeatID, t.SalePrice "
					+ "FROM Bookings b " + "JOIN Tickets t ON b.BookingID = t.BookingID "
					+ "JOIN Screenings s ON t.ScreeningID = s.ScreeningID " + "JOIN Movies m ON s.MovieID = m.MovieID "
					+ "WHERE b.CustomerID = ?";
			PreparedStatement stmt = dbConnection.prepareStatement(query);
			stmt.setString(1, userId);
			ResultSet rs = stmt.executeQuery();
			String[] columnNames = { "예매번호", "영화제목", "상영일", "상영관번호", "좌석번호", "판매가격" };
			DefaultTableModel model = new DefaultTableModel(columnNames, 0);

			while (rs.next()) {
				model.addRow(new Object[] { rs.getInt("BookingId"), rs.getString("Title"), rs.getDate("ScreeningDate"),
						rs.getInt("TheaterID"), rs.getInt("SeatID"), rs.getDouble("SalePrice") });
			}

			bookingsTable = new JTable(model);
			bookingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			JScrollPane scrollPane = new JScrollPane(bookingsTable);
			add(scrollPane, BorderLayout.CENTER);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private class ViewDetailsAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedRow = bookingsTable.getSelectedRow();
			if (selectedRow != -1) {
				int bookingId = (int) bookingsTable.getValueAt(selectedRow, 0);
				showAdditionalInfo(bookingId);
			} else {
				JOptionPane.showMessageDialog(ViewUserBookings.this, "예매를 선택하세요.", "예매 선택 필요",
						JOptionPane.WARNING_MESSAGE);
			}
		}

		private void showAdditionalInfo(int bookingId) {
			JFrame frame = new JFrame("예매 추가 정보 조회");
			frame.setSize(700, 400);
			frame.setLayout(new BorderLayout());

			JTextArea detailsTextArea = new JTextArea();
			detailsTextArea.setEditable(false);
			JScrollPane scrollPane = new JScrollPane(detailsTextArea);

			frame.add(scrollPane, BorderLayout.CENTER);

			try {
				String query = "SELECT s.ScreeningDate, s.TheaterID "
						+ "FROM Screenings s JOIN Movies m ON s.MovieID = m.MovieID "
						+ "WHERE s.MovieID = (SELECT s.MovieID FROM Screenings s "
						+ "JOIN Tickets t ON t.ScreeningID = s.ScreeningID WHERE t.BookingID = ?)";
				PreparedStatement stmt = dbConnection.prepareStatement(query);
				stmt.setInt(1, bookingId);
				ResultSet rs = stmt.executeQuery();

				StringBuilder details = new StringBuilder();
				details.append("상영일\t상영관번호\n");

				while (rs.next()) {
					details.append(rs.getDate("ScreeningDate")).append("\t").append(rs.getInt("TheaterID"))
							.append("\n");
				}

				detailsTextArea.setText(details.toString());

				stmt.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}

			try {
				String query = "SELECT * FROM Tickets WHERE BookingID = ?";
				PreparedStatement stmt = dbConnection.prepareStatement(query);
				stmt.setInt(1, bookingId);
				ResultSet rs = stmt.executeQuery();

				StringBuilder details = new StringBuilder();
				details.append("티켓ID\t상영일정\t좌석번호\t예매번호\t발권여부\t표준가격\t판매가격\n");

				while (rs.next()) {
					details.append(rs.getInt("TicketID")).append("\t").append(rs.getInt("ScreeningID")).append("\t")
							.append(rs.getInt("SeatId")).append("\t").append(rs.getInt("BookingId")).append("\t")
							.append(rs.getBoolean("IsTicketing")).append("\t").append(rs.getDouble("StandardPrice"))
							.append("\t").append(rs.getDouble("SalePrice")).append("\n");
				}

				detailsTextArea.append("\n\n티켓 상세 정보:\n");
				detailsTextArea.append(details.toString());

				stmt.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}

			// 추가 정보를 표시하는 코드 추가
			// 예를 들어, 사용자가 예약한 영화관의 위치나 예약된 좌석의 위치 등을 표시할 수 있습니다.

			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		}
	}

	private class DeleteBookingAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedRow = bookingsTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(ViewUserBookings.this, "삭제할 예매를 선택하세요.");
				return;
			}

			int bookingId = (int) bookingsTable.getValueAt(selectedRow, 0);
			try {
				String deleteTicketQuery = "DELETE FROM Tickets WHERE BookingID = ?";
				PreparedStatement deleteTicketStmt = dbConnection.prepareStatement(deleteTicketQuery);
				deleteTicketStmt.setInt(1, bookingId);
				deleteTicketStmt.executeUpdate();

				String deleteBookingQuery = "DELETE FROM Bookings WHERE BookingID = ?";
				PreparedStatement deleteBookingStmt = dbConnection.prepareStatement(deleteBookingQuery);
				deleteBookingStmt.setInt(1, bookingId);
				deleteBookingStmt.executeUpdate();

				JOptionPane.showMessageDialog(ViewUserBookings.this, "예매가 삭제되었습니다.");
				loadBookings(); // 테이블 갱신
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	private class ChangeMovieAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedRow = bookingsTable.getSelectedRow();
			int bookingId = (int) bookingsTable.getValueAt(selectedRow, 0);
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(ViewUserBookings.this, "변경할 예매를 선택하세요.");
				return;
			}

			showAllMovies(bookingId);

		}
	}

	private class ChangeScheduleAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedRow = bookingsTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(ViewUserBookings.this, "변경할 예매를 선택하세요.");
				return;
			}
			String movieName = (String) bookingsTable.getValueAt(selectedRow, 1);
			int MovieId = -1;
			try {
				MovieId = getMovieIDFromName(movieName);
				int bookingId = (int) bookingsTable.getValueAt(selectedRow, 0);

			} catch (

			SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			int bookingId = (int) bookingsTable.getValueAt(selectedRow, 0);
			showScreeningList(MovieId, bookingId);
		}
	}

	public void showScreeningList(int movieId, int bookingId) {
		Statement stmt = null;
		ResultSet resultSet = null;

		try {
			stmt = dbConnection.createStatement();

			String query = "SELECT * FROM Screenings WHERE MovieID = " + movieId;
			resultSet = stmt.executeQuery(query);

			// 결과 데이터를 JTable에 추가
			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();
			String[] columnNames = new String[columnCount];
			for (int i = 1; i <= columnCount; i++) {
				columnNames[i - 1] = metaData.getColumnName(i);
			}

			DefaultTableModel model = new DefaultTableModel(columnNames, 0);

			boolean hasResults = false;

			while (resultSet.next()) {
				hasResults = true;
				Object[] rowData = new Object[columnCount];
				for (int i = 1; i <= columnCount; i++) {
					rowData[i - 1] = resultSet.getObject(i);
				}
				model.addRow(rowData);
			}

			JTable table = new JTable(model);
			JScrollPane scrollPane = new JScrollPane(table);

			table.setRowHeight(30); // 원하는 높이로 설정

			if (!hasResults) {
				JOptionPane.showMessageDialog(null, "해당하는 상영 정보가 없습니다", "정보", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			// 새로운 JFrame 생성
			JFrame frame = new JFrame("영화 상영정보 조회");
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setLayout(new BorderLayout());

			// 버튼 패널 생성
			JPanel buttonPanel = new JPanel();

			// 예매 버튼 추가 및 위치와 크기 설정
			JButton bookButton = new JButton("선택");
			bookButton.setPreferredSize(new Dimension(100, 25)); // 버튼 크기 설정
			buttonPanel.add(bookButton); // 패널에 예매 버튼 추가

			// 취소 버튼 추가 및 위치와 크기 설정
			JButton cancelButton = new JButton("취소");
			cancelButton.setPreferredSize(new Dimension(100, 25)); // 버튼 크기 설정
			buttonPanel.add(cancelButton); // 패널에 취소 버튼 추가

			// 버튼 패널을 프레임 상단에 추가
			frame.add(buttonPanel, BorderLayout.NORTH);

			// 테이블 스크롤 패인을 가운데에 추가
			frame.add(scrollPane, BorderLayout.CENTER);

			frame.setSize(600, 400);
			frame.setLocationRelativeTo(null); // 화면 중앙에 표시
			frame.setVisible(true);

			// 예매 버튼 이벤트 처리
			bookButton.addActionListener(e -> {
				int selectedRow = table.getSelectedRow();

				if (selectedRow >= 0) {
					int screeningID = (int) model.getValueAt(selectedRow, 0);
					int movieID = (int) model.getValueAt(selectedRow, 1);
					int theaterID = (int) model.getValueAt(selectedRow, 2);
					Date screeningStartDate = (Date) model.getValueAt(selectedRow, 3);
					Date screeningDate = (Date) model.getValueAt(selectedRow, 4);
					int sessionNumber = (int) model.getValueAt(selectedRow, 5);
					Time startTime = (Time) model.getValueAt(selectedRow, 6);

					new TheaterReservationComponent(dbConnection, screeningID, theaterID, screeningDate, bookingId);

				} else {
					JOptionPane.showMessageDialog(frame, "상영일정을 선택하세요.", "예매 오류", JOptionPane.ERROR_MESSAGE);
				}
			});
			// 취소 버튼 이벤트 처리
			cancelButton.addActionListener(e -> frame.dispose());

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "상영 정보 조회 중 오류가 발생했습니다: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void showAllMovies(int bookingId) {
		// 모든 영화 목록 조회 로직 구현
		Statement stmt = null;
		ResultSet resultSet = null;

		try {
			stmt = dbConnection.createStatement();

			String query = "SELECT MovieID, Title FROM Movies";
			resultSet = stmt.executeQuery(query);

			// 결과 데이터를 JTable에 추가
			String[] columnNames = { "MovieID", "MovieTitle" };
			DefaultTableModel model = new DefaultTableModel(columnNames, 0);

			boolean hasResults = false;

			while (resultSet.next()) {
				hasResults = true;
				int movieID = resultSet.getInt("MovieID");
				String movieTitle = resultSet.getString("Title");
				model.addRow(new Object[] { movieID, movieTitle });
			}

			JTable table = new JTable(model);
			JScrollPane scrollPane = new JScrollPane(table);

			table.setRowHeight(30); // 원하는 높이로 설정

			if (!hasResults) {
				JOptionPane.showMessageDialog(null, "현재 상영 중인 영화가 없습니다", "정보", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			// 새로운 JFrame 생성
			JFrame frame = new JFrame("전체 영화 목록");
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setLayout(new BorderLayout());

			// 버튼 패널 생성
			JPanel buttonPanel = new JPanel();

			// 예매 버튼 추가 및 위치와 크기 설정
			JButton bookButton = new JButton("예매");
			bookButton.setPreferredSize(new Dimension(100, 25)); // 버튼 크기 설정
			buttonPanel.add(bookButton); // 패널에 예매 버튼 추가

			// 취소 버튼 추가 및 위치와 크기 설정
			JButton cancelButton = new JButton("취소");
			cancelButton.setPreferredSize(new Dimension(100, 25)); // 버튼 크기 설정
			buttonPanel.add(cancelButton); // 패널에 취소 버튼 추가

			// 버튼 패널을 프레임 상단에 추가
			frame.add(buttonPanel, BorderLayout.NORTH);

			// 테이블 스크롤 패인을 가운데에 추가
			frame.add(scrollPane, BorderLayout.CENTER);

			frame.setSize(800, 600);

			frame.setVisible(true);

			// 예매 버튼 이벤트 처리
			bookButton.addActionListener(e -> {
				int selectedRow = table.getSelectedRow();
				if (selectedRow >= 0) {
					int movieID = (int) model.getValueAt(selectedRow, 0);
					String movieTitle = (String) model.getValueAt(selectedRow, 1);
					// 여기서 예매 처리 로직을 구현하세요.
					showScreeningList(movieID, bookingId);

				} else {
					JOptionPane.showMessageDialog(frame, "영화를 선택하세요.", "예매 오류", JOptionPane.ERROR_MESSAGE);
				}
			});

			// 취소 버튼 이벤트 처리
			cancelButton.addActionListener(e -> frame.dispose());

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "영화 목록 조회 중 오류가 발생했습니다: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 영화 이름을 이용하여 영화 ID를 가져오는 메서드
	private int getMovieIDFromName(String movieName) throws SQLException {
		int movieID = -1; // 초기화

		// SQL 쿼리 준비
		String query = "SELECT MovieID FROM Movies WHERE Title = ?";
		PreparedStatement stmt = dbConnection.prepareStatement(query);
		stmt.setString(1, movieName);

		// 쿼리 실행 및 결과 처리
		ResultSet resultSet = stmt.executeQuery();
		if (resultSet.next()) {
			movieID = resultSet.getInt("MovieID");
		}

		// 리소스 해제
		resultSet.close();
		stmt.close();

		return movieID;
	}

	private class TheaterReservationComponent extends JFrame {
		private Connection dbConnection;
		private JFrame currentFrame;
		private Date today = Date.valueOf("2024-01-01");

		public TheaterReservationComponent(Connection connection, int screeningID, int theaterID, Date screeningDate,
				int bookingId) {
			dbConnection = connection;
			this.currentFrame = this; // 현재 프레임 참조 설정

			openTheaterReservationForm(screeningID, theaterID, screeningDate, bookingId);

			this.setLocationRelativeTo(null); // 프레임을 화면 중앙에 배치
		}

		private void openTheaterReservationForm(int screeningID, int theaterID, Date screeningDate, int bookingId) {
			Statement stmt = null;
			ResultSet resultSet = null;

			try {
				stmt = dbConnection.createStatement();
				String seatNumberQuery = "SELECT NumberOfSeats, HorizontalSeats, VerticalSeats " + "FROM Theaters "
						+ "WHERE TheaterID = " + theaterID;

				resultSet = stmt.executeQuery(seatNumberQuery);

				if (resultSet.next()) {
					int numberOfSeats = resultSet.getInt("NumberOfSeats");
					int horizontalSeats = resultSet.getInt("HorizontalSeats");
					int verticalSeats = resultSet.getInt("VerticalSeats");

					createSeatLayout(numberOfSeats, horizontalSeats, verticalSeats, screeningID, theaterID, bookingId);
				}

			} catch (SQLException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "예매 중 오류가 발생했습니다: " + e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);

			} finally {
				try {
					if (resultSet != null)
						resultSet.close();
					if (stmt != null)
						stmt.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
		}

		private void createSeatLayout(int numberOfSeats, int horizontalSeats, int verticalSeats, int screeningID,
				int theaterID, int bookingId) {
			// 패널 생성
			JPanel mainPanel = new JPanel(new BorderLayout());
			mainPanel.setPreferredSize(new Dimension(800, 600));

			JLabel screenLabel = new JLabel("SCREEN");
			screenLabel.setHorizontalAlignment(SwingConstants.CENTER);
			screenLabel.setFont(new Font("Arial", Font.BOLD, 20));

			// SCREEN에 테두리를 추가
			screenLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			screenLabel.setBorder(BorderFactory.createTitledBorder(screenLabel.getBorder()));
			screenLabel.setPreferredSize(new Dimension(screenLabel.getPreferredSize().width, 50));

			// 좌석 배치를 나타내는 패널 -> 가로좌석수, 세로좌석수 여기서 미리 지정
			JPanel seatsPanel = new JPanel(new GridLayout(verticalSeats, horizontalSeats, 10, 10));

			// 좌석 배치 패널을 BorderLayout의 중앙에 배치
			mainPanel.add(seatsPanel, BorderLayout.CENTER);

			// 영화관 화면을 BorderLayout의 상단에 배치
			mainPanel.add(screenLabel, BorderLayout.NORTH);

			// 패널에 여백 추가
			mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 2, 10, 2));

			// 프레임에 패널 추가
			this.add(mainPanel);

			// 프레임 크기를 내용물에 맞게 조절
			this.pack();

			// 화면에 표시
			this.setVisible(true);

			Statement stmt = null;
			ResultSet resultSet = null;

			try {
				stmt = dbConnection.createStatement();
				String seatIdQuery = "SELECT SeatID, isActive " + "FROM Seats " + "WHERE ScreeningID = " + screeningID
						+ " AND TheaterID = " + theaterID;

				resultSet = stmt.executeQuery(seatIdQuery);

				while (resultSet.next()) {
					int seatID = resultSet.getInt("SeatID");
					boolean isActive = resultSet.getBoolean("isActive");

					// 좌석 버튼을 생성하고 SeatID를 설정
					JButton seatButton = new JButton();
					seatButton.setPreferredSize(new Dimension(40, 30));

					// isActive가 true인 경우 버튼을 비활성화하고 배경색을 빨간색으로 변경
					if (isActive) {
						seatButton.setEnabled(false);
						seatButton.setBackground(Color.RED);
						seatButton.setText("예약됨");
						Font boldFont = new Font(seatButton.getFont().getName(), Font.BOLD, 30);
						seatButton.setFont(boldFont);
					} else {
						seatButton.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								int option = JOptionPane.showConfirmDialog(currentFrame, "예매하시겠습니까?", "예매 확인",
										JOptionPane.YES_NO_OPTION);
								if (option == JOptionPane.YES_OPTION) {
									// 사용자가 예를 선택한 경우
									seatButton.setBackground(Color.RED);
									seatButton.setText("예약됨");
									Font boldFont = new Font(seatButton.getFont().getName(), Font.BOLD, 30);
									seatButton.setFont(boldFont);
									seatButton.setEnabled(false);
									System.out.println("좌석 선택: " + seatID);

									// 결제 정보 입력 창 띄우기
									showPaymentDialog(screeningID, seatID, bookingId);

								} else {
									// 사용자가 아니오를 선택한 경우
									// 아무 작업도 하지 않음
								}
							}
						});
					}

					// 좌석 버튼을 패널에 추가
					seatsPanel.add(seatButton);
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, "좌석 정보를 가져오는 중 오류가 발생했습니다: " + ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			} finally {
				// ResultSet과 Statement를 닫음
				if (resultSet != null) {
					try {
						resultSet.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}

		// 사용자로부터 결제 정보를 입력받고 변수에 저장하는 메서드
		private void showPaymentDialog(int screeningID, int seatID, int bookingId) {
			JTextField paymentMethodField = new JTextField();
			JTextField paymentStatusField = new JTextField();
			JCheckBox ticketIssuedCheckBox = new JCheckBox();

			Object[] message = { "지불 방법:", paymentMethodField, "지불 상태:", paymentStatusField, "발권 여부:",
					ticketIssuedCheckBox };

			int option = JOptionPane.showConfirmDialog(currentFrame, message, "결제 정보 입력", JOptionPane.OK_CANCEL_OPTION);
			if (option == JOptionPane.OK_OPTION) {
				// 사용자가 확인을 누른 경우
				String paymentMethod = paymentMethodField.getText();
				String paymentStatus = paymentStatusField.getText();
				boolean isTicketing = ticketIssuedCheckBox.isSelected();

				// 데이터베이스에 추가하는 메서드 호출
				addBookingAndTicket(screeningID, seatID, paymentMethod, paymentStatus, isTicketing, bookingId);
			} else {
				// 사용자가 취소를 누른 경우
				// 아무 작업도 하지 않음
			}
		}

		// 결제 정보를 데이터베이스에 추가하는 메서드
		private void addBookingAndTicket(int screeningID, int seatID, String paymentMethod, String paymentStatus,
				boolean isTicketing, int bookingId) {
			String insertBookingQuery = "INSERT INTO Bookings (Payment, PaymentStatus, Amount, CustomerID, PaymentDate) VALUES (?, ?, ?, ?, ?)";
			String insertTicketQuery = "INSERT INTO Tickets (ScreeningID, SeatID, BookingID, IsTicketing, StandardPrice, SalePrice) VALUES (?, ?, ?, ?, ?, ?)";
			String updateSeatQuery = "UPDATE Seats SET isActive = TRUE WHERE SeatID = ?";

			int amount = 15000;
			int standardPrice = 15000;
			int salePrice = (paymentMethod.equals("신한카드")) ? (int) (standardPrice * 0.9) : standardPrice;
			String customerID = "user1";

			try {
				dbConnection.setAutoCommit(false); // 트랜잭션 시작

				// Bookings 테이블에 데이터 삽입
				try (PreparedStatement bookingStmt = dbConnection.prepareStatement(insertBookingQuery,
						Statement.RETURN_GENERATED_KEYS)) {
					bookingStmt.setString(1, paymentMethod);
					bookingStmt.setString(2, paymentStatus);
					bookingStmt.setInt(3, amount);
					bookingStmt.setString(4, customerID);
					bookingStmt.setDate(5, today);

					int affectedRows = bookingStmt.executeUpdate();
					if (affectedRows == 0) {
						throw new SQLException("Creating booking failed, no rows affected.");
					}

					// 방금 삽입한 BookingID 가져오기
					try (ResultSet generatedKeys = bookingStmt.getGeneratedKeys()) {
						if (generatedKeys.next()) {
							int bookingID = generatedKeys.getInt(1);

							// Tickets 테이블에 데이터 삽입
							try (PreparedStatement ticketStmt = dbConnection.prepareStatement(insertTicketQuery)) {
								ticketStmt.setInt(1, screeningID);
								ticketStmt.setInt(2, seatID);
								ticketStmt.setInt(3, bookingID);
								ticketStmt.setInt(4, (isTicketing) ? 1 : 0);
								ticketStmt.setInt(5, standardPrice);
								ticketStmt.setInt(6, salePrice);
								int ticketAffectedRows = ticketStmt.executeUpdate();
								if (ticketAffectedRows == 0) {
									throw new SQLException("Creating ticket failed, no rows affected.");
								}

								// Seats 테이블의 isActive 속성을 업데이트
								try (PreparedStatement updateSeatStmt = dbConnection
										.prepareStatement(updateSeatQuery)) {
									updateSeatStmt.setInt(1, seatID);

									int seatAffectedRows = updateSeatStmt.executeUpdate();
									if (seatAffectedRows == 0) {
										throw new SQLException("Updating seat failed, no rows affected.");
									}
								}
							}
						} else {
							throw new SQLException("Creating booking failed, no ID obtained.");
						}
					}
				}

				dbConnection.commit(); // 트랜잭션 커밋

				JOptionPane.showMessageDialog(currentFrame, "예매가 성공적으로 완료되었습니다.", "Success",
						JOptionPane.INFORMATION_MESSAGE);
				try {
					String updateSeatsQuery = "UPDATE Seats SET IsActive = FALSE WHERE SeatID IN "
							+ "(SELECT SeatID FROM Tickets WHERE BookingID = ?)";
					PreparedStatement updateSeatsStmt = dbConnection.prepareStatement(updateSeatsQuery);
					updateSeatsStmt.setInt(1, bookingId);
					updateSeatsStmt.executeUpdate();
					String deleteTicketQuery = "DELETE FROM Tickets WHERE BookingID = ?";
					PreparedStatement deleteTicketStmt = dbConnection.prepareStatement(deleteTicketQuery);
					deleteTicketStmt.setInt(1, bookingId);
					deleteTicketStmt.executeUpdate();

					String deleteBookingQuery = "DELETE FROM Bookings WHERE BookingID = ?";
					PreparedStatement deleteBookingStmt = dbConnection.prepareStatement(deleteBookingQuery);
					deleteBookingStmt.setInt(1, bookingId);
					deleteBookingStmt.executeUpdate();

					JOptionPane.showMessageDialog(ViewUserBookings.this, "예매가 변경되었습니다.");

				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			} catch (SQLException ex) {
				try {
					dbConnection.rollback(); // 예외 발생 시 롤백
				} catch (SQLException rollbackEx) {
					rollbackEx.printStackTrace();
				}
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, "예매 중 오류가 발생했습니다: " + ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			} finally {
				try {
					dbConnection.setAutoCommit(true); // 트랜잭션 종료 후 자동 커밋 설정
				} catch (SQLException autoCommitEx) {
					autoCommitEx.printStackTrace();
				}
			}
		}
	}

}
