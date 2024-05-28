import javax.swing.*;
import java.awt.*;

public class MovieInputComponent extends JFrame {
	public MovieInputComponent() {
		setTitle("Add Movie");
		setSize(400, 400);
		setLayout(new GridLayout(10, 2));

		JLabel idLabel = new JLabel("MovieID:");
		JTextField idField = new JTextField();
		JLabel titleLabel = new JLabel("Title:");
		JTextField titleField = new JTextField();
		JLabel timeLabel = new JLabel("MovieTime:");
		JTextField timeField = new JTextField();
		JLabel ratingLabel = new JLabel("Rating:");
		JTextField ratingField = new JTextField();
		JLabel directorLabel = new JLabel("Director:");
		JTextField directorField = new JTextField();
		JLabel genreLabel = new JLabel("Genre:");
		JTextField genreField = new JTextField();
		JLabel introLabel = new JLabel("Introduction:");
		JTextArea introField = new JTextArea();
		JLabel dateLabel = new JLabel("ReleaseDate:");
		JTextField dateField = new JTextField();
		JLabel scoreLabel = new JLabel("Score:");
		JTextField scoreField = new JTextField();

		JButton submitButton = new JButton("Submit");

		add(idLabel);
		add(idField);
		add(titleLabel);
		add(titleField);
		add(timeLabel);
		add(timeField);
		add(ratingLabel);
		add(ratingField);
		add(directorLabel);
		add(directorField);
		add(genreLabel);
		add(genreField);
		add(introLabel);
		add(new JScrollPane(introField));
		add(dateLabel);
		add(dateField);
		add(scoreLabel);
		add(scoreField);
		add(new JLabel()); // Empty label for spacing
		add(submitButton);

		submitButton.addActionListener(e -> {
			// 영화 입력 로직 구현
		});

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
