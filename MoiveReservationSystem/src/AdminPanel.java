import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

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
        insertButton.addActionListener(e -> new MovieInputComponent());
        deleteButton.addActionListener(e -> new MovieDeleteComponent());
        updateButton.addActionListener(e -> new MovieUpdateComponent());
        backButton.addActionListener(e -> Utils.switchToPanel(this, new MainPanel()));
    }

    private void initializeDatabase() {
        // 초기화 로직 구현
    }

    private void viewAllTables() {
        // 전체 테이블 조회 로직 구현
    }

    // MovieInputComponent, MovieDeleteComponent, MovieUpdateComponent 클래스의
    // 생성자 및 메서드는 실제 구현에 따라 내용을 추가해야 함.
}
