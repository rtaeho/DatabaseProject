import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

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

		buttonPanel.add(deleteButton);
		buttonPanel.add(changeMovieButton);
		buttonPanel.add(changeScheduleButton);

		add(buttonPanel, BorderLayout.SOUTH);

		deleteButton.addActionListener(new DeleteBookingAction());
		changeMovieButton.addActionListener(new ChangeMovieAction());
		changeScheduleButton.addActionListener(new ChangeScheduleAction());

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
				model.addRow(new Object[] { rs.getInt("BookingID"), rs.getString("Title"), rs.getDate("ScreeningDate"),
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
				String deleteQuery = "DELETE FROM Bookings WHERE BookingID = ?";
				PreparedStatement stmt = dbConnection.prepareStatement(deleteQuery);
				stmt.setInt(1, bookingId);
				stmt.executeUpdate();
				stmt.close();

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
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(ViewUserBookings.this, "변경할 예매를 선택하세요.");
				return;
			}

			int bookingId = (int) bookingsTable.getValueAt(selectedRow, 0);
			String newMovieId = JOptionPane.showInputDialog("새로운 영화 ID를 입력하세요:");
			if (newMovieId != null) {
				try {
					String updateQuery = "UPDATE Screenings s " + "JOIN Tickets t ON s.ScreeningID = t.ScreeningID "
							+ "SET s.MovieID = ? WHERE t.BookingID = ?";
					PreparedStatement stmt = dbConnection.prepareStatement(updateQuery);
					stmt.setString(1, newMovieId);
					stmt.setInt(2, bookingId);
					stmt.executeUpdate();
					stmt.close();

					JOptionPane.showMessageDialog(ViewUserBookings.this, "영화가 변경되었습니다.");
					loadBookings(); // 테이블 갱신
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
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

			int bookingId = (int) bookingsTable.getValueAt(selectedRow, 0);
			String newScreeningId = JOptionPane.showInputDialog("새로운 상영 일정 ID를 입력하세요:");
			if (newScreeningId != null) {
				try {
					String updateQuery = "UPDATE Tickets SET ScreeningID = ? WHERE BookingID = ?";
					PreparedStatement stmt = dbConnection.prepareStatement(updateQuery);
					stmt.setString(1, newScreeningId);
					stmt.setInt(2, bookingId);
					stmt.executeUpdate();
					stmt.close();

					JOptionPane.showMessageDialog(ViewUserBookings.this, "상영 일정이 변경되었습니다.");
					loadBookings(); // 테이블 갱신
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
