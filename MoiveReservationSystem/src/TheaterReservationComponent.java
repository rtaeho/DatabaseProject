import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class TheaterReservationComponent extends JFrame {
	private Connection dbConnection;
	private JFrame currentFrame;
	private Date today = Date.valueOf("2024-01-01");

	public TheaterReservationComponent(Connection connection, int screeningID, int theaterID, Date screeningDate) {
		dbConnection = connection;
		this.currentFrame = this; // 현재 프레임 참조 설정

		openTheaterReservationForm(screeningID, theaterID, screeningDate);

		this.setLocationRelativeTo(null); // 프레임을 화면 중앙에 배치
	}

	private void openTheaterReservationForm(int screeningID, int theaterID, Date screeningDate) {
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

				createSeatLayout(numberOfSeats, horizontalSeats, verticalSeats, screeningID, theaterID);
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
			int theaterID) {
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
								showPaymentDialog(screeningID, seatID);

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
	private void showPaymentDialog(int screeningID, int seatID) {
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
			addBookingAndTicket(screeningID, seatID, paymentMethod, paymentStatus, isTicketing);
		} else {
			// 사용자가 취소를 누른 경우
			// 아무 작업도 하지 않음
		}
	}

	// 결제 정보를 데이터베이스에 추가하는 메서드
	private void addBookingAndTicket(int screeningID, int seatID, String paymentMethod, String paymentStatus,
			boolean isTicketing) {
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
							try (PreparedStatement updateSeatStmt = dbConnection.prepareStatement(updateSeatQuery)) {
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
