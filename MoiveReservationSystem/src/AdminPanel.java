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
        deleteButton.addActionListener(e -> new MovieDeleteComponent());
        updateButton.addActionListener(e -> new MovieUpdateComponent());
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
            JOptionPane.showMessageDialog(this, "데이터베이스 초기화 중 오류가 발생했습니다: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
            tables = metaData.getTables(catalog, null, "%", new String[]{"TABLE"});
            

            // 모든 테이블 드롭하기
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                String dropQuery = "DROP TABLE IF EXISTS " + tableName;
                Utils.showMessage(tableName);    // 테스트용: 찾아낸 테이블 이름 출력
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
        	    "CREATE TABLE Actors (" +
        	    "    ActorID INT PRIMARY KEY AUTO_INCREMENT," +
        	    "    ActorName VARCHAR(255)" +
        	    ");",

        	    "CREATE TABLE Customers (" +
        	    "    CustomerID VARCHAR(255) PRIMARY KEY," +
        	    "    CustomerName VARCHAR(255)," +
        	    "    PhoneNumber VARCHAR(15)," +
        	    "    Email VARCHAR(255)" +
        	    ");",

        	    "CREATE TABLE Movies (" +
        	    "    MovieID INT PRIMARY KEY AUTO_INCREMENT," +
        	    "    Title VARCHAR(255) NOT NULL," +
        	    "    MovieTime INT NOT NULL," +
        	    "    Rating VARCHAR(255)," +
        	    "    Director VARCHAR(255)," +
        	    "    Genre VARCHAR(255)," +
        	    "    Introduction VARCHAR(255)," +
        	    "    ReleaseDate DATE," +
        	    "    Score DECIMAL(2,1)" +
        	    ");",

        	    "CREATE TABLE Theaters (" +
        	    "    TheaterID INT PRIMARY KEY AUTO_INCREMENT," +
        	    "    NumberOfSeats INT," +
        	    "    HorizontalSeats INT," +
        	    "    VerticalSeats INT" +
        	    ");",

        	    "CREATE TABLE Screenings (" +
        	    "    ScreeningID INT PRIMARY KEY AUTO_INCREMENT," +
        	    "    MovieID INT," +
        	    "    TheaterID INT," +
        	    "    ScreeningStartDate DATE," +
        	    "    ScreeningDate DATE," +
        	    "    SessionNumber INT," +
        	    "    StartTime TIME," +
        	    "    FOREIGN KEY (MovieID) REFERENCES Movies(MovieID)," +
        	    "    FOREIGN KEY (TheaterID) REFERENCES Theaters(TheaterID)" +
        	    ");",

        	    "CREATE TABLE Seats (" +
        	    "    SeatID INT PRIMARY KEY AUTO_INCREMENT," +
        	    "    TheaterID INT," +
        	    "    ScreeningID INT," +
        	    "    IsActive BOOLEAN," +
        	    "    FOREIGN KEY (TheaterID) REFERENCES Theaters(TheaterID)," +
        	    "    FOREIGN KEY (ScreeningID) REFERENCES Screenings(ScreeningID)" +
        	    ");",

        	    "CREATE TABLE TheaterUse (" +
        	    "    TheaterID INT," +
        	    "    ScreeningID INT," +
        	    "    TheaterUse BOOLEAN," +
        	    "    PRIMARY KEY (TheaterID, ScreeningID)," +
        	    "    FOREIGN KEY (TheaterID) REFERENCES Theaters(TheaterID)," +
        	    "    FOREIGN KEY (ScreeningID) REFERENCES Screenings(ScreeningID)" +
        	    ");",

        	    "CREATE TABLE Bookings (" +
        	    "    BookingID INT PRIMARY KEY AUTO_INCREMENT," +
        	    "    Payment VARCHAR(50)," +
        	    "    PaymentStatus VARCHAR(50)," +
        	    "    Amount INT," +
        	    "    CustomerID VARCHAR(255)," +
        	    "    PaymentDate DATE," +
        	    "    FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID)" +
        	    ");",

        	    "CREATE TABLE Tickets (" +
        	    "    TicketID INT PRIMARY KEY AUTO_INCREMENT," +
        	    "    ScreeningID INT," +
        	    "    SeatID INT," +
        	    "    BookingID INT," +
        	    "    IsTicketing BOOLEAN," +
        	    "    StandardPrice INT," +
        	    "    SalePrice INT," +
        	    "    FOREIGN KEY (ScreeningID) REFERENCES Screenings(ScreeningID)," +
        	    "    FOREIGN KEY (SeatID) REFERENCES Seats(SeatID)," +
        	    "    FOREIGN KEY (BookingID) REFERENCES Bookings(BookingID)" +
        	    ");",

        	    "CREATE TABLE MovieActors (" +
        	    "    MovieID INT," +
        	    "    ActorID INT," +
        	    "    PRIMARY KEY (MovieID, ActorID)," +
        	    "    FOREIGN KEY (MovieID) REFERENCES Movies(MovieID)," +
        	    "    FOREIGN KEY (ActorID) REFERENCES Actors(ActorID)" +
        	    ");"
        	};

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

    private void insertSampleData() throws SQLException {
        String insertActors = 
        	    "INSERT INTO Actors (ActorID, ActorName) VALUES " +
        	    "(1, 'Actor 1'), (2, 'Actor 2'), (3, 'Actor 3'), (4, 'Actor 4'), " +
        	    "(5, 'Actor 5'), (6, 'Actor 6'), (7, 'Actor 7'), (8, 'Actor 8'), " +
        	    "(9, 'Actor 9'), (10, 'Actor 10'), (11, 'Actor 11'), (12, 'Actor 12')";

        String insertCustomers = 
        	    "INSERT INTO Customers (CustomerID, CustomerName, PhoneNumber, Email) VALUES " +
        	    "('C001', 'Customer 1', '123-456-7890', 'customer1@example.com'), " +
        	    "('C002', 'Customer 2', '123-456-7891', 'customer2@example.com'), " +
        	    "('C003', 'Customer 3', '123-456-7892', 'customer3@example.com'), " +
        	    "('C004', 'Customer 4', '123-456-7893', 'customer4@example.com'), " +
        	    "('C005', 'Customer 5', '123-456-7894', 'customer5@example.com'), " +
        	    "('C006', 'Customer 6', '123-456-7895', 'customer6@example.com'), " +
        	    "('C007', 'Customer 7', '123-456-7896', 'customer7@example.com'), " +
        	    "('C008', 'Customer 8', '123-456-7897', 'customer8@example.com'), " +
        	    "('C009', 'Customer 9', '123-456-7898', 'customer9@example.com'), " +
        	    "('C010', 'Customer 10', '123-456-7899', 'customer10@example.com'), " +
        	    "('C011', 'Customer 11', '123-456-7800', 'customer11@example.com'), " +
        	    "('C012', 'Customer 12', '123-456-7801', 'customer12@example.com')";

        String insertMovies = 
        	    "INSERT INTO Movies (Title, MovieTime, Rating, Director, Genre, Introduction, ReleaseDate, Score) VALUES " +
        	    "('Movie 1', 120, 'PG-13', 'Director 1', 'Action', 'Introduction 1', '2023-01-01', 8.5), " +
        	    "('Movie 2', 110, 'R', 'Director 2', 'Comedy', 'Introduction 2', '2023-02-01', 7.5), " +
        	    "('Movie 3', 130, 'PG', 'Director 3', 'Drama', 'Introduction 3', '2023-03-01', 6.5), " +
        	    "('Movie 4', 140, 'PG-13', 'Director 4', 'Horror', 'Introduction 4', '2023-04-01', 8.0), " +
        	    "('Movie 5', 150, 'R', 'Director 5', 'Sci-Fi', 'Introduction 5', '2023-05-01', 9.0), " +
        	    "('Movie 6', 95, 'PG', 'Director 6', 'Animation', 'Introduction 6', '2023-06-01', 7.0), " +
        	    "('Movie 7', 105, 'PG-13', 'Director 7', 'Action', 'Introduction 7', '2023-07-01', 6.0), " +
        	    "('Movie 8', 115, 'R', 'Director 8', 'Comedy', 'Introduction 8', '2023-08-01', 5.5), " +
        	    "('Movie 9', 125, 'PG', 'Director 9', 'Drama', 'Introduction 9', '2023-09-01', 8.0), " +
        	    "('Movie 10', 135, 'PG-13', 'Director 10', 'Horror', 'Introduction 10', '2023-10-01', 7.5), " +
        	    "('Movie 11', 145, 'R', 'Director 11', 'Sci-Fi', 'Introduction 11', '2023-11-01', 9.5), " +
        	    "('Movie 12', 155, 'PG', 'Director 12', 'Animation', 'Introduction 12', '2023-12-01', 6.5)";

        String insertTheaters = 
        	    "INSERT INTO Theaters (TheaterID, NumberOfSeats, HorizontalSeats, VerticalSeats) VALUES " +
        	    "(1, 20, 5, 4), (2, 20, 5, 4), (3, 20, 5, 4), (4, 20, 5, 4), " +
        	    "(5, 20, 5, 4), (6, 20, 5, 4), (7, 20, 5, 4), (8, 20, 5, 4), " +
        	    "(9, 20, 5, 4), (10, 20, 5, 4), (11, 20, 5, 4), (12, 20, 5, 4)";

        String insertScreenings = 
        	    "INSERT INTO Screenings (MovieID, TheaterID, ScreeningStartDate, ScreeningDate, SessionNumber, StartTime) VALUES " +
        	    "(1, 1, '2023-01-01', '2023-01-02', 1, '10:00:00'), " +
        	    "(2, 2, '2023-02-01', '2023-02-02', 1, '11:00:00'), " +
        	    "(3, 3, '2023-03-01', '2023-03-02', 1, '12:00:00'), " +
        	    "(4, 4, '2023-04-01', '2023-04-02', 1, '13:00:00'), " +
        	    "(5, 5, '2023-05-01', '2023-05-02', 1, '14:00:00'), " +
        	    "(6, 6, '2023-06-01', '2023-06-02', 1, '15:00:00'), " +
        	    "(7, 7, '2023-07-01', '2023-07-02', 1, '16:00:00'), " +
        	    "(8, 8, '2023-08-01', '2023-08-02', 1, '17:00:00'), " +
        	    "(9, 9, '2023-09-01', '2023-09-02', 1, '18:00:00'), " +
        	    "(10, 10, '2023-10-01', '2023-10-02', 1, '19:00:00'), " +
        	    "(11, 11, '2023-11-01', '2023-11-02', 1, '20:00:00'), " +
        	    "(12, 12, '2023-12-01', '2023-12-02', 1, '21:00:00')";
        
        String insertTheaterUse = 
        	    "INSERT INTO TheaterUse (TheaterID, ScreeningID, TheaterUse) " +
        	    "SELECT t.TheaterID, s.ScreeningID, FALSE " +
        	    "FROM Theaters t " +
        	    "CROSS JOIN Screenings s";

        String updateTheaterUse = 
        	    "UPDATE TheaterUse tu " +
        	    "JOIN Screenings s ON tu.TheaterID = s.TheaterID AND tu.ScreeningID = s.ScreeningID " +
        	    "SET tu.TheaterUse = TRUE";

        String insertBookings = 
        	    "INSERT INTO Bookings (Payment, PaymentStatus, Amount, CustomerID, PaymentDate) VALUES " +
        	    "('Credit Card', 'Paid', 150, 'C001', '2023-01-15'), " +
        	    "('Credit Card', 'Pending', 200, 'C002', '2023-02-20'), " +
        	    "('Cash', 'Paid', 100, 'C003', '2023-03-10'), " +
        	    "('Credit Card', 'Cancelled', 250, 'C004', '2023-04-05'), " +
        	    "('Cash', 'Paid', 175, 'C005', '2023-05-25'), " +
        	    "('Credit Card', 'Refunded', 300, 'C006', '2023-06-15'), " +
        	    "('Cash', 'Pending', 125, 'C007', '2023-07-30'), " +
        	    "('Credit Card', 'Paid', 275, 'C008', '2023-08-18'), " +
        	    "('Cash', 'Cancelled', 225, 'C009', '2023-09-22'), " +
        	    "('Credit Card', 'Pending', 350, 'C010', '2023-10-05'), " +
        	    "('Cash', 'Paid', 100, 'C011', '2023-11-11'), " +
        	    "('Credit Card', 'Refunded', 150, 'C012', '2023-12-31')";

        String insertTickets = 
        	    "INSERT INTO Tickets (ScreeningID, SeatID, BookingID, IsTicketing, StandardPrice, SalePrice) VALUES " +
        	    "(1, 1, 1, TRUE, 15000, 10000), " +
        	    "(2, 21, 2, TRUE, 15000, 10000), " +
        	    "(3, 41, 3, TRUE, 15000, 10000), " +
        	    "(4, 61, 4, TRUE, 15000, 10000), " +
        	    "(5, 81, 5, TRUE, 15000, 10000), " +
        	    "(6, 101, 6, TRUE, 15000, 10000), " +
        	    "(7, 121, 7, TRUE, 15000, 10000), " +
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
        	    "(1, 1), (1, 2), (1, 3), " +
        	    "(2, 4), (2, 5), " +
        	    "(3, 6), (3, 7), " +
        	    "(4, 8), (4, 9), (4, 10), " +
        	    "(5, 11), (5, 12), " +
        	    "(6, 1), (6, 3), " +
        	    "(7, 2), (7, 4), " +
        	    "(8, 5), (8, 6), " +
        	    "(9, 7), (9, 8), " +
        	    "(10, 9), (10, 10), " +
        	    "(11, 11), (11, 1), " +
        	    "(12, 2), (12, 12)";

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
              Ticket 샘플데이터 입력 시 
              Ticket에 있는 SeatID -> 활성화, Seats테이블에서 해당 하는 SeatID의 IsActive를 true로 업데이트
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
                    System.out.println("ScreeningID: " + screeningID + ", TheaterID: " + theaterID + ", SeatCount: " + seatCount);
                    for (int i = 0; i < seatCount; i++) {
                        String insertQuery = "INSERT INTO Seats (TheaterID, ScreeningID, IsActive) VALUES (" 
                                             + theaterID + ", " + screeningID + ", false)";
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

