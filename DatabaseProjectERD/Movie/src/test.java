import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class test extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public test() {
        setTitle("영화 예매 시스템");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 로그인 화면 추가
        mainPanel.add(createLoginPanel(), "Login");

        // 관리자 인터페이스 추가
        mainPanel.add(createAdminPanel(), "Admin");

        // 회원 인터페이스 추가
        mainPanel.add(createUserPanel(), "User");

        add(mainPanel);
        cardLayout.show(mainPanel, "Login");
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel userLabel = new JLabel("사용자 유형:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(userLabel, gbc);

        String[] userTypes = {"관리자", "회원"};
        JComboBox<String> userTypeComboBox = new JComboBox<>(userTypes);
        gbc.gridx = 1;
        panel.add(userTypeComboBox, gbc);

        JLabel usernameLabel = new JLabel("사용자 이름:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("비밀번호:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        JButton loginButton = new JButton("로그인");
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(loginButton, gbc);

        JLabel messageLabel = new JLabel();
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(messageLabel, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userType = (String) userTypeComboBox.getSelectedItem();
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (userType.equals("관리자") && username.equals("root") && password.equals("1234")) {
                    cardLayout.show(mainPanel, "Admin");
                } else if (userType.equals("회원") && username.equals("user1") && password.equals("user1")) {
                    cardLayout.show(mainPanel, "User");
                } else {
                    messageLabel.setText("로그인 실패! 다시 시도하세요.");
                }
            }
        });

        return panel;
    }

    private JPanel createAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // 상단 패널
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnInitDB = new JButton("데이터베이스 초기화");
        JButton btnInsert = new JButton("데이터 삽입");
        JButton btnDelete = new JButton("데이터 삭제");
        JButton btnUpdate = new JButton("데이터 변경");
        JButton btnViewAll = new JButton("전체 테이블 보기");

        topPanel.add(btnInitDB);
        topPanel.add(btnInsert);
        topPanel.add(btnDelete);
        topPanel.add(btnUpdate);
        topPanel.add(btnViewAll);

        // 메인 패널
        JPanel mainAdminPanel = new JPanel();
        mainAdminPanel.setLayout(new CardLayout());

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(mainAdminPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createUserPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // 상단 패널
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnSearchMovies = new JButton("영화 조회");
        JButton btnBookTicket = new JButton("영화 예매");
        JButton btnViewBookings = new JButton("예매 조회");
        JButton btnCancelBooking = new JButton("예매 취소");

        topPanel.add(btnSearchMovies);
        topPanel.add(btnBookTicket);
        topPanel.add(btnViewBookings);
        topPanel.add(btnCancelBooking);

        // 메인 패널
        JPanel mainUserPanel = new JPanel();
        mainUserPanel.setLayout(new CardLayout());

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(mainUserPanel, BorderLayout.CENTER);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new test().setVisible(true);
            }
        });
    }
}
