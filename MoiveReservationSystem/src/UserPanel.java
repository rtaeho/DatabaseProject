import javax.swing.*;
import java.awt.*;

public class UserPanel extends JPanel {
    public UserPanel() {
        setLayout(new BorderLayout());

        JButton viewMoviesButton = new JButton("영화 조회");
        JButton viewBookingsButton = new JButton("예매한 영화 조회");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(viewMoviesButton);
        buttonPanel.add(viewBookingsButton);

        add(buttonPanel, BorderLayout.NORTH);

        viewMoviesButton.addActionListener(e -> new BookingComponent());
        viewBookingsButton.addActionListener(e -> viewUserBookings());
    }

    private void viewUserBookings() {
        // 예매한 영화 조회 로직 구현
    }
}
