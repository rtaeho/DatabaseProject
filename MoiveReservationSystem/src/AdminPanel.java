import javax.swing.*;
import java.awt.*;

public class AdminPanel extends JPanel {
    public AdminPanel() {
        setLayout(new BorderLayout());

        JButton initButton = new JButton("초기화");
        JButton viewAllButton = new JButton("전체 테이블 조회");
        JButton insertButton = new JButton("입력");
        JButton deleteButton = new JButton("삭제");
        JButton updateButton = new JButton("변경");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(initButton);
        buttonPanel.add(viewAllButton);
        buttonPanel.add(insertButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);

        add(buttonPanel, BorderLayout.NORTH);

        initButton.addActionListener(e -> initializeDatabase());
        viewAllButton.addActionListener(e -> viewAllTables());
        insertButton.addActionListener(e -> new MovieInputComponent());
        deleteButton.addActionListener(e -> new MovieDeleteComponent());
        updateButton.addActionListener(e -> new MovieUpdateComponent());
    }

    private void initializeDatabase() {
        // 초기화 로직 구현
    }

    private void viewAllTables() {
        // 전체 테이블 조회 로직 구현
    }
}
