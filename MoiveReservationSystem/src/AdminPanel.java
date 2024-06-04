import javax.swing.*;
import javax.swing.table.*;

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
        insertButton.addActionListener(e -> new MovieInputComponent(dbConnection));
        deleteButton.addActionListener(e -> new MovieDeleteComponent());
        updateButton.addActionListener(e -> new MovieUpdateComponent());
        backButton.addActionListener(e -> Utils.switchToPanel(this, new MainPanel()));
    }
    
    private void initializeDatabase() {
        Statement stmt = null;
        ResultSet tables = null;
        
        String[] createTableQueries = {
                "CREATE TABLE Actors (" +
                "    ActorID INT PRIMARY KEY," +
                "    ActorName VARCHAR(255)" +
                ")",

                "CREATE TABLE Customers (" +
                "    CustomerID VARCHAR(255) PRIMARY KEY," +
                "    CustomerName VARCHAR(255)," +
                "    PhoneNumber VARCHAR(15)," +
                "    Email VARCHAR(255)" +
                ")",

                "CREATE TABLE Movies (" +
                "    MovieID INT PRIMARY KEY AUTO_INCREMENT," +  // auto_increment 추가
                "    Title VARCHAR(255) NOT NULL," +
                "    MovieTime INT NOT NULL," +
                "    Rating VARCHAR(255)," +
                "    Director VARCHAR(255)," +
                "    Genre VARCHAR(255)," +
                "    Introduction VARCHAR(255)," +
                "    ReleaseDate DATE," +
                "    Score DECIMAL(2,1)" +
                ")",

                "CREATE TABLE Theaters (" +
                "    TheaterID INT PRIMARY KEY," +
                "    NumberOfSeats INT," +
                "    HorizontalSeats INT," +
                "    VerticalSeats INT" +
                ")",

                "CREATE TABLE Screenings (" +
                "    ScreeningID INT PRIMARY KEY AUTO_INCREMENT," +  // auto_increment 추가
                "    MovieID INT," +
                "    TheaterID INT," +
                "    ScreeningStartDate DATE," +
                "    ScreeningDate DATE UNIQUE," +
                "    SessionNumber INT," +
                "    StartTime TIME," +
                "    EndTime TIME," +
                "    FOREIGN KEY (MovieID) REFERENCES Movies(MovieID)," +
                "    FOREIGN KEY (TheaterID) REFERENCES Theaters(TheaterID)" +
                ")",

                "CREATE TABLE Seats (" +
                "    SeatID INT PRIMARY KEY," +
                "    TheaterID INT," +
                "    ScreeningID INT," +
                "    IsActive BOOLEAN," +
                "    FOREIGN KEY (TheaterID) REFERENCES Theaters(TheaterID)," +
                "    FOREIGN KEY (ScreeningID) REFERENCES Screenings(ScreeningID)" +
                ")",

                "CREATE TABLE TheaterUse (" +
                "    TheaterID INT," +
                "    ScreeningDate DATE," +
                "    TheaterUse BOOLEAN," +
                "    PRIMARY KEY (TheaterID, ScreeningDate)," +
                "    FOREIGN KEY (TheaterID) REFERENCES Theaters(TheaterID)," +
                "    FOREIGN KEY (ScreeningDate) REFERENCES Screenings(ScreeningDate)" +
                ")",

                "CREATE TABLE Bookings (" +
                "    BookingID INT PRIMARY KEY AUTO_INCREMENT," +  // auto_increment 추가
                "    Payment VARCHAR(50)," +
                "    PaymentStatus VARCHAR(50)," +
                "    Amount INT," +
                "    CustomerID VARCHAR(255)," +
                "    PaymentDate DATE," +
                "    FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID)" +
                ")",

                "CREATE TABLE Tickets (" +
                "    TicketID INT PRIMARY KEY AUTO_INCREMENT," +  // auto_increment 추가
                "    ScreeningID INT," +
                "    ScreeningDate DATE," +
                "    TheaterID INT," +
                "    SeatID INT," +
                "    BookingID INT," +
                "    IsTicketing BOOLEAN," +
                "    StandardPrice INT," +
                "    SalePrice INT," +
                "    FOREIGN KEY (ScreeningID) REFERENCES Screenings(ScreeningID)," +
                "    FOREIGN KEY (SeatID) REFERENCES Seats(SeatID)," +
                "    FOREIGN KEY (BookingID) REFERENCES Bookings(BookingID)" +
                ")",

                "CREATE TABLE MovieActors (" +
                "    MovieID INT," +
                "    ActorID INT," +
                "    FOREIGN KEY (MovieID) REFERENCES Movies(MovieID)," +
                "    FOREIGN KEY (ActorID) REFERENCES Actors(ActorID)" +
                ")"
            };
        
        try {
            stmt = dbConnection.createStatement(); // Statement 객체 생성
            
            stmt.executeUpdate("SET foreign_key_checks = 0"); //외래키 제약조건 해제
            
            // 데이터베이스 메타데이터를 통해 테이블 목록 가져오기
            DatabaseMetaData metaData = dbConnection.getMetaData();
            String catalog = dbConnection.getCatalog(); // 현재 연결된 데이터베이스의 카탈로그 이름을 가져옴
            Utils.showMessage(catalog);
            tables = metaData.getTables(catalog, null, "%", new String[]{"TABLE"});

            // 모든 테이블 드롭하기
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                
                Utils.showMessage(tableName);    // 테스트용: 찾아낸 테이블 이름 출력
                
                String dropQuery = "DROP TABLE IF EXISTS " + tableName;
                stmt.executeUpdate(dropQuery); // DROP TABLE 쿼리 실행
            }
            
            stmt.executeUpdate("SET foreign_key_checks = 1");  //외래키 제약조건 재설정

            // 새 테이블 생성  
            for (String query : createTableQueries) {
                stmt.executeUpdate(query); // CREATE TABLE 쿼리 실행
            }
            
            //테이블 샘플 데이터 삽입
            

            JOptionPane.showMessageDialog(this, "데이터베이스 초기화가 완료되었습니다.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "데이터베이스 초기화 중 오류가 발생했습니다: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Statement와 ResultSet 객체를 명시적으로 닫기
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
    
    private void viewAllTables() {
        Statement stmt = null;
        ResultSet tables = null;

        try {
            stmt = dbConnection.createStatement();

            // 데이터베이스 메타데이터를 통해 테이블 목록 가져오기
            DatabaseMetaData metaData = dbConnection.getMetaData();
            String catalog = dbConnection.getCatalog();
            tables = metaData.getTables(catalog, null, "%", new String[]{"TABLE"});

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
            JOptionPane.showMessageDialog(this, "테이블 조회 중 오류가 발생했습니다: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

/*
private void viewAllTables() {
    Statement stmt = null;
    ResultSet tables = null;

    try {
        stmt = dbConnection.createStatement();
        
        // 데이터베이스 메타데이터를 통해 테이블 목록 가져오기
        DatabaseMetaData metaData = dbConnection.getMetaData();
        String catalog = dbConnection.getCatalog();
        tables = metaData.getTables(catalog, null, "%", new String[]{"TABLE"});

        // 새로운 JFrame 생성
        JFrame frame = new JFrame("전체 테이블 조회");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        // 모든 테이블 데이터를 담을 JPanel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
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
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createTitledBorder(tableName));
            
            // JPanel에 JScrollPane 추가
            panel.add(scrollPane);
        }

        // JFrame에 JPanel 추가
        frame.add(new JScrollPane(panel), BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setVisible(true);

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "테이블 조회 중 오류가 발생했습니다: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
*/


/*
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
	        
	        for (String query : createTableQueries) {
	            stmt.executeUpdate(query); // CREATE TABLE 쿼리 실행
	        }
	        

	        JOptionPane.showMessageDialog(this, "데이터베이스 초기화가 완료되었습니다.");
	    } catch (SQLException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(this, "데이터베이스 초기화 중 오류가 발생했습니다: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    }

}
*/


























