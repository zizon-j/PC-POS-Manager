package User_Login;

import main.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class User_LoginFrame extends JFrame implements ActionListener {
    JTextField id;
    JPasswordField passwd;
    JButton loginBtn, deleteBtn, registerBtn, findBtn;

    public User_LoginFrame(String title) {
        setTitle(title);
        setResizable(false);
        setBounds(0, 0, 600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 창을 화면 중앙에 위치
//        setLocationRelativeTo(null);

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

        deleteBtn = new JButton("취소");
        deleteBtn.setBounds(241, 265, 97, 23);
        deleteBtn.addActionListener(this);
        ct.add(deleteBtn);

        registerBtn = new JButton("회원가입");
        registerBtn.setBounds(364, 265, 97, 23);
        registerBtn.addActionListener(this);
        ct.add(registerBtn);

        findBtn = new JButton("비밀번호 찾기");
        findBtn.setBounds(210, 232, 165, 23);
        findBtn.addActionListener(this);
        ct.add(findBtn);
    }

    public void actionPerformed(ActionEvent ae) {
        String s = ae.getActionCommand();

        if (s.equals("취소")) {
            id.setText("");
            passwd.setText("");
        } else if (s.equals("회원가입")) {
            User_RegisterFrame userRegisterFrame = new User_RegisterFrame("회원가입");
            userRegisterFrame.setVisible(true);
        } else if (s.equals("비밀번호 찾기")) {
            User_FindFrame userFindFrame = new User_FindFrame("비밀번호 찾기");
            userFindFrame.setVisible(true);
        } else if (s.equals("로그인")) {
            // 로그인 처리 (DB 연동 후 추가 예정)
            new Main(); // Main 화면 호출
            this.dispose(); // 현재 Login 창 닫기
        }
    }

    public static void main(String[] args) {
        User_LoginFrame userLoginFrame = new User_LoginFrame("명전 피시방 사용자 키오스크");
        userLoginFrame.setVisible(true);
    }
}
