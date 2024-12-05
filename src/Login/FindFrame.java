package Login;

import DAO.EmployeeDAO;
import DTO.EmployeeDTO;
import Jdbc.PCPosDBConnection;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;

class FindFrame extends JFrame implements ActionListener {
    // 입력 필드
    private JTextField posIdBox; // POS ID 입력 필드
    private JTextField idBox; // 아이디 입력 필드
    private JTextField nameBox; // 이름 입력 필드
    private JTextField phoneBox; // 전화번호 입력 필드

    // 전화번호 앞자리 콤보박스
    private JComboBox<String> phoneFirst;

    // 버튼들
    private JButton okBtn, cancelBtn, checkPosBtn;

    // 전화번호 앞자리 옵션
    private String[] phoneNumbers = { "010", "070", "02", "031", "032" };

    // 라디오 버튼
    private JRadioButton adminBtn, empBtn;

    // POS ID 패널
    private JPanel posPanel;

    // POS ID 확인 여부
    private boolean isPosIdOk = false;

    public FindFrame(String title) {
        // 기본 창 설정
        setTitle(title);
        setSize(350, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // 메인 컨테이너
        Container main = getContentPane();
        main.setLayout(new BorderLayout());

        // 하단 버튼 패널
        JPanel bottom = new JPanel();
        main.add(bottom, BorderLayout.SOUTH);

        // 취소 버튼
        cancelBtn = new JButton("취소");
        cancelBtn.addActionListener(this);
        bottom.add(cancelBtn);

        // 확인 버튼
        okBtn = new JButton("확인");
        okBtn.addActionListener(this);
        bottom.add(okBtn);

        // 입력 필드들 패널
        JPanel inputPanel = new JPanel();
        main.add(inputPanel, BorderLayout.CENTER);
        inputPanel.setLayout(new GridLayout(5, 1));

        // 사용자 유형 선택 (관리자/직원)
        JPanel userType = new JPanel(new FlowLayout(FlowLayout.LEFT));
        adminBtn = new JRadioButton("관리자");
        empBtn = new JRadioButton("직원", true);
        ButtonGroup group = new ButtonGroup();
        group.add(adminBtn);
        group.add(empBtn);
        userType.add(adminBtn);
        userType.add(empBtn);
        inputPanel.add(userType);

        // POS ID 입력 패널
        posPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        posPanel.add(new JLabel("POS ID     :  "));
        posIdBox = new JTextField(8); // POS ID 입력 필드
        posPanel.add(posIdBox);

        // POS ID 확인 버튼
        checkPosBtn = new JButton("ID 확인");
        checkPosBtn.addActionListener(this);
        posPanel.add(checkPosBtn);

        // POS ID 패널을 입력 필드 패널에 추가
        inputPanel.add(posPanel);

        // 아이디 입력창
        makeInputRow(inputPanel, "아이디        :  ", idBox = new JTextField(8));

        // 이름 입력창
        makeInputRow(inputPanel, "이름           :  ", nameBox = new JTextField(8));

        // 전화번호 입력창
        JPanel phonePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        phonePanel.add(new JLabel("전화번호    :  "));
        phoneFirst = new JComboBox<>(phoneNumbers); // 전화번호 앞자리 선택
        phoneBox = new JTextField(10); // 전화번호 입력 필드
        phonePanel.add(phoneFirst);
        phonePanel.add(phoneBox);
        inputPanel.add(phonePanel);

        // 라디오 버튼 동작 설정
        adminBtn.addActionListener(e -> showHidePosId());
        empBtn.addActionListener(e -> showHidePosId());
        showHidePosId(); // 초기 상태 설정
    }

    // 입력 필드와 라벨을 포함한 행을 생성
    private void makeInputRow(JPanel parent, String label, JTextField field) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel(label));
        row.add(field);
        parent.add(row);
    }

    // POS ID 입력창 표시/숨기기
    private void showHidePosId() {
        posPanel.setVisible(adminBtn.isSelected()); // 관리자일 때만 보이도록 설정
        revalidate();
        repaint();
    }

    // 버튼 클릭 시 동작
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd.equals("취소")) {
            dispose(); // 창 닫기
        } else if (cmd.equals("ID 확인")) {
            checkPosId(); // POS ID 확인
        } else if (cmd.equals("확인")) {
            checkAndFind(); // 입력 정보 확인 및 처리
        }
    }

    // POS ID 확인 로직
    private void checkPosId() {
        String posId = posIdBox.getText().trim(); // 입력된 POS ID 가져오기
        if (posId.isEmpty()) {
            showError("ID를 입력해주세요."); // 입력되지 않은 경우 메시지 표시
        } else if (posId.equals("2020081049")) { // 예제: 유효한 POS ID 확인
            isPosIdOk = true;
            showMessage("ID 확인", "알맞은 ID입니다."); // 성공 메시지 표시
        } else {
            isPosIdOk = false;
            showError("ID가 맞지 않습니다."); // 실패 메시지 표시
        }
    }

    // 입력 정보 확인 및 처리
    private void checkAndFind() {
        String id = idBox.getText().trim();
        String name = nameBox.getText().trim();
        String phone = phoneBox.getText().trim();

        // 관리자 선택 시 POS ID 확인 필수
        if (adminBtn.isSelected()) {
            if (!isPosIdOk) {
                showError("POS ID 확인이 필요합니다.");
                return;
            }
        }

        // 필수 입력 필드 확인
        if (id.isEmpty() || name.isEmpty() || phone.isEmpty()) {
            showError("모든 항목을 입력해주세요.");
            return;
        }

        // 전화번호 형식 확인
        if (phone.length() != 8) {
            showError("전화번호를 8자리로 입력해주세요.\n예: 12345678");
            return;
        }

        // 전화번호 형식 완성
        String fullPhone = phoneFirst.getSelectedItem() + "-" +
                phone.substring(0, 4) + "-" +
                phone.substring(4);

        // 비밀번호 찾기 로직 호출
        findPassword(id, name, fullPhone);
    }

    // 비밀번호 찾기
    private void findPassword(String id, String name, String phone) {
        Connection db = PCPosDBConnection.getConnection();
        if (db == null) {
            showError("DB 연결 실패!");
            return;
        }

        try {
            EmployeeDAO dao = new EmployeeDAO(db);
            EmployeeDTO user = dao.findById(id);

            // 회원 정보 확인
            if (user == null) {
                showError("일치하는 회원이 없습니다.");
                return;
            }

            // 관리자 정보 접근 제한
            if (empBtn.isSelected() && user.getPriority().equals("관리자")) {
                showError("관리자 정보를 볼 수 없습니다!");
                return;
            }

            // 정보 일치 확인
            if (user.getEmp_name().equals(name) && user.getEmp_phone().equals(phone)) {
                showMessage("비밀번호 찾기 성공",
                        user.getEmp_name() + "님의 비밀번호는 " + user.getEmp_pwd() + " 입니다.");
                dispose(); // 성공 시 창 닫기
            } else {
                showError("입력한 정보가 일치하지 않습니다.");
            }
        } finally {
            try {
                db.close(); // DB 연결 종료
            } catch (Exception e) {
            }
        }
    }

    // 오류 메시지 표시
    private void showError(String msg) {
        showMessage("오류", msg);
    }

    // 메시지 표시
    private void showMessage(String title, String msg) {
        Login_MessageDialog dialog = new Login_MessageDialog(this, title, true, msg);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
