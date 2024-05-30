import javax.swing.*;
import java.awt.*;
//import java.sql.Connection;
//import java.sql.DatabaseMetaData;
//import java.sql.Statement;
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
        insertButton.addActionListener(e -> new MovieInputComponent());
        deleteButton.addActionListener(e -> new MovieDeleteComponent());
        updateButton.addActionListener(e -> new MovieUpdateComponent());
        backButton.addActionListener(e -> Utils.switchToPanel(this, new MainPanel()));
    }

    private void initializeDatabase() {
        // 초기화 로직 구현
    	
    	// CREATE TABLE 쿼리문들 미리 만들어놓기.
    	String[] createTableQueries = {
    	        "CREATE TABLE table1 (id INT PRIMARY KEY, name VARCHAR(50))",
    	        "CREATE TABLE table2 (id INT PRIMARY KEY, address VARCHAR(100))",
    	        "CREATE TABLE table3 (id INT PRIMARY KEY, email VARCHAR(100))"
    	        // 필요한 테이블 생성 쿼리 추가
    	};
    	
    	try (Connection conn = dbConnection; // dbConnection은 유효한 Connection 객체여야 함
    	         Statement stmt = conn.createStatement()) { // Statement 객체 생성

    	        // 데이터베이스 메타데이터를 통해 테이블 목록 가져오기
    	        DatabaseMetaData metaData = conn.getMetaData();
    	        String catalog = dbConnection.getCatalog(); // 현재 연결된 데이터베이스의 카탈로그 이름을 가져옴
    	        Utils.showMessage(catalog);
    	        ResultSet tables = metaData.getTables(catalog, null, "%", new String[] {"TABLE"});

    	        // 모든 테이블 드롭하기
    	        while (tables.next()) {
    	            String tableName = tables.getString("TABLE_NAME");
    	           
    	            Utils.showMessage(tableName);    // 테스트용 : 찾아낸 테이블 이름 출력
    	            
    	            String dropQuery = "DROP TABLE IF EXISTS " + tableName;
    	            stmt.executeUpdate(dropQuery); // DROP TABLE 쿼리 실행
    	        }

    	        // 새 테이블 생성하기
    	        /*
    	        for (String query : createTableQueries) {
    	            stmt.executeUpdate(query); // CREATE TABLE 쿼리 실행
    	        }
    	        */

    	        JOptionPane.showMessageDialog(this, "데이터베이스 초기화가 완료되었습니다.");
    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	        JOptionPane.showMessageDialog(this, "데이터베이스 초기화 중 오류가 발생했습니다: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    	    }
    	
    	

    }

    private void viewAllTables() {
        // 전체 테이블 조회 로직 구현
    }

    // MovieInputComponent, MovieDeleteComponent, MovieUpdateComponent 클래스의
    // 생성자 및 메서드는 실제 구현에 따라 내용을 추가해야 함.
}

