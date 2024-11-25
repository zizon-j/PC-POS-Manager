package Login;

import main.Main;
import DAO.EmployeeDAO;
import DTO.EmployeeDTO;
import Jdbc.PCPosDBConnection;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;

public class LoginFrame extends JFrame implements ActionListener {

    JTextField loginId;
    JPasswordField loginPw;
    JButton btnLogin, btnClear, btnJoin, btnFindPw;
    JRadioButton adminRadio, employeeRadio;
    ButtonGroup roleGroup;

    public LoginFrame(String title) {
        setTitle(title);
        setResizable(false);
        setBounds(100, 100, 600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        makeScreen();
    }

    // UI 구성 메서드
    private void makeScreen() {
        Container container = getContentPane();
        container.setLayout(null);

        // 제목 레이블 생성
        JLabel lblTitle = new JLabel("명전 PC방 POS");
        lblTitle.setFont(new Font("나눔고딕", Font.BOLD, 30));
        lblTitle.setBounds(185, 28, 310, 79);
        container.add(lblTitle);

        // 역할 선택 라디오 버튼
        roleGroup = new ButtonGroup();
        adminRadio = new JRadioButton("관리자");
        employeeRadio = new JRadioButton("직원");
        employeeRadio.setSelected(true); // 기본값으로 직원 선택

        adminRadio.setBounds(138, 100, 80, 20);
        employeeRadio.setBounds(238, 100, 80, 20);

        roleGroup.add(adminRadio);
        roleGroup.add(employeeRadio);

        container.add(adminRadio);
        container.add(employeeRadio);

        // 아이디 레이블 및 입력 필드 생성
        JLabel lblId = new JLabel("아이디    : ");
        lblId.setBounds(138, 134, 60, 15);
        container.add(lblId);

        loginId = new JTextField();
        loginId.setBounds(210, 131, 180, 21);
        container.add(loginId);

        // 비밀번호 레이블 및 입력 필드 생성
        JLabel lblPw = new JLabel("비밀번호 : ");
        lblPw.setBounds(138, 178, 80, 15);
        container.add(lblPw);

        loginPw = new JPasswordField();
        loginPw.setBounds(210, 175, 180, 21);
        container.add(loginPw);

        // 로그인 버튼 생성
        btnLogin = new JButton("로그인");
        btnLogin.setBounds(118, 265, 97, 23);
        btnLogin.addActionListener(this);
        container.add(btnLogin);

        // 취소 버튼 생성
        btnClear = new JButton("취소");
        btnClear.setBounds(241, 265, 97, 23);
        btnClear.addActionListener(this);
        container.add(btnClear);

        // 회원가입 버튼 생성
        btnJoin = new JButton("회원가입");
        btnJoin.setBounds(364, 265, 97, 23);
        btnJoin.addActionListener(this);
        container.add(btnJoin);

        // 비밀번호 찾기 버튼 생성
        btnFindPw = new JButton("비밀번호 찾기");
        btnFindPw.setBounds(210, 232, 165, 23);
        btnFindPw.addActionListener(this);
        container.add(btnFindPw);
    }

    // 버튼 클릭 시 호출되는 메서드
    public void actionPerformed(ActionEvent e) {
        String clickBtn = e.getActionCommand();

        // 클릭된 버튼에 따라 다른 동작 수행
        if (clickBtn.equals("취소")) {
            clearInputs();
        } else if (clickBtn.equals("회원가입")) {
            showRegisterFrame();
        } else if (clickBtn.equals("비밀번호 찾기")) {
            showFindPwFrame();
        } else if (clickBtn.equals("로그인")) {
            tryLogin(); // 로그인 시도
        }
    }

    // 입력 필드 초기화 메서드
    private void clearInputs() {
        loginId.setText(""); // 아이디 필드 비우기
        loginPw.setText(""); // 비밀번호 필드 비우기
    }

    // 회원가입 화면 표시 메서드
    private void showRegisterFrame() {
        RegisterFrame frame = new RegisterFrame("회원가입");
        frame.setVisible(true);
    }

    // 비밀번호 찾기 화면 표시 메서드
    private void showFindPwFrame() {
        FindFrame frame = new FindFrame("비밀번호 찾기");
        frame.setVisible(true);
    }

    // 로그인 시도 메서드
    private void tryLogin() {
        String id = loginId.getText().trim();
        String pw = new String(loginPw.getPassword()).trim();
        boolean isAdmin = adminRadio.isSelected();

        if (id.isEmpty() || pw.isEmpty()) {
            showMessage("로그인 오류", "아이디와 비밀번호를 입력해주세요.");
            return;
        }

        // 데이터베이스 연결 시도
        Connection conn = PCPosDBConnection.getConnection();
        if (conn == null) {
            showMessage("연결 오류", "데이터베이스 연결에 실패했습니다.");
            return;
        }

        try {
            EmployeeDAO dao = new EmployeeDAO(conn);
            EmployeeDTO user = dao.findById(id);

            if (user != null && user.getEmp_pwd().equals(pw)) {
                // 관리자 권한 체크 수정
                if (isAdmin) {
                    if ("관리자".equals(user.getPriority())) {
                        showMessage("로그인 성공", "관리자님 환영합니다!");
                        new Main();
                        dispose();
                    } else {
                        showMessage("로그인 실패", "관리자 권한이 없습니다.");
                    }
                } else {
                    // 직원 로그인
                    if ("직원".equals(user.getPriority())) {
                        showMessage("로그인 성공", "직원님 환영합니다!");
                        new Main();
                        dispose();
                    } else {
                        showMessage("로그인 실패", "직원 계정이 아닙니다.");
                    }
                }
            } else {
                showMessage("로그인 실패", "아이디 또는 비밀번호가 일치하지 않습니다.");
            }
        } finally {
            try {
                conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void showMessage(String title, String message) {
        Login_MessageDialog dialog = new Login_MessageDialog(this, title, true, message);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        LoginFrame frame = new LoginFrame("명전 PC방 POS - 로그인 화면");
        frame.setVisible(true);
    }
}
