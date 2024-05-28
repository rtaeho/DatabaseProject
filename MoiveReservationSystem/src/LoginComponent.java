import javax.swing.*;
import java.awt.*;

public class LoginComponent extends JPanel {
	public LoginComponent() {
		setLayout(new GridLayout(3, 2));

		JLabel userLabel = new JLabel("Username:");
		JTextField userField = new JTextField();
		JLabel passLabel = new JLabel("Password:");
		JPasswordField passField = new JPasswordField();
		JButton loginButton = new JButton("Login");

		add(userLabel);
		add(userField);
		add(passLabel);
		add(passField);
		add(new JLabel()); // Empty label for spacing
		add(loginButton);

		loginButton.addActionListener(e -> {
			String username = userField.getText();
			String password = new String(passField.getPassword());
			DatabaseConnector.connectToDatabase(username, password);
		});
	}
}
