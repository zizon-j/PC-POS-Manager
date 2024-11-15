package UI;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MemberManagement_UI extends JPanel{
    private JButton searchBtn, addBtn, editBtn, deleteBtn;
    private JPanel search, btnPanel;
    private DefaultTableModel model;

    public MemberManagement_UI() {
        create_search();
        btnPlus();
        create_memberTable();


        setVisible(true);
        setSize(1600,900);
    }
    private void deleteRow() { // 회원 삭제(직원 삭제)

    }
    void btnPlus() { //수정, 추가, 삭제 버튼
        btnPanel = new JPanel();

        editBtn = new JButton("회원수정");
        addBtn = new JButton("회원추가");
        deleteBtn = new JButton("회원삭제");

        btnPanel.add(editBtn);
        btnPanel.add(addBtn);
        btnPanel.add(deleteBtn);

        add(btnPanel);
    }
    void create_search() { //검색 패널 생성 및 검색 필드, 버튼 추가
        search = new JPanel();
        JTextField searchField = new JTextField(20); //검색창
        searchBtn = new JButton("검색"); //검색버튼
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
    void create_memberTable() { //테이블 생성 및 설정
        String[] colums = {"회원번호", "이름", "성별", "연령", "남은시간", "사용시간", "총사용금액", "생년월일", "가입날자"};

        model = new DefaultTableModel(colums, 100);
        JTable member_table = new JTable(model);
        member_table.setRowHeight(20);

        JScrollPane jScrollPane = new JScrollPane(member_table); //스크롤 패널
        jScrollPane.setPreferredSize(new Dimension(1580, 860)); //테이블 사이즈

        add(jScrollPane, BorderLayout.CENTER);
    }
}
