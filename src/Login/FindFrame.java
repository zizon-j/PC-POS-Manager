package Login;

import DAO.EmployeeDAO;
import DTO.EmployeeDTO;
import Jdbc.PCPosDBConnection;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;

class FindFrame extends JFrame implements ActionListener {
    private JTextField posIdBox;
    private JTextField idBox;
    private JTextField nameBox;
    private JTextField phoneBox;
    private JComboBox<String> phoneFirst;
    private JButton okBtn, cancelBtn;
    private String[] phoneNumbers = { "010", "070", "02", "031", "032" };
    private JRadioButton adminBtn, empBtn;
    private JPanel posPanel;

    public FindFrame(String title) {
        setTitle(title);
        setSize(350, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Container main = getContentPane();
        main.setLayout(new BorderLayout());

        JPanel bottom = new JPanel();
        main.add(bottom, BorderLayout.SOUTH);

        cancelBtn = new JButton("취소");
        cancelBtn.addActionListener(this);
        bottom.add(cancelBtn);

        okBtn = new JButton("확인");
        okBtn.addActionListener(this);
        bottom.add(okBtn);

        JPanel inputPanel = new JPanel();
        main.add(inputPanel, BorderLayout.CENTER);
        inputPanel.setLayout(new GridLayout(5, 1));

        JPanel userType = new JPanel(new FlowLayout(FlowLayout.LEFT));
        adminBtn = new JRadioButton("관리자");
        empBtn = new JRadioButton("직원", true);
        ButtonGroup group = new ButtonGroup();
        group.add(adminBtn);
        group.add(empBtn);
        userType.add(adminBtn);
        userType.add(empBtn);
        inputPanel.add(userType);

        posPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        posPanel.add(new JLabel("POS ID     :  "));
        posIdBox = new JTextField(8);
        posPanel.add(posIdBox);
        inputPanel.add(posPanel);

        makeInputRow(inputPanel, "아이디        :  ", idBox = new JTextField(8));
        makeInputRow(inputPanel, "이름           :  ", nameBox = new JTextField(8));

        JPanel phonePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        phonePanel.add(new JLabel("전화번호    :  "));
        phoneFirst = new JComboBox<>(phoneNumbers);
        phoneBox = new JTextField(10);
        phonePanel.add(phoneFirst);
        phonePanel.add(phoneBox);
        inputPanel.add(phonePanel);

        adminBtn.addActionListener(e -> showHidePosId());
        empBtn.addActionListener(e -> showHidePosId());
        showHidePosId();
    }

    private void makeInputRow(JPanel parent, String label, JTextField field) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel(label));
        row.add(field);
        parent.add(row);
    }

    private void showHidePosId() {
        posPanel.setVisible(adminBtn.isSelected());
        revalidate();
        repaint();
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd.equals("취소")) {
            dispose();
        } else if (cmd.equals("확인")) {
            checkAndFind();
        }
    }

    private void checkAndFind() {
        String id = idBox.getText().trim();
        String name = nameBox.getText().trim();
        String phone = phoneBox.getText().trim();

        if (adminBtn.isSelected()) {
            String posId = posIdBox.getText().trim();
            if (!posId.equals("2020081049")) {
                showError("포스기 ID가 맞지 않습니다.");
                return;
            }
        }

        if (id.isEmpty() || name.isEmpty() || phone.isEmpty()) {
            showError("모든 항목을 입력해주세요.");
            return;
        }

        if (phone.length() != 8) {
            showError("전화번호를 8자리로 입력해주세요.\n예: 12345678");
            return;
        }

        String fullPhone = phoneFirst.getSelectedItem() + "-" +
                phone.substring(0, 4) + "-" +
                phone.substring(4);

        findPassword(id, name, fullPhone);
    }

    private void findPassword(String id, String name, String phone) {
        Connection db = PCPosDBConnection.getConnection();
        if (db == null) {
            showError("DB 연결 실패!");
            return;
        }

        try {
            EmployeeDAO dao = new EmployeeDAO(db);
            EmployeeDTO user = dao.findById(id);

            if (user == null) {
                showError("일치하는 회원이 없습니다.");
                return;
            }

            if (empBtn.isSelected() && user.getPriority().equals("관리자")) {
                showError("관리자 정보를 볼 수 없습니다!");
                return;
            }

            if (user.getEmp_name().equals(name) && user.getEmp_phone().equals(phone)) {
                showMessage("비밀번호 찾기 성공",
                        user.getEmp_name() + "님의 비밀번호는 " + user.getEmp_pwd() + " 입니다.");
                dispose();
            } else {
                showError("입력한 정보가 일치하지 않습니다.");
            }
        } finally {
            try {
                db.close();
            } catch (Exception e) {
            }
        }
    }

    private void showError(String msg) {
        showMessage("오류", msg);
    }

    private void showMessage(String title, String msg) {
        Login_MessageDialog dialog = new Login_MessageDialog(this, title, true, msg);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
