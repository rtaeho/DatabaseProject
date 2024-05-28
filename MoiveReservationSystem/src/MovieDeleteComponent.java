import javax.swing.*;
import java.awt.*;

public class MovieDeleteComponent extends JFrame {
	public MovieDeleteComponent() {
		setTitle("Delete Movie");
		setSize(300, 150);
		setLayout(new GridLayout(3, 2));

		JLabel idLabel = new JLabel("MovieID:");
		JTextField idField = new JTextField();
		JButton deleteButton = new JButton("Delete");

		add(idLabel);
		add(idField);
		add(new JLabel()); // Empty label for spacing
		add(deleteButton);

		deleteButton.addActionListener(e -> {
			// 영화 삭제 로직 구현
		});

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
