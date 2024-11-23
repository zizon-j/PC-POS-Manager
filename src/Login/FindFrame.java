package Login;

import DAO.MemberDAO;
import DTO.MemberDTO;
import Jdbc.PCPosDBConnection;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;

class FindFrame extends JFrame implements ActionListener {
    // UI Components
    private JTextField posIdField;
    private JTextField idField;
    private JTextField nameField;
    private JTextField phoneField;
    private JComboBox<String> phoneComboBox;
    private JButton confirmButton;
    private JButton cancelButton;

    // 전화 코드 배열
    private String[] phoneCodes = { "010", "070", "02", "031", "032" };

    public FindFrame(String title) {
        setTitle(title);
        setResizable(false);
        setBounds(100, 100, 350, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Container ct = getContentPane();
        ct.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        ct.add(mainPanel, BorderLayout.CENTER);

        // POS ID 레이블 및 필드
        JLabel posIdLabel = new JLabel("POS ID     :");
        posIdLabel.setBounds(30, 30, 70, 15);
        mainPanel.add(posIdLabel);

        posIdField = new JTextField();
        posIdField.setBounds(110, 30, 150, 21);
        mainPanel.add(posIdField);

        // ID 레이블 및 필드
        JLabel idLabel = new JLabel("아이디     :");
        idLabel.setBounds(30, 60, 70, 15);
        mainPanel.add(idLabel);

        idField = new JTextField();
        idField.setBounds(110, 60, 150, 21);
        mainPanel.add(idField);

        // 이름 레이블 및 필드
        JLabel nameLabel = new JLabel("이름         : ");
        nameLabel.setBounds(30, 90, 70, 15);
        mainPanel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(110, 90, 150, 21);
        mainPanel.add(nameField);

        // 전화번호 레이블 및 필드
        JLabel phoneLabel = new JLabel("전화번호 : ");
        phoneLabel.setBounds(30, 120, 70, 15);
        mainPanel.add(phoneLabel);

        phoneComboBox = new JComboBox<>(phoneCodes);
        phoneComboBox.setBounds(110, 120, 50, 21);
        mainPanel.add(phoneComboBox);

        phoneField = new JTextField();
        phoneField.setBounds(170, 120, 90, 21);
        mainPanel.add(phoneField);

        // 버튼 패널 생성
        JPanel bottomPanel = new JPanel();
        ct.add(bottomPanel, BorderLayout.SOUTH);

        // 취소 버튼
        cancelButton = new JButton("취소");
        cancelButton.addActionListener(this);
        bottomPanel.add(cancelButton);

        // 확인 버튼
        confirmButton = new JButton("확인");
        confirmButton.addActionListener(this);
        bottomPanel.add(confirmButton);
    }

    // 버튼 클릭 이벤트 처리
    public void actionPerformed(ActionEvent ae) {
        String s = ae.getActionCommand(); // 클릭된 버튼의 명령어 가져오기

        if (s.equals("취소")) {
            dispose(); // 프레임 닫기
        } else if (s.equals("확인")) {
            findPassword(); // 비밀번호 찾기 메서드 호출
        }
    }

    // 비밀번호 찾기 로직
    private void findPassword() {
        String posId = posIdField.getText().trim();
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String phoneNumber = phoneField.getText().trim();

        // POS ID 검증
        if (!posId.equals("2020081049")) {
            showMessage("오류", "포스기 ID가 맞지 않습니다.");
            return;
        }

        // 입력값 검증
        if (id.isEmpty() || name.isEmpty() || phoneNumber.isEmpty()) {
            showMessage("오류", "모든 항목을 입력해주세요.");
            return;
        }

        // 전화번호가 숫자 8자리인지 확인 (예: 12341234)
        if (!phoneNumber.matches("\\d{8}")) {
            showMessage("오류", "전화번호를 8자리 숫자로 입력해주세요.\n예) 12341234");
            return;
        }

        // 전화번호 형식 만들기 (예: 010-1234-1234)
        String formattedPhone = String.format("%s-%s-%s",
                phoneComboBox.getSelectedItem().toString(),
                phoneNumber.substring(0, 4),
                phoneNumber.substring(4));

        // 데이터베이스 연결 및 검증 로직
        Connection conn = PCPosDBConnection.getConnection(); // DB 연결
        if (conn == null) {
            showMessage("오류", "데이터베이스 연결에 실패했습니다.");
            return;
        }

        try {
            MemberDAO dao = new MemberDAO(conn); // DAO 객체 생성
            MemberDTO member = dao.findById(id); // ID로 회원 정보 조회

            if (member != null) {
                // 디버깅용 출력
                System.out.println("DB 저장값: " + member.getPhone());
                System.out.println("입력값: " + formattedPhone);
                System.out.println("DB 이름: " + member.getMember_name());
                System.out.println("입력 이름: " + name);

                // 이름과 전화번호 비교
                if (member.getMember_name().equals(name) &&
                        member.getPhone().equals(formattedPhone)) {
                    String message = String.format("%s님의 비밀번호는 %s 입니다.",
                            member.getMember_name(),
                            member.getMember_pwd());
                    showMessage("비밀번호 찾기 성공", message);
                    dispose();
                } else {
                    showMessage("비밀번호 찾기 실패", "입력하신 정보와 일치하는 회원이 없습니다.");
                }
            } else {
                showMessage("비밀번호 찾기 실패", "입력하신 ID가 존재하지 않습니다.");
            }
        } finally {
            try {
                conn.close(); // DB 연결 종료
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 메시지 다이얼로그 표시
    private void showMessage(String title, String message) {
        Login_MessageDialog dialog = new Login_MessageDialog(this, title, true, message);
        dialog.setLocationRelativeTo(this); // 다이얼로그 위치 설정
        dialog.setVisible(true); // 다이얼로그 표시
    }
}
