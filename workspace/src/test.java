import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class test {
    public static void main(String[] args) {
        // MySQL 연결 정보 설정
        String url = "jdbc:mysql://localhost:3306";
        String user = "root";  // MySQL 설치 시 설정한 사용자명
        String password = "1234";  // MySQL 설치 시 설정한 비밀번호 

        try {
            // MySQL JDBC 드라이버 로드
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 데이터베이스 연결
            Connection connection = DriverManager.getConnection(url, user, password);

            // Statement 객체 생성
            Statement statement = connection.createStatement();

            // 데이터베이스 생성 쿼리 실행
            String createDBQuery = "CREATE DATABASE testdb";
            statement.executeUpdate(createDBQuery);
            System.out.println("Database created successfully...");

            // 데이터베이스 선택
            statement.execute("USE testdb");

            // 테이블 생성 쿼리 실행
            String createTableQuery = "CREATE TABLE users (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(50), email VARCHAR(50))";
            statement.executeUpdate(createTableQuery);
            System.out.println("Table created successfully...");

            // 데이터 삽입 쿼리 실행
            String insertDataQuery = "INSERT INTO users (name, email) VALUES ('John Doe', 'john.doe@example.com')";
            statement.executeUpdate(insertDataQuery);
            System.out.println("Data inserted successfully...");

            // 데이터 조회 쿼리 실행
            String selectQuery = "SELECT * FROM users";
            ResultSet resultSet = statement.executeQuery(selectQuery);

            // 결과 출력
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                System.out.println("ID: " + id + ", Name: " + name + ", Email: " + email);
            }

            // 자원 해제
            resultSet.close();
            statement.close();
            connection.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
// branch test
