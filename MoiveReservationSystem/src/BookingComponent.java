import javax.swing.*;
import java.awt.*;

public class BookingComponent extends JFrame {
	public BookingComponent() {
		setTitle("Book Movie");
		setSize(400, 400);
		setLayout(new GridLayout(5, 2));

		JLabel movieIdLabel = new JLabel("MovieID:");
		JTextField movieIdField = new JTextField();
		JLabel customerIdLabel = new JLabel("CustomerID:");
		JTextField customerIdField = new JTextField();
		JLabel paymentLabel = new JLabel("Payment:");
		JTextField paymentField = new JTextField();
		JLabel paymentStatusLabel = new JLabel("PaymentStatus:");
		JTextField paymentStatusField = new JTextField();
		JLabel amountLabel = new JLabel("Amount:");
		JTextField amountField = new JTextField();

		JButton bookButton = new JButton("Book");

		add(movieIdLabel);
		add(movieIdField);
		add(customerIdLabel);
		add(customerIdField);
		add(paymentLabel);
		add(paymentField);
		add(paymentStatusLabel);
		add(paymentStatusField);
		add(amountLabel);
		add(amountField);
		add(new JLabel()); // Empty label for spacing
		add(bookButton);

		bookButton.addActionListener(e -> {
			// 예매 로직 구현
		});

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
