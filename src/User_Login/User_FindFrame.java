package User_Login;

import DAO.MemberDAO;
import DTO.MemberDTO;
import Jdbc.PCPosDBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

class User_FindFrame extends JFrame {
    JTextField userIdField, userNameField;
    JButton confirmButton, cancelButton;

    public User_FindFrame(String title) {
        setTitle(title);
        setResizable(false);
        setBounds(100, 100, 350, 300);
        //창 종료시 찾기 창만 꺼지게 수정
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

        Container ct = getContentPane();
        ct.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        ct.add(mainPanel, BorderLayout.CENTER);

        JLabel idLabel = new JLabel("ID    :");
        idLabel.setBounds(31, 34, 70, 15);
        mainPanel.add(idLabel);

        userIdField = new JTextField();
        userIdField.setBounds(113, 31, 116, 21);
        mainPanel.add(userIdField);

        JLabel nameLabel = new JLabel("이름         : ");
        nameLabel.setBounds(31, 85, 70, 15);
        mainPanel.add(nameLabel);

        userNameField = new JTextField();
        userNameField.setBounds(113, 82, 116, 21);
        mainPanel.add(userNameField);

        JPanel bottomPanel = new JPanel();
        ct.add(bottomPanel, BorderLayout.SOUTH);

        cancelButton = new JButton("취소");
        bottomPanel.add(cancelButton);

        confirmButton = new JButton("확인");
        bottomPanel.add(confirmButton);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                dispose();
            }
        });

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String id = userIdField.getText();
                String name = userNameField.getText();

                if (id.isEmpty() || name.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "항목을 모두 입력해주세요");
                } else {
                    Connection conn = null;
                    conn = PCPosDBConnection.getConnection();
                    MemberDAO memberDAO = new MemberDAO(conn);
                    MemberDTO member = memberDAO.findById(userIdField.getText());

                    if (member.getMember_name().equals(userNameField.getText())) {
                        JOptionPane.showMessageDialog(null, "비밀번호: " + member.getMember_pwd());
                    } else
                        JOptionPane.showMessageDialog(null, ":꺼져");

                }
            }
        });
    }
}