package Login;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class RegisterFrame extends JFrame implements ActionListener {
    JTextField idField, nameField, phoneField, addressField;
    JPasswordField passwordField;
    JComboBox<String> phoneComboBox;
    JButton idCheckButton, confirmButton, cancelButton;
    String[] phoneCodes = { "010", "070", "02", "031", "032" };

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
        topPanel.setLayout(new GridLayout(5, 1));

        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel idLabel = new JLabel("POS 기기 ID  :  ");
        //id
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
        //이름
        nameField = new JTextField(8);
        p3.add(nameLabel);
        p3.add(nameField);
        topPanel.add(p3);

        JPanel p4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel phoneLabel = new JLabel("연락처            :  ");
        //전화번호
        phoneComboBox = new JComboBox<>(phoneCodes);
        phoneField = new JTextField(10);
        p4.add(phoneLabel);
        p4.add(phoneComboBox);
        p4.add(phoneField);
        topPanel.add(p4);

        JPanel p5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel addressLabel = new JLabel("주소                :  ");
        //주소
        addressField = new JTextField(20);
        p5.add(addressLabel);
        p5.add(addressField);
        topPanel.add(p5);
    }

    public void actionPerformed(ActionEvent ae) {
        String s = ae.getActionCommand();

        if (s.equals("취소")) {
            dispose();
        } else if (s.equals("확인")) {
            // Handle confirmation (DB interaction)
            dispose();
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

