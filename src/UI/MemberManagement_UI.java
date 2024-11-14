package UI;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MemberManagement_UI extends JPanel{

    public MemberManagement_UI() {
        create_search();
        create_memberTable();


        setVisible(true);
        setSize(1600,900);
    }
    void create_search() {
        JPanel search = new JPanel();
        JTextField searchField = new JTextField(20); //검색창
        JButton searchBtn = new JButton("검색"); //검색버튼
        search.add(searchField);
        search.add(searchBtn);

        searchBtn.addActionListener(new ActionListener() {// 회원 검색
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = searchField.getText();
                //keyword를 넘겨서 원하는 회원 검색
            }
        });

        add(search);
    }
    void create_memberTable() {
        String[] colums = {"회원번호", "이름", "성별", "연령", "남은시간", "사용시간", "총사용금액", "생년월일", "가입날자"};

        DefaultTableModel model = new DefaultTableModel(colums, 100);
        JTable member_table = new JTable(model);
        member_table.setRowHeight(20);

        JScrollPane jScrollPane = new JScrollPane(member_table);
        jScrollPane.setPreferredSize(new Dimension(1580, 860)); //테이블 사이즈

        add(jScrollPane, BorderLayout.CENTER);
    }
}
