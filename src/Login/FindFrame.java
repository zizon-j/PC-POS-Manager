package Login;

import UI.MessageDialog;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class FindFrame extends JFrame implements ActionListener {
    JTextField posIdField, nameField, phoneField;
    JComboBox<String> phoneComboBox;
    JButton confirmButton, cancelButton;
    String[] phoneCodes = { "010", "070", "02", "031", "032" };

    public FindFrame(String title) {
        setTitle(title);
        setResizable(false);
        setBounds(100, 100, 350, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        Container ct = getContentPane();
        ct.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        ct.add(mainPanel, BorderLayout.CENTER);

        JLabel idLabel = new JLabel("POS ID    :");
        idLabel.setBounds(31, 34, 70, 15);
        mainPanel.add(idLabel);

        posIdField = new JTextField();
        posIdField.setBounds(113, 31, 116, 21);
        mainPanel.add(posIdField);

        JLabel nameLabel = new JLabel("이름         : ");
        nameLabel.setBounds(31, 85, 70, 15);
        mainPanel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(113, 82, 116, 21);
        mainPanel.add(nameField);

        JLabel phoneLabel = new JLabel("전화번호 : ");
        phoneLabel.setBounds(31, 136, 70, 15);
        mainPanel.add(phoneLabel);

        phoneComboBox = new JComboBox<>(phoneCodes);
        phoneComboBox.setBounds(113, 130, 50, 21);
        mainPanel.add(phoneComboBox);

        phoneField = new JTextField();
        phoneField.setBounds(170, 130, 116, 21);
        mainPanel.add(phoneField);

        JPanel bottomPanel = new JPanel();
        ct.add(bottomPanel, BorderLayout.SOUTH);

        cancelButton = new JButton("취소");
        cancelButton.addActionListener(this);
        bottomPanel.add(cancelButton);

        confirmButton = new JButton("확인");
        confirmButton.addActionListener(this);
        bottomPanel.add(confirmButton);
    }

    public void actionPerformed(ActionEvent ae) {
        String s = ae.getActionCommand();

        if (s.equals("취소")) {
            dispose();
        } else if (s.equals("확인")) {
            String id = posIdField.getText();
            String name = nameField.getText();
            String phone = phoneField.getText();

            if (id.isEmpty() || name.isEmpty() || phone.isEmpty()) {
                MessageDialog md = new MessageDialog(this, "오류", true, "모든 항목들을 입력해주세요.");
                md.setLocationRelativeTo(this);
                md.setVisible(true);
            } else {
                // Implement password retrieval logic
                MessageDialog md = new MessageDialog(this, "비밀번호 찾기", true, "---님의 비밀번호는 --- 입니다. (추후 연동 후 구현)");
                md.setLocationRelativeTo(this);
                md.setVisible(true);
            }
        }
    }
}
