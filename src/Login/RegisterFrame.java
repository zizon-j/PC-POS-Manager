package Login;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import DAO.MemberDAO;
import DTO.MemberDTO;
import Jdbc.PCPosDBConnection;
import java.sql.Connection;

class RegisterFrame extends JFrame implements ActionListener {
    JTextField idField, nameField, phoneField, addressField, birthdayField;
    JPasswordField passwordField;
    JComboBox<String> phoneComboBox, sexComboBox;
    JButton idCheckButton, confirmButton, cancelButton;
    String[] phoneCodes = { "010", "070", "02", "031", "032" };
    String[] sexCodes = { "남자", "여자" };

    public RegisterFrame(String title) {
        setTitle(title);
        setBounds(100, 100, 350, 300);
        // 창 누르면 회원가입 창만 꺼지게 수정
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

        Container ct = getContentPane();
        ct.setLayout(new BorderLayout(0, 0));

        JPanel bottomPanel = new JPanel();
        ct.add(bottomPanel, BorderLayout.SOUTH);

        cancelButton = new JButton("취소");
        cancelButton.addActionListener(this);
        bottomPanel.add(cancelButton);

        confirmButton = new JButton("확인");
        confirmButton.addActionListener(this);
        bottomPanel.add(confirmButton);

        JPanel topPanel = new JPanel();
        ct.add(topPanel, BorderLayout.CENTER);
        topPanel.setLayout(new GridLayout(7, 1));

        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel idLabel = new JLabel("POS 기기 ID  :  ");
        // id
        idField = new JTextField(8);
        idCheckButton = new JButton("ID 확인");
        idCheckButton.addActionListener(this);
        p1.add(idLabel);
        p1.add(idField);
        p1.add(idCheckButton);
        topPanel.add(p1);

        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel pwdLabel = new JLabel("PASSWORD :  ");
        // 비밀번호
        passwordField = new JPasswordField(8);
        p2.add(pwdLabel);
        p2.add(passwordField);
        topPanel.add(p2);

        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel nameLabel = new JLabel("이름                :  ");
        // 이름
        nameField = new JTextField(8);
        p3.add(nameLabel);
        p3.add(nameField);
        topPanel.add(p3);

        JPanel p4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel phoneLabel = new JLabel("연락처            :  ");
        // 전화번호
        phoneComboBox = new JComboBox<>(phoneCodes);
        phoneField = new JTextField(10);
        p4.add(phoneLabel);
        p4.add(phoneComboBox);
        p4.add(phoneField);
        topPanel.add(p4);

        JPanel p5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel addressLabel = new JLabel("주소                :  ");
        // 주소
        addressField = new JTextField(20);
        p5.add(addressLabel);
        p5.add(addressField);
        topPanel.add(p5);

        JPanel p6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel birthdayLabel = new JLabel("생년월일        :  ");
        birthdayField = new JTextField(10);
        p6.add(birthdayLabel);
        p6.add(birthdayField);
        topPanel.add(p6);

        JPanel p7 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel sexLabel = new JLabel("성별                :  ");
        sexComboBox = new JComboBox<>(sexCodes);
        p7.add(sexLabel);
        p7.add(sexComboBox);
        topPanel.add(p7);
    }

    public void actionPerformed(ActionEvent ae) { // member 아니고 employee 에 넣어야함
        String s = ae.getActionCommand();

        if (s.equals("취소")) {
            dispose();
        } else if (s.equals("확인")) {
            // 입력값 유효성 검사
            if (idField.getText().trim().isEmpty() ||
                    passwordField.getText().trim().isEmpty() ||
                    nameField.getText().trim().isEmpty() ||
                    phoneField.getText().trim().isEmpty() ||
                    addressField.getText().trim().isEmpty() ||
                    birthdayField.getText().trim().isEmpty()) {

                Login_MessageDialog md = new Login_MessageDialog(this, "오류", true, "모든 항목을 입력해주세요.");
                md.setLocationRelativeTo(this);
                md.setVisible(true);
                return;
            }

            // MemberDTO 객체 생성 및 데이터 설정
            MemberDTO member = new MemberDTO();
            member.setMember_id(idField.getText().trim());
            member.setMember_pwd(passwordField.getText().trim());
            member.setMember_name(nameField.getText().trim());
            member.setPhone(phoneComboBox.getSelectedItem().toString() + phoneField.getText().trim());
            member.setAddress(addressField.getText().trim());

            try {
                java.sql.Date birthday = java.sql.Date.valueOf(birthdayField.getText().trim());
                member.setBirthday(birthday);
                member.setSex(sexComboBox.getSelectedItem().toString());
            } catch (IllegalArgumentException e) {
                Login_MessageDialog md = new Login_MessageDialog(this, "오류", true, "생년월일 형식이 올바르지 않습니다. (YYYY-MM-DD)");
                md.setLocationRelativeTo(this);
                md.setVisible(true);
                return;
            }

            // DB 연결 및 데이터 저장
            Connection conn = PCPosDBConnection.getConnection();
            if (conn != null) {
                MemberDAO memberDAO = new MemberDAO(conn);
                boolean success = memberDAO.insert(member);
                if (success) {
                    Login_MessageDialog md = new Login_MessageDialog(this, "성공", true, "회원가입이 완료되었습니다.");
                    md.setLocationRelativeTo(this);
                    md.setVisible(true);
                    dispose();
                } else {
                    Login_MessageDialog md = new Login_MessageDialog(this, "오류", true, "회원가입에 실패했습니다.");
                    md.setLocationRelativeTo(this);
                    md.setVisible(true);
                }
            } else {
                Login_MessageDialog md = new Login_MessageDialog(this, "오류", true, "데이터베이스 연결에 실패했습니다.");
                md.setLocationRelativeTo(this);
                md.setVisible(true);
            }
        } else if (s.equals("ID 확인")) {
            String enteredId = idField.getText().trim();

            if (enteredId.isEmpty()) {
                Login_MessageDialog md = new Login_MessageDialog(this, "ID 확인", true, "ID를 입력해주세요.");
                md.setLocationRelativeTo(this);
                md.setVisible(true);
            } else if (enteredId.equals("2020081049")) { // Temporary ID for checking
                Login_MessageDialog md = new Login_MessageDialog(this, "ID 확인", true, "알맞은 ID입니다.");
                md.setLocationRelativeTo(this);
                md.setVisible(true);
            } else {
                Login_MessageDialog md = new Login_MessageDialog(this, "ID 확인", true, "ID가 맞지 않습니다.");
                md.setLocationRelativeTo(this);
                md.setVisible(true);
            }
        }
    }
}
