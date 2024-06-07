import javax.swing.*;

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("온라인 영화 예매 시스템");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(800, 600);
			frame.setLocationRelativeTo(null);

			// MainPanel 인스턴스를 생성하고 프레임에 추가
			MainPanel mainPanel = new MainPanel();
			frame.add(mainPanel);
			frame.setVisible(true);
		});
	}
}
