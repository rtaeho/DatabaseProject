package test;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class test1 {

    public static void main(String[] args) {
        // Create the frame
        JFrame frame = new JFrame("Database Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        
        // Create the panel to hold the buttons
        JPanel panel = new JPanel();
        
        // Create the buttons
        JButton adminButton = new JButton("관리자 로그인");
        JButton userButton = new JButton("유저 로그인");
        
        // Add action listeners to the buttons
        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToDatabase("root", "1234");
            }
        });
        
        userButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToDatabase("user1", "user1");
            }
        });
        
        // Add buttons to the panel
        panel.add(adminButton);
        panel.add(userButton);
        
        // Add panel to the frame
        frame.add(panel);
        
        // Set the frame visibility
        frame.setVisible(true);
    }
    
    private static void connectToDatabase(String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load the MySQL driver
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/db1", username, password); // Connect to the database
            JOptionPane.showMessageDialog(null, "DB 연결 완료: " + username);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "JDBC 드라이버 로드 오류", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "DB 연결 오류", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
