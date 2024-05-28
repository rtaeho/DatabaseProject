import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {

	public MainPanel() {
		setLayout(new BorderLayout());

		JLabel titleLabel = new JLabel("온라인 영화 예매 시스템", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

		JButton adminButton = new JButton("관리자");
		adminButton.setPreferredSize(new Dimension(200, 100));
		adminButton.addActionListener(e -> Utils.switchToPanel(this, new AdminPanel()));

		JButton userButton = new JButton("사용자");
		userButton.setPreferredSize(new Dimension(200, 100));
		userButton.addActionListener(e -> Utils.switchToPanel(this, new UserPanel()));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 50));
		buttonPanel.add(adminButton);
		buttonPanel.add(userButton);

		add(titleLabel, BorderLayout.NORTH);
		add(buttonPanel, BorderLayout.CENTER);
	}

}
