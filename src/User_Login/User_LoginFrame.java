package User_Login;

import Common_Panel.Time_Plus_Jpanel;
import DAO.MemberDAO;
import DAO.SeatDAO;
import DAO.UsageHistoryDAO;
import DTO.MemberDTO;
import DTO.SeatDTO;
import DTO.UsageHistoryDTO;
import Jdbc.PCPosDBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class User_LoginFrame extends JFrame {
    JTextField id;
    JPasswordField passwd;
    JButton loginBtn, registerBtn, findBtn, selectSeat;
    JComboBox<Integer> seatChombo; // 좌석 번호는 Integer 타입으로 선언
    String seatNo;
    Time_Plus_Jpanel time_plus_jpanel;

    public User_LoginFrame(String title) {
        setTitle(title);
        setResizable(false);
        setBounds(100, 100, 800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 창을 화면 중앙에 위치
        setLocationRelativeTo(null);

        Container ct = getContentPane();
        ct.setLayout(null);

        //제목
        JLabel titleLabel = new JLabel("사용자 키오스크");
        titleLabel.setFont(new Font("나눔고딕", Font.BOLD, 30));
        titleLabel.setBounds(250, 30, 300, 50);
        ct.add(titleLabel);

        //아이디
        JLabel idLabel = new JLabel("아이디    : ");
        idLabel.setBounds(200, 120, 80, 30);
        ct.add(idLabel);

        //아이디 입려
        id = new JTextField();
        id.setBounds(300, 120, 200, 30);
        ct.add(id);

        //비밀번호
        JLabel pwdLabel = new JLabel("비밀번호 : ");
        pwdLabel.setBounds(200, 180, 80, 30);
        ct.add(pwdLabel);

        //비밀번호 입력
        passwd = new JPasswordField();
        passwd.setBounds(300, 180, 200, 30);
        ct.add(passwd);

        //로그인버튼
        loginBtn = new JButton("로그인");
        loginBtn.setBounds(200, 300, 120, 30);
        ct.add(loginBtn);

        //회원가입 버튼
        registerBtn = new JButton("회원가입");
        registerBtn.setBounds(380, 300, 120, 30);
        ct.add(registerBtn);

        //비밀번호 찾기 버튼
        findBtn = new JButton("비밀번호 찾기");
        findBtn.setBounds(300, 240, 200, 30);
        ct.add(findBtn);

        // 좌석 선택 라벨
        JLabel seatLabel = new JLabel("좌석 선택:");
        seatLabel.setFont(new Font("나눔고딕", Font.PLAIN, 16));
        seatLabel.setBounds(200, 360, 80, 30);
        ct.add(seatLabel);

        // JComboBox 초기화 및 데이터 추가
        seatChombo = new JComboBox<>();
        seatChombo.setBounds(300, 360, 200, 30);
        ct.add(seatChombo);

        loadAvailableSeats();

        time_plus_jpanel = new Time_Plus_Jpanel();
        time_plus_jpanel.setBounds(150, 420, 400, 50);
        ct.add(time_plus_jpanel);

        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //e드가자
                // id 와 passwd 가 일치할때 로그인
                // id를 사용해서 dto 객체를 가져와서 받은 pwd와 dto.pwd가 같은지 화깅ㄴ
                //로그인 성공시 좌석 상태 변경 update seat -> 이건 trigger 가 낫지 않냐? -> trigger 해도 할게 많네
                if (seatChombo.getSelectedItem() != null) {
                    Connection conn = PCPosDBConnection.getConnection();
                    MemberDAO memberDAO = new MemberDAO(conn);
                    MemberDTO member = memberDAO.findById(id.getText());
                    SeatDAO seatDAO = new SeatDAO(conn);
                    UsageHistoryDAO usageHistoryDAO = new UsageHistoryDAO(conn);
                    if (member != null) { // id를 통해 가져오는 정보가잇는지 확인
                        if (member.getMember_pwd().equals(passwd.getText())) { // id, pwd 일치한지 확인
                            if (member.getLeft_time() != 0) { // 남은 시간 없으면 안됨
                                int seatNo = (int) seatChombo.getSelectedItem();
                                seatDAO.updateSeat(member.getMember_no(), (int) seatChombo.getSelectedItem());
                                User_OrderProduct op = new User_OrderProduct(seatNo, member.getMember_no());
                                op.setVisible(true);
                                dispose();
                                // //로그인 창 닫기

                                //사용 시작 시간 저장
                                UsageHistoryDTO usageHistory = new UsageHistoryDTO();
                                usageHistory.setMember_no(member.getMember_no());
                                usageHistoryDAO.insert(usageHistory);


                            }
                            else JOptionPane.showMessageDialog(null, "시간을 충전해주세요 감사합니다.");
                        }else
                            JOptionPane.showMessageDialog(null, "틀렸습니다.");
                    } else
                        JOptionPane.showMessageDialog(null, "없는 ID 입니다.");

                    //좌석 선택 버튼 필요없음
                } else
                    JOptionPane.showMessageDialog(null, "좌석을 선택하세요");
            }
        });

        //회원가입버튼
        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //userFindFrame 에서 작업
                User_FindFrame userFindFrame = new User_FindFrame("비밀번호 찾기");
                userFindFrame.setVisible(true);
            }
        });

        //비밀번호 잦기 버튼
        findBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User_FindFrame userFindFrame = new User_FindFrame("비밀번호 찾기");
                userFindFrame.setVisible(true);
            }
        });
    }

    //현재 사용가능한 좌석 번호만 뜨게 함
    // state = " 미사용"
    private void loadAvailableSeats() {
        try (Connection conn = PCPosDBConnection.getConnection()) {
            SeatDAO seatDAO = new SeatDAO(conn);

            // 사용하지 않는 좌석 리스트 가져오기
            ArrayList<SeatDTO> seats = seatDAO.choose_not_using();

            for (SeatDTO s : seats) {
                seatChombo.addItem(s.getSeat_no()); // JComboBox에 좌석 번호 추가
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "좌석 데이터를 불러오는 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        User_LoginFrame userLoginFrame = new User_LoginFrame("명전 피시방 사용자 키오스크");
        userLoginFrame.setVisible(true);
    }
}
