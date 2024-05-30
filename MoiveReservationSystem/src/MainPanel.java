import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MainPanel extends JPanel {

    public MainPanel() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("online 영화 예매 시스템", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Sansserif", Font.BOLD, 24));

        JButton adminButton = new JButton("관리자");
        adminButton.setPreferredSize(new Dimension(200, 100));
        adminButton.addActionListener(e -> {
            // 데이터베이스 연결 시도
            Connection dbConnection = Utils.connectToDatabase("root", "1234"); // 여기서 사용자 이름과 비밀번호를 입력하세요.
            if (dbConnection != null) {
                // 연결 성공 시 AdminPanel로 전환
            	Utils.showMessage("DB 연결 성공");
                Utils.switchToPanel(this, new AdminPanel(dbConnection));
            }
        });

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
