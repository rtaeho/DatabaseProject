import javax.swing.*;
import java.awt.*;

public class SeatSelectionComponent extends JFrame {
	public SeatSelectionComponent() {
		setTitle("Select Seat");
		setSize(400, 400);
		setLayout(new GridLayout(10, 10));

		// 좌석 선택 UI 구현
		// ...

		JButton confirmButton = new JButton("Confirm");
		add(confirmButton);

		confirmButton.addActionListener(e -> {
			// 좌석 선택 확인 로직 구현
		});

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
