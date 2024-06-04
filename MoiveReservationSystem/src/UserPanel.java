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

        viewMoviesButton.addActionListener(e -> showMovieList());
        viewBookingsButton.addActionListener(e -> viewUserBookings());
        backButton.addActionListener(e -> Utils.switchToPanel(this, new MainPanel()));
    }
    
    private void showMovieList() {
        // 영화 목록 조회 로직 구현
        // 예를 들어, MovieListComponent 클래스의 인스턴스를 생성하고 화면에 표시
    	
    }

    private void viewUserBookings() {
        // 예매한 영화 조회 로직 구현
        // 사용자가 예매한 영화 목록을 보여주는 컴포넌트를 활성화
    }
    
    // MovieListComponent 클래스는 실제로 영화 목록을 보여주는 UI 컴포넌트입니다.
    // 이 예제에서는 해당 클래스의 구현이 제공되지 않았습니다.
}
