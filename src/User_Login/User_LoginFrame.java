package User_Login;

import DAO.MemberDAO;
import DAO.SeatDAO;
import DTO.MemberDTO;
import DTO.SeatDTO;
import Jdbc.PCPosDBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class User_LoginFrame extends JFrame implements ActionListener {
    JTextField id;
    JPasswordField passwd;
    JButton loginBtn, registerBtn, findBtn, selectSeat;
    JComboBox<Integer> seatChombo; // 좌석 번호는 Integer 타입으로 선언

    public User_LoginFrame(String title) {
        setTitle(title);
        setResizable(false);
        setBounds(100, 100, 800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 창을 화면 중앙에 위치
        setLocationRelativeTo(null);

        Container ct = getContentPane();
        ct.setLayout(null);

        JLabel titleLabel = new JLabel("사용자 키오스크");
        titleLabel.setFont(new Font("나눔고딕", Font.BOLD, 30));
        titleLabel.setBounds(198, 28, 203, 79);
        ct.add(titleLabel);

        JLabel idLabel = new JLabel("아이디    : ");
        idLabel.setBounds(138, 134, 60, 15);
        ct.add(idLabel);

        id = new JTextField();
        id.setBounds(210, 131, 180, 21);
        ct.add(id);

        JLabel pwdLabel = new JLabel("비밀번호 : ");
        pwdLabel.setBounds(138, 178, 80, 15);
        ct.add(pwdLabel);

        passwd = new JPasswordField();
        passwd.setBounds(210, 175, 180, 21);
        ct.add(passwd);

        loginBtn = new JButton("로그인");
        loginBtn.setBounds(118, 265, 97, 23);
        loginBtn.addActionListener(this);
        ct.add(loginBtn);

        registerBtn = new JButton("회원가입");
        registerBtn.setBounds(364, 265, 97, 23);
        registerBtn.addActionListener(this);
        ct.add(registerBtn);

        findBtn = new JButton("비밀번호 찾기");
        findBtn.setBounds(210, 232, 165, 23);
        findBtn.addActionListener(this);
        ct.add(findBtn);

//        selectSeat = new JButton("좌석 선택");
//        selectSeat.setBounds(210, 298, 165, 23);
//        selectSeat.addActionListener(this);
//        ct.add(selectSeat);

        // JComboBox 초기화 및 데이터 추가
        seatChombo = new JComboBox<>();
        seatChombo.setBounds(210, 331, 165, 23);
        ct.add(seatChombo);

        loadAvailableSeats();
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

    @Override
    public void actionPerformed(ActionEvent ae) {
        String s = ae.getActionCommand();


        if (s.equals("회원가입")) {
            User_RegisterFrame userRegisterFrame = new User_RegisterFrame("회원가입");
            userRegisterFrame.setVisible(true);
        } else if (s.equals("비밀번호 찾기")) {
            //userFindFrame 에서 작업
            User_FindFrame userFindFrame = new User_FindFrame("비밀번호 찾기");
            userFindFrame.setVisible(true);
        } else if (s.equals("로그인")) {
            // 로그인 처리 (DB 연동 후 추가 예정)
            //e드가자
            // id 와 passwd 가 일치할때 로그인
            // id를 사용해서 dto 객체를 가져와서 받은 pwd와 dto.pwd가 같은지 화깅ㄴ
            //로그인 성공시 좌석 상태 변경 update seat -> 이건 trigger 가 낫지 않냐? -> trigger 해도 할게 많네
            if (seatChombo.getSelectedItem() != null) {
                Connection conn = PCPosDBConnection.getConnection();
                MemberDAO memberDAO = new MemberDAO(conn);
                MemberDTO member = memberDAO.findById(id.getText());
                SeatDAO seatDAO = new SeatDAO(conn);
                if (member != null) {
                    if (member.getMember_pwd().equals(passwd.getText())) {
                        seatDAO.updateSeat(member.getMember_no(), (int) seatChombo.getSelectedItem());

                        this.dispose(); // //로그인 창 닫기
                    } else
                        JOptionPane.showMessageDialog(this, "틀렸습니다.");
                } else
                    JOptionPane.showMessageDialog(this, "없는 ID 입니다.");

                //좌석 선택 버튼 필요없음
            }
            else
                    JOptionPane.showMessageDialog(this, "좌석을 선택하세요");
        }
//                else if (s.equals("좌석 선택")) {
//                Integer selectedSeat = (Integer) seatChombo.getSelectedItem();
//                if (selectedSeat != null) {
//                    JOptionPane.showMessageDialog(this, "선택된 좌석: " + selectedSeat, "좌석 선택", JOptionPane.INFORMATION_MESSAGE);
//                } else {
//                    JOptionPane.showMessageDialog(this, "좌석을 선택하세요.", "경고", JOptionPane.WARNING_MESSAGE);
//                }
//            }

    }

    public static void main(String[] args) {
        User_LoginFrame userLoginFrame = new User_LoginFrame("명전 피시방 사용자 키오스크");
        userLoginFrame.setVisible(true);
    }
}
