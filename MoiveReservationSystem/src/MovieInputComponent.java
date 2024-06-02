import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MovieInputComponent extends JFrame {
    private Connection connection;
    private JFrame currentFrame;

    public MovieInputComponent(Connection connection) {
        this.connection = connection;
        setTitle("테이블 선택");
        setSize(400, 400);
        setLayout(new GridLayout(5, 2));

        addButton("Movies");
        addButton("MovieActors");
        addButton("Actors");
        addButton("Screenings");
        addButton("Theaters");
        addButton("Tickets");
        addButton("Seats");
        addButton("Customers");
        addButton("Bookings");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void addButton(String tableName) {
        JButton button = new JButton(tableName);
        button.addActionListener(e -> openInputForm(e, tableName));
        add(button);
    }

    private void openInputForm(ActionEvent e, String tableName) {
        currentFrame = createFrame("추가 " + tableName, 400, 400);

        switch (tableName) {
            case "Movies":
                addInputFields(currentFrame,
                        new JLabel("영화ID:"), new JTextField(),
                        new JLabel("제목:"), new JTextField(),
                        new JLabel("상영시간:"), new JTextField(),
                        new JLabel("상영등급:"), new JTextField(),
                        new JLabel("감독명:"), new JTextField(),
                        new JLabel("장르:"), new JTextField(),
                        new JLabel("영화소개:"), new JTextArea(),
                        new JLabel("개봉일자:"), new JTextField(),
                        new JLabel("평점:"), new JTextField()
                );
                break;
            case "MovieActors":
                addInputFields(currentFrame,
                        new JLabel("영화ID:"), new JTextField(),
                        new JLabel("배우ID:"), new JTextField()
                );
                break;
            case "Actors":
                addInputFields(currentFrame,
                        new JLabel("배우ID:"), new JTextField(),
                        new JLabel("배우이름:"), new JTextField()
                );
                break;
            case "Screenings":
                addInputFields(currentFrame,
                        new JLabel("상영ID:"), new JTextField(),
                        new JLabel("영화ID:"), new JTextField(),
                        new JLabel("상영관ID:"), new JTextField(),
                        new JLabel("상영일:"), new JTextField(),
                        new JLabel("상영회차:"), new JTextField(),
                        new JLabel("상영시작시간:"), new JTextField(),
                        new JLabel("상영종료시간:"), new JTextField()
                );
                break;
            case "Theaters":
                addInputFields(currentFrame,
                        new JLabel("상영관ID:"), new JTextField(),
                        new JLabel("상영일:"), new JTextField(),
                        new JLabel("좌석수:"), new JTextField(),
                        new JLabel("상영관사용여부:"), new JCheckBox(),
                        new JLabel("가로좌석수:"), new JTextField(),
                        new JLabel("세로좌석수:"), new JTextField()
                );
                break;
            case "Tickets":
                addInputFields(currentFrame,
                        new JLabel("티켓ID:"), new JTextField(),
                        new JLabel("상영ID:"), new JTextField(),
                        new JLabel("상영일:"), new JTextField(),
                        new JLabel("상영관ID:"), new JTextField(),
                        new JLabel("좌석ID:"), new JTextField(),
                        new JLabel("예매ID:"), new JTextField(),
                        new JLabel("발권여부:"), new JCheckBox(),
                        new JLabel("표준가격:"), new JTextField(),
                        new JLabel("판매가격:"), new JTextField()
                );
                break;
            case "Seats":
                addInputFields(currentFrame,
                        new JLabel("좌석ID:"), new JTextField(),
                        new JLabel("상영관ID:"), new JTextField(),
                        new JLabel("상영일:"), new JTextField(),
                        new JLabel("좌석사용여부:"), new JCheckBox()
                );
                break;
            case "Customers":
                addInputFields(currentFrame,
                        new JLabel("회원ID:"), new JTextField(),
                        new JLabel("회원이름:"), new JTextField(),
                        new JLabel("휴대폰번호:"), new JTextField(),
                        new JLabel("전자메일주소:"), new JTextField()
                );
                break;
            case "Bookings":
                addInputFields(currentFrame,
                        new JLabel("예매ID:"), new JTextField(),
                        new JLabel("결제 방법:"), new JTextField(),
                        new JLabel("결제 상태:"), new JTextField(),
                        new JLabel("결제 금액:"), new JTextField(),
                        new JLabel("회원 ID:"), new JTextField(),
                        new JLabel("결제 일자:"), new JTextField()
                );
                break;
            default:
                break;
        }
    }

    private void saveData(ActionEvent e, String tableName) {
        try {
            String query = generateInsertQuery(tableName);
            PreparedStatement statement = connection.prepareStatement(query);
            setValues(statement, currentFrame);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "데이터가 성공적으로 저장되었습니다.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "데이터 저장 중 오류가 발생했습니다.");
        }
    }

    private String generateInsertQuery(String tableName) {
        switch (tableName) {
            case "Movies":
                return "INSERT INTO Movies (MovieID, Title, MovieTime, Rating, Director, Genre, Introduction, ReleaseDate, Score) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            case "MovieActors":
                return "INSERT INTO MovieActors (MovieID, ActorID) VALUES (?, ?)";
            case "Actors":
                return "INSERT INTO Actors (ActorID, ActorName) VALUES (?, ?)";
            case "Screenings":
                return "INSERT INTO Screenings (ScreeningID, MovieID, TheaterID, ScreeningDate, SessionNumber, StartTime, EndTime) VALUES (?, ?, ?, ?, ?, ?, ?)";
            case "Theaters":
                return "INSERT INTO Theaters (TheaterID, ScreeningDate, NumberOfSeats, TheaterUse, HorizontalSeats, VerticalSeats) VALUES (?, ?, ?, ?, ?, ?)";
            case "Tickets":
                return "INSERT INTO Tickets (TicketID, ScreeningID, ScreeningDate, TheaterID, SeatID, BookingID, IsTicketing, StandardPrice, SalePrice) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            case "Seats":
                return "INSERT INTO Seats (SeatID, TheaterID, ScreeningDate, IsActive) VALUES (?, ?, ?, ?)";
            case "Customers":
                return "INSERT INTO Customers (CustomerID, CustomerName, PhoneNumber, Email) VALUES (?, ?, ?, ?)";
            case "Bookings":
                return "INSERT INTO Bookings (BookingID, Payment, PaymentStatus, Amount, CustomerID, PaymentDate) VALUES (?, ?, ?, ?, ?, ?)";
            default:
                throw new IllegalArgumentException("알 수 없는 테이블 이름: " + tableName);
        }
    }

    private void setValues(PreparedStatement statement, JFrame frame) throws SQLException {
        Component[] components = frame.getContentPane().getComponents();
        int index = 1;
        for (Component component : components) {
            if (component instanceof JTextField) {
                statement.setString(index++, ((JTextField) component).getText());
            } else if (component instanceof JTextArea) {
                statement.setString(index++, ((JTextArea) component).getText());
            } else if (component instanceof JCheckBox) {
                statement.setBoolean(index++, ((JCheckBox) component).isSelected());
            }
        }
    }

    private void addInputFields(JFrame frame, Component... components) {
        for (Component component : components) {
            frame.add(component);
        }
        JButton saveButton = new JButton("저장");
        saveButton.addActionListener(e -> saveData(e, frame.getTitle().substring(3))); // frame title is "추가 " + tableName
        frame.add(saveButton);
        JButton cancelButton = new JButton("취소");
        cancelButton.addActionListener(e -> frame.dispose());
        frame.add(cancelButton);
    }

    private JFrame createFrame(String title, int width, int height) {
        JFrame frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setLayout(new GridLayout(0, 2));
        frame.setVisible(true);
        return frame;
    }
}
