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
/*
1. 영화 테이블 (Movies)
- MovieID (INT, Primary Key): 영화번호
- Title (VARCHAR(255), NOT NULL): 영화명
- MovieTime (INT, NOT NULL): 상영시간 (예 : 105(분), 120(분))
- Rating (VARCHAR(255)): 상영등급 (예: PG, R-18 등)
- Director (VARCHAR(255)): 감독명
- Genre (VARCHAR(255)): 장르 (예: Action, Drama 등)
- Introduction (TEXT): 영화소개
- ReleaseDate (DATE): 개봉일자
- Score (DECIMAL(2,1)): 평점

2. 영화 - 배우 관계 테이블 (MovieActors)
- MovieID (INT, Foreign Key, References Movies(MovieID)):
영화번호
- ActorID (INT, Foreign Key, References Actors(ActorID)):
배우번호

3. 영화배우 테이블 (Actors)
- ActorID (INT, Primary Key): 배우번호
- ActorName (VARCHAR(255)): 배우명

4. 상영일정 테이블 (Screenings)
- ScreeningID (INT, Primary Key): 상영일정번호
- MovieID (INT, Foreign Key, References Movies(MovieID)):
영화번호
- TheaterID (INT)): 상영관번호
- ScreeningDate (DATE): 상영일
- SessionNumber (INT): 상영회차
- StartTime (TIME): 상영시작시간
- EndTime (TIME): 상영종료시간
- FOREIGN KEY (TheaterID, ScreeningDate) REFERENCES
Theaters(TheaterID, ScreeningDate)

5. 상영관 테이블 (Theaters)
- TheaterID (INT): 상영관번호
- ScreeningDate (DATE): 상영일
- NumberOfSeats (INT): 좌석수
- TheaterUse (BOOLEAN): 상영관사용여부
- HorizontalSeats (INT): 좌석 가로 수
- VerticalSeats (INT): 좌석 세로 수
- PRIMARY KEY (TheaterID, ScreeningDate)

6. 티켓 테이블 (Tickets)
- TicketID (INT, Primary Key): 티켓번호
- ScreeningID (INT, Foreign Key, References
Screenings(ScreeningID)): 상영일정번호
- ScreeningDate (DATE): 상영일
- TheaterID (INT): 상영관번호
- SeatID (INT, Foreign Key, References Seats(SeatID)): 좌석번호
- BookingID (INT, Foreign Key, References
Bookings(BookingID)): 예매번호
- IsTicketing (BOOLEAN): 발권여부
- StandardPrice (INT): 표준가격
- SalePrice (INT): 판매가격
- FOREIGN KEY (TheaterID, ScreeningDate) REFERENCES
Theaters(TheaterID, ScreeningDate)

7. 좌석 테이블 (Seats)
- SeatID (INT, Primary Key): 좌석 번호
- TheaterID (INT): 상영관 번호
- ScreeningDate (DATE): 상영일
- IsActive (BOOLEAN): 좌석 사용 여부
- FOREIGN KEY (TheaterID, ScreeningDate) REFERENCES
Theaters(TheaterID, ScreeningDate)

8. 회원고객 테이블 (Customers)
- CustomerID (VARCHAR(255), Primary Key): 회원 아이디
- CustomerName (VARCHAR(255)): 고객명
- PhoneNumber (VARCHAR(15)): 휴대폰번호
- Email (VARCHAR(255)): 전자메일주소

9. 예매정보 테이블 (Bookings)
- BookingID (INT, Primary Key, AUTO_INCREMENT): 예매번호
- Payment (VARCHAR(50)): 결제 방법
- PaymentStatus (VARCHAR(50)): 결제 상태
- Amount (INT): 결제 금액
- CustomerID (VARCHAR(255), Foreign Key, References
Customers(CustomerID)): 회원 아이디
- PaymentDate (DATE): 결제 일자
*/