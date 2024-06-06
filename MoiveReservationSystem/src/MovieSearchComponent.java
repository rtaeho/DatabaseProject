import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class MovieSearchComponent extends JFrame {
	private Connection dbConnection;
	private JFrame currentFrame;
	

	public MovieSearchComponent(Connection connection) {
		dbConnection = connection;
		this.currentFrame = this;  // 현재 프레임 참조 설정
		
		openSearchForm();
	}
	
	private void openSearchForm() {	
		currentFrame = createFrame("영화 검색", 600, 600);
		currentFrame.getContentPane().requestFocusInWindow();
		
		JTextField movieNameField = new JTextField();
		JTextField directorField = new JTextField();
		JTextField genreField = new JTextField();
		JTextField actorsField = new JTextField(); // 배우명 필드 추가

		addInputFields(currentFrame,
				new JLabel("영화명:"), movieNameField,
				new JLabel("감독명:"), directorField,
				new JLabel("배우명:"), actorsField,
				new JLabel("장르:"), genreField);
		
		JPanel buttonPanel = searchButtonPanel(e -> {
			String movieName = movieNameField.getText();
			String director = directorField.getText();
			String genre = genreField.getText();
			String actor = actorsField.getText(); // 쉼표로 구분된 배우명

			if (movieName.isEmpty() && director.isEmpty() && genre.isEmpty() && actor.isEmpty()) {
				JOptionPane.showMessageDialog(currentFrame, "하나의 값 이상 입력해야 합니다.", "입력 오류", JOptionPane.WARNING_MESSAGE);
			} else {
				Utils.showMessage("영화를 검색 중 입니다.");
				showMovieList(movieName, director, genre, actor);
				currentFrame.dispose();
			}
			
			//currentFrame.dispose();
		}, e -> currentFrame.dispose());
		currentFrame.add(buttonPanel);
	}
	
	private JFrame createFrame(String title, int width, int height) {
		JFrame frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setLayout(new GridLayout(0, 1));
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		return frame;
	}
	
	private void addInputFields(JFrame frame, JComponent... components) {
		for (JComponent component : components) {
			frame.add(component);
		}
	}
	
	private JPanel searchButtonPanel(ActionListener saveAction, ActionListener cancelAction) {
		JPanel buttonPanel = new JPanel();

		JButton searchButton = new JButton("검색");
		searchButton.addActionListener(saveAction);

		JButton cancelButton = new JButton("취소");
		cancelButton.addActionListener(cancelAction);

		buttonPanel.add(searchButton);
		buttonPanel.add(cancelButton);

		return buttonPanel;
	}
	public void showMovieList(String movieName, String director, String genre, String actor) {
	    // 영화 목록 조회 로직 구현
	    Statement stmt = null;
	    ResultSet resultSet = null;

	    try {
	        stmt = dbConnection.createStatement();

	        String query = setQuery(movieName, director, genre, actor);
	        resultSet = stmt.executeQuery(query);

	        // 결과 데이터를 JTable에 추가
	        String[] columnNames = {"MovieID", "MovieTitle"};
	        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

	        boolean hasResults = false;

	        while (resultSet.next()) {
	            hasResults = true;
	            int movieID = resultSet.getInt("MovieID");
	            String movieTitle = resultSet.getString("Title");
	            model.addRow(new Object[]{movieID, movieTitle});
	        }

	        JTable table = new JTable(model);
	        JScrollPane scrollPane = new JScrollPane(table);

	        table.setRowHeight(30); // 원하는 높이로 설정

	        if (!hasResults) {
	            JOptionPane.showMessageDialog(null, "해당하는 영화가 없습니다", "정보", JOptionPane.INFORMATION_MESSAGE);
	            return;
	        }

	        // 새로운 JFrame 생성
	        JFrame frame = new JFrame("영화 조회");
	        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        frame.setLayout(new BorderLayout());

	        // 버튼 패널 생성
	        JPanel buttonPanel = new JPanel();

	        // 예매 버튼 추가 및 위치와 크기 설정
	        JButton bookButton = new JButton("예매");
	        bookButton.setPreferredSize(new Dimension(100, 25)); // 버튼 크기 설정
	        buttonPanel.add(bookButton); // 패널에 예매 버튼 추가

	        // 취소 버튼 추가 및 위치와 크기 설정
	        JButton cancelButton = new JButton("취소");
	        cancelButton.setPreferredSize(new Dimension(100, 25)); // 버튼 크기 설정
	        buttonPanel.add(cancelButton); // 패널에 취소 버튼 추가

	        // 버튼 패널을 프레임 상단에 추가
	        frame.add(buttonPanel, BorderLayout.NORTH);

	        // 테이블 스크롤 패인을 가운데에 추가
	        frame.add(scrollPane, BorderLayout.CENTER);

	        frame.setSize(800, 600);
	        frame.setVisible(true);

	        // 예매 버튼 이벤트 처리
	        bookButton.addActionListener(e -> {
	            int selectedRow = table.getSelectedRow();
	            if (selectedRow >= 0) {
	                int movieID = (int) model.getValueAt(selectedRow, 0);
	                String movieTitle = (String) model.getValueAt(selectedRow, 1);
	                // 여기서 예매 처리 로직을 구현하세요.
	                JOptionPane.showMessageDialog(frame, movieTitle + " 예매 완료!", "예매 성공", JOptionPane.INFORMATION_MESSAGE);
	            } else {
	                JOptionPane.showMessageDialog(frame, "영화를 선택하세요.", "예매 오류", JOptionPane.ERROR_MESSAGE);
	            }
	        });

	        // 취소 버튼 이벤트 처리
	        cancelButton.addActionListener(e -> frame.dispose());

	    } catch (SQLException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "테이블 조회 중 오류가 발생했습니다: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    } finally {
	        try {
	            if (resultSet != null) {
	                resultSet.close();
	            }
	            if (stmt != null) {
	                stmt.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}

    private String setQuery(String movieName, String director, String genre, String actor) {
        // 기본 쿼리 문자열 생성
    	// 한 영화에 배우 여러명이라 같은 항목 생김 -> DISTINCT로 중복값 제거
        String query = "SELECT DISTINCT Movies.MovieID, Movies.Title " +
                       "FROM Movies " +
                       "JOIN MovieActors ON Movies.MovieID = MovieActors.MovieID " +
                       "JOIN Actors ON MovieActors.ActorID = Actors.ActorID";
        boolean hasCondition = false;

        // 조건 추가
        if (!movieName.isEmpty()) {
            query += " WHERE Movies.Title = '" + movieName + "'";
            hasCondition = true;
        }

        if (!director.isEmpty()) {
            if (hasCondition) {
                query += " AND";
            } else {
                query += " WHERE";
                hasCondition = true;
            }
            query += " Movies.Director = '" + director + "'";
        }

        if (!genre.isEmpty()) {
            if (hasCondition) {
                query += " AND";
            } else {
                query += " WHERE";
                hasCondition = true;
            }
            query += " Movies.Genre = '" + genre + "'";
        }

        if (!actor.isEmpty()) {
            if (hasCondition) {
                query += " AND";
            } else {
                query += " WHERE";
                hasCondition = true;
            }
            query += " Actors.ActorName = '" + actor + "'";
        }

        return query;
    }


}

/*
private void showMovieList(String movieName, String director, String genre, String actor) {
    // 영화 목록 조회 로직 구현
    Statement stmt = null;
    ResultSet resultSet = null;
         
    try {
        stmt = dbConnection.createStatement();
        
        String query = setQuery(movieName, director, genre, actor);
        resultSet = stmt.executeQuery(query);

        // 결과 데이터를 JTable에 추가
        String[] columnNames = {"MovieID", "MovieTitle"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        
        boolean hasResults = false;
        
        while (resultSet.next()) {
        	hasResults = true;
            int movieID = resultSet.getInt("MovieID");
            String movieTitle = resultSet.getString("Title");
            model.addRow(new Object[]{movieID, movieTitle});
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        
        table.setRowHeight(30); // 원하는 높이로 설정
        
        if (!hasResults) {
            JOptionPane.showMessageDialog(null, "해당하는 영화가 없습니다", "정보", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // 새로운 JFrame 생성
        JFrame frame = new JFrame("영화 조회");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setVisible(true);

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "테이블 조회 중 오류가 발생했습니다: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } finally {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	   	    	
}
*/
