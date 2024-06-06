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
        /* 예매한 영화 조회 -> 현재 user1 이므로 customerID가 user1인 예매정보만 가져오기 
        *  검색 시 Bookings 테이블에서 customerID가 user1인 column들만 모아보기
        *  customerID가 user1인 BookingID 값만 모으기
        *  Tickets 테이블에서 BookingID 값으로 상영일정번호 찾기
        *  상영일정번호로 영화번호 찾기
        *  영화제목, 상영일정번호, 상영관번호 조합해서 띄우기?
        */
        viewBookingsButton.addActionListener(e -> viewUserBookings());
        backButton.addActionListener(e -> Utils.switchToPanel(this, new MainPanel()));
    }
   

    private void viewUserBookings() {
        // 예매한 영화 조회 로직 구현
        // 사용자가 예매한 영화 목록을 보여주는 컴포넌트를 활성화
    }
    
    // MovieListComponent 클래스는 실제로 영화 목록을 보여주는 UI 컴포넌트입니다.
    // 이 예제에서는 해당 클래스의 구현이 제공되지 않았습니다.
}
