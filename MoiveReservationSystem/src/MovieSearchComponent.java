import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MovieSearchComponent extends JFrame {
	private Connection connection;
	private JFrame currentFrame;
	

	public MovieSearchComponent(Connection connection) {
		this.connection = connection;
		this.currentFrame = this;  // 현재 프레임 참조 설정
		
		openSearchForm();
	}
	
	private void openSearchForm() {	
		currentFrame = createFrame("영화 검색", 600, 600);
		currentFrame.getContentPane().requestFocusInWindow();
		
		JTextField movieNameField = new JTextField();
		JTextField directorField = new JTextField();
		JTextField genreField = new JTextField();
		JTextField actorsField = new JTextField(); // 배우명 필드 추가

		addInputFields(currentFrame,
				new JLabel("영화명:"), movieNameField,
				new JLabel("감독명:"), directorField,
				new JLabel("배우명:"), actorsField,
				new JLabel("장르:"), genreField);
		
		JPanel buttonPanel = searchButtonPanel(e -> {
			String movieName = movieNameField.getText();
			String director = directorField.getText();
			String genre = genreField.getText();
			String actor = actorsField.getText(); // 쉼표로 구분된 배우명

			if (movieName.isEmpty() && director.isEmpty() && genre.isEmpty() && actor.isEmpty()) {
				JOptionPane.showMessageDialog(currentFrame, "하나의 값 이상 입력해야 합니다.", "입력 오류", JOptionPane.WARNING_MESSAGE);
			} else {
				Utils.showMessage("영화를 검색 중 입니다.");
				currentFrame.dispose();
			}
			
			//currentFrame.dispose();
		}, e -> currentFrame.dispose());
		currentFrame.add(buttonPanel);
	}
	
	private JFrame createFrame(String title, int width, int height) {
		JFrame frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setLayout(new GridLayout(0, 1));
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		return frame;
	}
	
	private void addInputFields(JFrame frame, JComponent... components) {
		for (JComponent component : components) {
			frame.add(component);
		}
	}
	
	private JPanel searchButtonPanel(ActionListener saveAction, ActionListener cancelAction) {
		JPanel buttonPanel = new JPanel();

		JButton searchButton = new JButton("검색");
		searchButton.addActionListener(saveAction);

		JButton cancelButton = new JButton("취소");
		cancelButton.addActionListener(cancelAction);

		buttonPanel.add(searchButton);
		buttonPanel.add(cancelButton);

		return buttonPanel;
	}
}


