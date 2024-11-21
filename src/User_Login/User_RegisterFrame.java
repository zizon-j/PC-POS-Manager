package User_Login;

import DAO.MemberDAO;
import DTO.MemberDTO;
import Jdbc.PCPosDBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

class User_RegisterFrame extends JFrame implements ActionListener {
    JTextField idField, nameField, phoneField, addressField, birthdayField;
    JPasswordField passwordField;
    JComboBox<String> phoneComboBox, sexComboBox;
    JButton idCheckButton, confirmButton, cancelButton;
    String[] phoneCodes = { "010", "070", "02", "031", "032" };
    String[] sexCodes ={"남자", "여자"};

    public User_RegisterFrame(String title) {
        setTitle(title);
        setBounds(100, 100, 600, 400);
        // 창 누르면 회원가입 창만 꺼지게 수정
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //중앙배치
        setLocationRelativeTo(null);

        //레이아웃 설정
        Container ct = getContentPane();
        ct.setLayout(new BorderLayout(0, 0));

        //버튼 레이아웃
        JPanel bottomPanel = new JPanel();
        ct.add(bottomPanel, BorderLayout.SOUTH);

        //취소버튼
        cancelButton = new JButton("취소");
        cancelButton.addActionListener(this);
        bottomPanel.add(cancelButton);

        //확인버튼
        confirmButton = new JButton("확인");
        confirmButton.addActionListener(this);
        bottomPanel.add(confirmButton);

        //입력필드
        JPanel topPanel = new JPanel();
        ct.add(topPanel, BorderLayout.CENTER);
        topPanel.setLayout(new GridLayout(7, 1));

        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel idLabel = new JLabel("회원 ID  :  ");
        //id
        idField = new JTextField(8); 
        idCheckButton = new JButton("ID 중복 확인");
        idCheckButton.addActionListener(this);
        p1.add(idLabel);
        p1.add(idField);
        p1.add(idCheckButton);
        topPanel.add(p1);

        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel pwdLabel = new JLabel("비밀번호 :  ");
        // 비밀번호
        passwordField = new JPasswordField(8); 
        p2.add(pwdLabel);
        p2.add(passwordField);
        topPanel.add(p2);

        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel nameLabel = new JLabel("이름                :  ");
        //이름
        nameField = new JTextField(8);
        p3.add(nameLabel);
        p3.add(nameField);
        topPanel.add(p3);

        JPanel p4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel phoneLabel = new JLabel("전화번호            :  ");
        //전화번호
        phoneComboBox = new JComboBox<>(phoneCodes);
        phoneField = new JTextField(14);
        p4.add(phoneLabel);
        p4.add(phoneField);
        topPanel.add(p4);

        JPanel p5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel addressLabel = new JLabel("주소                :  ");
        //주소
        addressField = new JTextField(50);
        p5.add(addressLabel);
        p5.add(addressField);
        topPanel.add(p5);

        JPanel p6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel birthdayLabel = new JLabel("생년월일:   ");
        // 생일
        birthdayField = new JTextField(10);
        p6.add(birthdayLabel);
        p6.add(birthdayField);
        topPanel.add(p6);

        JPanel p7 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel sexLabel = new JLabel("성별:   ");
        // 성별
        sexComboBox = new JComboBox<>(sexCodes);
        p7.add(sexLabel);
        p7.add(sexComboBox);
        topPanel.add(p7);
    }

    public void actionPerformed(ActionEvent ae) {
        String s = ae.getActionCommand();

        if (s.equals("취소")) {
            dispose();
        } else if (s.equals("확인")) {
            // 입력 데이트를 MemberDTO로 변환
            MemberDTO member = new MemberDTO();
            member.setMember_id(idField.getText().trim()); // trim -> 공백 없앰
            member.setMember_pwd(passwordField.getText().trim());
            member.setMember_name(nameField.getText().trim());
            member.setPhone(phoneField.getText().trim());
            member.setAddress(addressField.getText().trim());

            //String -> java.sql.Date 변환
            java.sql.Date d = java.sql.Date.valueOf(birthdayField.getText());
            member.setBirthday(d);
            member.setSex(sexComboBox.getSelectedItem().toString());

            Connection conn = PCPosDBConnection.getConnection();
            if(conn!= null){
                MemberDAO memberDAO = new MemberDAO(conn);
                boolean success = memberDAO.insert(member);
                if(success){
                    JOptionPane.showMessageDialog(this, "회원가입 성공");
                    dispose();
                }else {
                    JOptionPane.showMessageDialog(this, "db연결 시패");
                }
            }

        } else if (s.equals("ID 확인")) {
            //미구현
            String enteredId = idField.getText().trim();

        }
    }
}
