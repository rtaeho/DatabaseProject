import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.sql.*;

public class TheaterReservationComponent extends JFrame {
	private Connection dbConnection;
	private JFrame currentFrame;
	private Date today = Date.valueOf("2024-01-01");

	public TheaterReservationComponent(Connection connection, int screeningID, int movieID, int theaterID,
			Date screeningStartDate, Date screeningDate, int sessionNumber, Time startTime) {
		dbConnection = connection;
		this.currentFrame = this; // 현재 프레임 참조 설정
		
		openTheaterReservationForm(screeningID, movieID, theaterID, screeningStartDate, screeningDate, sessionNumber, startTime);
			
	}
	
	
	
	
	private void openTheaterReservationForm(int screeningID, int movieID, int theaterID,
			Date screeningStartDate, Date screeningDate, int sessionNumber, Time startTime) {
		
		Statement stmt = null;
		ResultSet resultSet = null;
	    

		
        try {
            stmt = dbConnection.createStatement();
            String seatNumberQuery = "SELECT NumberOfSeats, HorizontalSeats, VerticalSeats " +
                    "FROM Theaters " +
                    "WHERE TheaterID = " + theaterID;
               
            String seatIdQuery = "SELECT SeatID " +
                    "FROM Seats " +
                    "WHERE ScreeningID = " + screeningID + " AND TheaterID = " + theaterID;
            
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
                if (resultSet != null) resultSet.close();
                if (stmt != null) stmt.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

	
	
	private void createSeatLayout(int numberOfSeats, int horizontalSeats, int verticalSeats, int screeningID, int theaterID) {
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
		    String seatIdQuery = "SELECT SeatID, isActive " +
		            "FROM Seats " +
		            "WHERE ScreeningID = " + screeningID + " AND TheaterID = " + theaterID;
		            
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
		                	 int option = JOptionPane.showConfirmDialog(currentFrame, "예매하시겠습니까?", "예매 확인", JOptionPane.YES_NO_OPTION);
		                     if (option == JOptionPane.YES_OPTION) {
		                         // 사용자가 예를 선택한 경우
		                         // 여기에 예매 처리 코드를 추가하세요
		                         seatButton.setBackground(Color.RED);
		                         seatButton.setText("예약됨");
		                         Font boldFont = new Font(seatButton.getFont().getName(), Font.BOLD, 30);
		                         seatButton.setFont(boldFont);
		                         seatButton.setEnabled(false);
		                         System.out.println("좌석 선택: " + seatID);
		                         
		                         
		                         
		                         
		                     } else {
		                         // 사용자가 아니오를 선택한 경우
		                         // 아무 작업도 하지 않음
		                     }
		                	/*                	
		                    seatButton.setBackground(Color.RED);
		                    seatButton.setText("예약됨");
		                    Font boldFont = new Font(seatButton.getFont().getName(), Font.BOLD, 30);
				            seatButton.setFont(boldFont);
				            seatButton.setEnabled(false);
		                    System.out.println("좌석 선택: " + seatID);	
		                    */	                	
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

}
