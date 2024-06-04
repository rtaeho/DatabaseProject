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
		addButton("Screenings");
		addButton("Theaters");
		addButton("Tickets");
		addButton("Seats");
		addButton("Customers");
		addButton("Bookings");

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null); // 창을 화면 중앙에 배치
		setVisible(true);
	}

	private void addButton(String tableName) {
		JButton button = new JButton(tableName);
		button.addActionListener(e -> openInputForm(e, tableName));
		add(button);
	}

	private void openInputForm(ActionEvent e, String tableName) {
		currentFrame = createFrame("추가 " + tableName, 400, 400);
		currentFrame.getContentPane().requestFocusInWindow();
		switch (tableName) {
		case "Movies":
			addInputFields(currentFrame, new JLabel("영화명:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 기생충", false), new JLabel("상영시간(분):"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 131", false), new JLabel("상영등급:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 15세 이상 관람가", false), new JLabel("감독명:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 봉준호", false), new JLabel("배우:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 송강호, 이선균, 최우식", false), new JLabel("장르:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 드라마", false), new JLabel("영화소개:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 전원백수로 살 길 막막하지만 사이는 좋은 기택(송강호) 가족...",
							true),
					new JLabel("개봉일자:"), PlaceholderComponent.createPlaceholderTextComponent("ex) 2019.05.30", false),
					new JLabel("평점:"), PlaceholderComponent.createPlaceholderTextComponent("ex) 9.1", false));
			break;
		case "Screenings":
			addInputFields(currentFrame, new JLabel("영화번호:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 1", false), new JLabel("상영관번호:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 1", false), new JLabel("상영시작일:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 2024.01.01", false), new JLabel("상영일:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 2024.01.03", false), new JLabel("상영회차:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 1", false), new JLabel("상영시작시간:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 10:00", false));
			break;
		case "Theaters":
			addInputFields(currentFrame, new JLabel("상영관번호:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 1", false), new JLabel("좌석수:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 100", false), new JLabel("상영관사용여부:"),
					new JCheckBox(), new JLabel("가로좌석수:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 10", false), new JLabel("세로좌석수:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 10", false));
			break;
		case "Tickets":
			addInputFields(currentFrame, new JLabel("상영일정번호:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 1", false), new JLabel("상영일:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 2024.01.03", false), new JLabel("상영관번호:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 1", false), new JLabel("좌석번호:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 1", false), new JLabel("예매번호:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 1", false), new JLabel("발권여부:"),
					new JCheckBox(), new JLabel("표준가격:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 10000", false), new JLabel("판매가격:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 9000", false));
			break;
		case "Seats":
			addInputFields(currentFrame, new JLabel("상영관번호:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 1", false), new JLabel("상영일:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 2024.01.03", false), new JLabel("좌석사용여부:"),
					new JCheckBox());
			break;
		case "Customers":
			addInputFields(currentFrame, new JLabel("회원아이디:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) kim123", false), new JLabel("고객:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 김세종", false), new JLabel("휴대폰번호:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 010-1234-5678", false),
					new JLabel("전자메일주소:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) sejong@sju.ac.kr", false));
			break;
		case "Bookings":
			addInputFields(currentFrame, new JLabel("결제방법:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 카드", false), new JLabel("결제상태:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 완료", false), new JLabel("결제금액:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 9000", false), new JLabel("회원아이디:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 1", false), new JLabel("결제 일자:"),
					PlaceholderComponent.createPlaceholderTextComponent("ex) 2024.01.02", false));
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
				JTextField textField = (JTextField) component;
				statement.setString(index++,
						textField.getText().equals(textField.getForeground().equals(Color.GRAY)) ? ""
								: textField.getText());
			} else if (component instanceof JTextArea) {
				JTextArea textArea = (JTextArea) component;
				statement.setString(index++, textArea.getText().equals(textArea.getForeground().equals(Color.GRAY)) ? ""
						: textArea.getText());
			} else if (component instanceof JCheckBox) {
				statement.setBoolean(index++, ((JCheckBox) component).isSelected());
			}
		}
	}

	private void addInputFields(JFrame frame, Component... components) {
		JPanel inputPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5); // 컴포넌트 간의 여백 설정

		// 입력 필드 추가
		for (int i = 0; i < components.length; i++) {
			gbc.gridx = 0;
			gbc.gridy = i;
			gbc.weightx = 1.0; // 수평 방향으로 공간을 차지하도록 설정
			gbc.weighty = 0.0; // 수직 방향으로는 공간을 차지하지 않도록 설정
			gbc.gridwidth = GridBagConstraints.REMAINDER; // 한 줄 전체를 차지하도록 설정
			inputPanel.add(components[i], gbc);
		}

		// 저장 버튼 추가
		JButton saveButton = new JButton("저장");
		saveButton.addActionListener(e -> saveData(e, frame.getTitle().substring(3))); // frame title is "추가 " +
																						// tableName
		gbc.gridy = components.length; // 저장 버튼의 위치 설정
		inputPanel.add(saveButton, gbc);

		// 취소 버튼 추가
		JButton cancelButton = new JButton("취소");
		cancelButton.addActionListener(e -> frame.dispose());
		gbc.gridy = components.length + 1; // 취소 버튼의 위치 설정
		inputPanel.add(cancelButton, gbc);

		frame.add(inputPanel); // 입력 패널을 프레임에 추가
		frame.pack(); // 프레임 크기 조정
		frame.setLocationRelativeTo(null); // 프레임을 화면 중앙에 배치
	}

	private JFrame createFrame(String title, int width, int height) {
		JFrame frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setLayout(new GridLayout(0, 2));
		frame.setVisible(true);
		return frame;
	}
}
