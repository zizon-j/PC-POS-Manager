package Login;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

import DAO.EmployeeDAO;
import DTO.EmployeeDTO;
import Jdbc.PCPosDBConnection;
import java.sql.Connection;

class RegisterFrame extends JFrame implements ActionListener {
    // 입력창들
    JTextField posIdInput, idInput, nameInput, phoneInput;
    JPasswordField pwInput;

    // 버튼들
    JButton checkPosBtn, checkIdBtn, okBtn, cancelBtn;

    // 전화번호 앞자리
    String[] phoneStart = { "010", "070", "02", "031", "032" };

    // 체크 여부
    boolean isPosIdOk = false;
    boolean isIdOk = false;

    // 라디오 버튼
    JRadioButton adminBtn, staffBtn;

    // POS ID 입력 패널
    JPanel posIdPanel;

    // 전화번호 선택
    JComboBox<String> phoneSelect;

    public RegisterFrame(String title) {
        // 기본 창 설정
        setTitle(title);
        setBounds(100, 100, 350, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // 메인 화면
        Container mainScreen = getContentPane();
        mainScreen.setLayout(new BorderLayout(0, 0));

        // 하단 버튼 패널
        JPanel bottomPanel = new JPanel();
        mainScreen.add(bottomPanel, BorderLayout.SOUTH);

        // 취소, 확인 버튼
        cancelBtn = new JButton("취소");
        cancelBtn.addActionListener(this);
        bottomPanel.add(cancelBtn);

        okBtn = new JButton("확인");
        okBtn.addActionListener(this);
        bottomPanel.add(okBtn);

        // 입력 필드들 패널
        JPanel inputPanel = new JPanel();
        mainScreen.add(inputPanel, BorderLayout.CENTER);
        inputPanel.setLayout(new GridLayout(6, 1));

        // 관리자/직원 선택
        JPanel userType = new JPanel(new FlowLayout(FlowLayout.LEFT));
        adminBtn = new JRadioButton("관리자");
        staffBtn = new JRadioButton("직원", true);
        ButtonGroup group = new ButtonGroup();
        group.add(adminBtn);
        group.add(staffBtn);
        userType.add(adminBtn);
        userType.add(staffBtn);
        inputPanel.add(userType);

        // POS ID 입력창
        posIdPanel = makeInputPanel("POS 기기 ID  :  ", posIdInput = new JTextField(8));
        checkPosBtn = new JButton("ID 확인");
        checkPosBtn.addActionListener(this);
        posIdPanel.add(checkPosBtn);
        inputPanel.add(posIdPanel);

        // 아이디 입력창
        JPanel idPanel = makeInputPanel("아이디            :  ", idInput = new JTextField(8));
        checkIdBtn = new JButton("중복 확인");
        checkIdBtn.addActionListener(this);
        idPanel.add(checkIdBtn);
        inputPanel.add(idPanel);

        // 비밀번호 입력창
        inputPanel.add(makeInputPanel("비밀번호        :  ", pwInput = new JPasswordField(8)));

        // 이름 입력창
        inputPanel.add(makeInputPanel("이름                :  ", nameInput = new JTextField(8)));

        // 전화번호 입력창
        JPanel phonePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        phonePanel.add(new JLabel("연락처            :  "));
        phoneSelect = new JComboBox<>(phoneStart);
        phoneInput = new JTextField(10);
        phonePanel.add(phoneSelect);
        phonePanel.add(phoneInput);
        inputPanel.add(phonePanel);

        // 라디오 버튼 동작
        adminBtn.addActionListener(e -> showHidePosId());
        staffBtn.addActionListener(e -> showHidePosId());
        showHidePosId();
    }

    // 입력창 만들기
    private JPanel makeInputPanel(String text, JTextField input) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel(text));
        panel.add(input);
        return panel;
    }

    // POS ID창 보이기/숨기기
    private void showHidePosId() {
        posIdPanel.setVisible(adminBtn.isSelected());
        revalidate();
        repaint();
    }

    // 버튼 클릭시 동작
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd.equals("취소")) {
            dispose();
        } else if (cmd.equals("ID 확인")) {
            checkPosId();
        } else if (cmd.equals("중복 확인")) {
            checkDuplicateId();
        } else if (cmd.equals("확인")) {
            saveEmployee();
        }
    }

    // POS ID 확인
    private void checkPosId() {
        String posId = posIdInput.getText().trim();
        if (posId.isEmpty()) {
            showMsg("ID 확인", "ID를 입력해주세요.");
        } else if (posId.equals("2020081049")) {
            isPosIdOk = true;
            showMsg("ID 확인", "알맞은 ID입니다.");
        } else {
            isPosIdOk = false;
            showMsg("ID 확인", "ID가 맞지 않습니다.");
        }
    }

    // ID 중복 확인
    private void checkDuplicateId() {
        String id = idInput.getText().trim();
        if (id.isEmpty()) {
            showMsg("중복 확인", "ID를 입력해주세요.");
            return;
        }

        Connection db = PCPosDBConnection.getConnection();
        if (db != null) {
            try {
                EmployeeDAO dao = new EmployeeDAO(db);
                if (dao.findById(id) == null) {
                    isIdOk = true;
                    showMsg("중복 확인", "사용 가능한 ID입니다.");
                } else {
                    isIdOk = false;
                    showMsg("중복 확인", "이미 사용 중인 ID입니다.");
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ex) {
                }
            }
        }
    }

    // 직원 정보 저장
    private void saveEmployee() {
        // 입력 확인
        if (adminBtn.isSelected() && !isPosIdOk) {
            showMsg("오류", "POS 기기 ID 확인이 필요합니다.");
            return;
        }

        if (!isIdOk) {
            showMsg("오류", "ID 중복 확인이 필요합니다.");
            return;
        }

        // 필수 입력 확인
        if (idInput.getText().trim().isEmpty() ||
                pwInput.getText().trim().isEmpty() ||
                nameInput.getText().trim().isEmpty() ||
                phoneInput.getText().trim().isEmpty()) {
            showMsg("오류", "모든 항목을 입력해주세요.");
            return;
        }

        // 전화번호 형식 확인
        String phone = phoneInput.getText().trim().replaceAll("[^0-9]", "");
        if (phone.length() != 8) {
            showMsg("오류", "전화번호를 8자리로 입력해주세요.");
            return;
        }

        // 직원 정보 생성
        EmployeeDTO emp = new EmployeeDTO();
        emp.setEmp_id(idInput.getText().trim());
        emp.setEmp_pwd(pwInput.getText().trim());
        emp.setEmp_name(nameInput.getText().trim());
        emp.setEmp_phone(String.format("%s-%s-%s",
                phoneSelect.getSelectedItem(),
                phone.substring(0, 4),
                phone.substring(4)));
        emp.setPriority(adminBtn.isSelected() ? "관리자" : "직원");

        // DB에 저장
        Connection db = PCPosDBConnection.getConnection();
        if (db != null) {
            try {
                if (new EmployeeDAO(db).insert(emp)) {
                    showMsg("성공", "회원가입이 완료되었습니다.");
                    dispose();
                } else {
                    showMsg("오류", "회원가입에 실패했습니다.");
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ex) {
                }
            }
        } else {
            showMsg("오류", "DB 연결 실패!");
        }
    }

    // 메시지 표시
    private void showMsg(String title, String msg) {
        Login_MessageDialog dialog = new Login_MessageDialog(this, title, true, msg);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
