import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("온라인 영화 예매 시스템");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            JPanel adminPanel = new AdminPanel();
            JPanel userPanel = new UserPanel();

            // "온라인 영화 예매 시스템" 문구
            JLabel titleLabel = new JLabel("온라인 영화 예매 시스템", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

            // "관리자" 버튼
            JButton adminButton = new JButton("관리자");
            adminButton.setPreferredSize(new Dimension(200, 100));
            adminButton.addActionListener(e -> {
                frame.getContentPane().removeAll();
                frame.getContentPane().add(adminPanel, BorderLayout.CENTER);
                frame.revalidate();
                frame.repaint();
            });

            // "사용자" 버튼
            JButton userButton = new JButton("사용자");
            userButton.setPreferredSize(new Dimension(200, 100));
            userButton.addActionListener(e -> {
                frame.getContentPane().removeAll();
                frame.getContentPane().add(userPanel, BorderLayout.CENTER);
                frame.revalidate();
                frame.repaint();
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 50));
            buttonPanel.add(adminButton);
            buttonPanel.add(userButton);

            JPanel topPanel = new JPanel(new BorderLayout());
            JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            topPanel.add(titleLabel, BorderLayout.NORTH);
            topPanel.add(loginPanel, BorderLayout.CENTER);

            frame.add(topPanel, BorderLayout.NORTH);
            frame.add(buttonPanel, BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }
}
