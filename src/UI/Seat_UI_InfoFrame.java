package UI;

import DAO.MemberDAO;
import DAO.SeatDAO;
import DAO.UsageHistoryDAO;
import DTO.MemberDTO;
import DTO.SeatDTO;
import DTO.UsageHistoryDTO;
import Jdbc.PCPosDBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;

public class Seat_UI_InfoFrame extends JFrame {

    public Seat_UI_InfoFrame(int seat_no) {

        //좌석 번호
        setTitle("좌석정보");
        JPanel seat_Info = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel seat_0 = new JLabel("좌석번호: ");
        JLabel seat_1 = new JLabel();
        seat_Info.add(seat_0);
        seat_Info.add(seat_1);

        //이름
        JPanel name_Info = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel name_0 = new JLabel("이름: ");
        JLabel name_1 = new JLabel();
        name_Info.add(name_0);
        name_Info.add(name_1);

        //id
        JPanel nickname_Info = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel nickname_0 = new JLabel("닉네임: ");
        JLabel nickname_1 = new JLabel();
        nickname_Info.add(nickname_0);
        nickname_Info.add(nickname_1);

        //사용시간 실시간
        JPanel usedTime_Info = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel usedTime_0 = new JLabel("사용 시간: ");
        JLabel usedTime_1 = new JLabel();
        usedTime_Info.add(usedTime_0);
        usedTime_Info.add(usedTime_1);

        //남은시간 실시간
        JPanel leftTime_Info = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel leftTime_0 = new JLabel("남은 시간: ");
        JLabel leftTime_1 = new JLabel();
        leftTime_Info.add(leftTime_0);
        leftTime_Info.add(leftTime_1);

        //사용중 미사용중 비활성화
        JPanel seat_State_Info = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel seat_State_Label = new JLabel("좌석 상태: ");
        JLabel seat_State = new JLabel();
        seat_State_Info.add(seat_State_Label);
        seat_State_Info.add(seat_State);

        //TODO완료
        // - 좌석번호 seat, 이름 member, 닉네임 member, (조인 사용) , 사용시간,남은시간 usage_history, 좌석상태 seat
        // 3개 테이블 불러와야함

        Connection conn = PCPosDBConnection.getConnection();
        MemberDAO memberDAO = new MemberDAO(conn);
        SeatDAO seatDAO = new SeatDAO(conn);
        UsageHistoryDAO usageHistoryDAO = new UsageHistoryDAO(conn);
        MemberDTO member;

        if (conn != null) {
            member = memberDAO.joinSeat(String.valueOf(seat_no));
            LocalDateTime loginTime = usageHistoryDAO.getLoginTime(member);
            Timer timer = new Timer(1000, e -> { //서정우가 함, 1초마다 e를 수행
                LocalDateTime now = LocalDateTime.now(); //현재 시간을 가져옴
                Duration duration = Duration.between(loginTime, now); //로그인 시작 시간과 현재 시간의 차이를 계산

                //전체 초 계산
                long totalSeconds = duration.getSeconds();
                long hours = totalSeconds / 3600; //시
                long minutes = (totalSeconds % 3600) / 60; //분(전체 초에서 시간에 해당하는 초를 제외하고 게산)
                long seconds = totalSeconds % 60; //초
                usedTime_1.setText(hours + "시간 " + minutes + "분 " + seconds + "초"); //시간 표시
            });
            timer.start();
            if (member != null) {
                SeatDTO seat = seatDAO.findById(String.valueOf(seat_no));

                seat_1.setText(String.valueOf(seat_no));
                name_1.setText(member.getMember_name());
                nickname_1.setText(member.getMember_id());
                seat_State.setText(seat.getSeat_state());
            }
        }

        setLayout(new GridLayout(6, 1, 0, 10));
        add(seat_Info);
        add(name_Info);
        add(nickname_Info);
        add(usedTime_Info);
        add(leftTime_Info);
        add(seat_State_Info);

        Container c = getContentPane();
        c.setFont(new Font("Arial", Font.BOLD, 14));

        setLocationRelativeTo(null);
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
    }
}
