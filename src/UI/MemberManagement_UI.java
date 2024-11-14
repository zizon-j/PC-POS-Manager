package UI;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MemberManagement_UI extends JPanel{

    public MemberManagement_UI() {
        create_memberTable();


        setVisible(true);
        setSize(1600,900);
    }
    void create_memberTable() {
        String[] colums = {"회원번호", "이름", "비밀번호", "성별", "연령", "사용시간", "총사용금액", "생년월일", "전화번호", "가입날자"};

        DefaultTableModel model = new DefaultTableModel(colums, 0);
        JTable member_table = new JTable(model);
        JScrollPane jScrollPane = new JScrollPane(member_table);
        jScrollPane.setPreferredSize(new Dimension(1600, 900));

        add(jScrollPane);

    }
}
