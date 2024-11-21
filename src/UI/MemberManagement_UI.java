package UI;

import DAO.MemberDAO;
import DTO.MemberDTO;
import Jdbc.PCPosDBConnection;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;

public class MemberManagement_UI extends JPanel {
    private JButton searchBtn, addBtn, editBtn, deleteBtn;
    private JPanel search, btnPanel;
    private DefaultTableModel model;
    private JTextField searchField;
    private MemberDAO memberDAO; //DAO 객체

    public MemberManagement_UI() {
        try {
            //데이터베이스 연결
            Connection conn = new PCPosDBConnection().getConnection();
            memberDAO = new MemberDAO(conn);

            //UI 구성요소
            create_search();
            create_BtnPanel();
            create_memberTable();
            fetchAndUpdateTable();

            setVisible(true);
            setSize(1600, 900);
        } catch (Exception e) {

        }
    }

    private void fetchAndUpdateTable() {
        try {
            List<MemberDTO> members = memberDAO.findAll(); //모든 회원정보 가져옴
            model.setRowCount(0); // 기존 데이터 초기화
            for (MemberDTO m : members) { //연령, 사용시간, 총 사용금액(아직 못함)
                Object[] row = {
                        m.getMember_no(), //번호
                        m.getMember_name(), //이름
                        m.getSex(), //성별
                        //연령
                        m.getLeft_time(),
                        //사용시간
                        //총사용금액
                        m.getBirthday(),
                        m.getReg_date()
                };
                model.addRow(row); //테이블에 행 추가
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void deleteMember() { // 회원 삭제

    }

    private void addMember() { // 회원 추가

    }

    private void editMember() { // 회원 수정

    }

    private void searchMember(String keyword) { //회원 검색

    }

    void create_BtnPanel() { //수정, 추가, 삭제 버튼
        btnPanel = new JPanel();

        editBtn = new JButton("회원수정");
        addBtn = new JButton("회원추가");
        deleteBtn = new JButton("회원삭제");

        btnPanel.add(editBtn);
        btnPanel.add(addBtn);
        btnPanel.add(deleteBtn);

        editBtn.addActionListener(new ActionListener() { //수정
            @Override
            public void actionPerformed(ActionEvent e) {
                editMember();
            }
        });
        addBtn.addActionListener(new ActionListener() { //추가
            @Override
            public void actionPerformed(ActionEvent e) {
                addMember();
            }
        });
        deleteBtn.addActionListener(new ActionListener() { //삭제
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteMember();
            }
        });

        add(btnPanel);
    }

    void create_search() { //검색 패널 생성 및 검색 필드, 버튼 추가
        search = new JPanel();
        searchField = new JTextField(20); //검색창
        searchBtn = new JButton("검색"); //검색버튼
        search.add(searchField);
        search.add(searchBtn);

        searchBtn.addActionListener(new ActionListener() {// 회원 검색
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = searchField.getText();
                searchMember(keyword);
            }
        });

        add(search);
    }

    void create_memberTable() { //테이블 생성 및 설정
        String[] colums = {"회원번호", "이름", "성별", "연령", "남은시간", "사용시간", "총사용금액", "생년월일", "가입날자"};

        model = new DefaultTableModel(colums, 0); //테이블 모델 초기화
        JTable member_table = new JTable(model); //테이블 생성
        member_table.setRowHeight(20); //행 높이 설정

        JScrollPane jScrollPane = new JScrollPane(member_table); //스크롤 패널
        jScrollPane.setPreferredSize(new Dimension(1580, 860)); //테이블 사이즈

        add(jScrollPane, BorderLayout.CENTER);
    }
}
