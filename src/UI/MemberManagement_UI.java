package UI;

import DAO.MemberDAO;
import DAO.UsageHistoryDAO;
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
class NotSelectedRowException extends Exception {}
public class MemberManagement_UI extends JPanel {
    private JButton searchBtn, addBtn, editBtn, deleteBtn, saveEditBtn, cancelEditBtn, resetpwdBtn;
    private JPanel search, btnPanel, EditMember_leftPanel, EditMember_rightPanel, EditMemberPanel;
    private JTable member_table; //회원 테이블
    private DefaultTableModel model;
    private JTextField searchField;
    private JDialog EditMemberDialog, AddMemberDialog;
    private JTextField idField, pwdField, nameField, birthdayField, phoneField, addressField,
            totalUsage_Money, totalUsage_time, joinDate;
    private JRadioButton man, woman;
    private ButtonGroup group;

    private MemberDAO memberDAO; //회원DAO 객체
    private UsageHistoryDAO usageHistoryDAO; // 사용기록DAO 객체

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

    private void fetchAndUpdateTable() {//정보 가져오기 및 업데이트
        try {
            List<MemberDTO> members = memberDAO.findAll(); //모든 회원정보 가져옴
            model.setRowCount(0); // 기존 데이터 초기화
            for (MemberDTO m : members) { //연령, 사용시간, 총 사용금액(아직 못함)
                Object[] row = {
                        m.getMember_no(), //번호
                        m.getMember_name(), //이름
                        m.getSex(), //성별
                        m.getPhone(),
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
    private void searchMember(String keyword) { //회원 검색
        try {
            List<MemberDTO> member = memberDAO.findAll();
            for (MemberDTO m : member) {
                if (keyword.equals(m.getMember_name())) { //검색 내용과 같다면
                    model.setRowCount(0); // 기존 데이터 초기화
                    Object[] row = {
                            m.getMember_no(), //번호
                            m.getMember_name(), //이름
                            m.getSex(), //성별
                            m.getPhone(),
                            m.getLeft_time(),
                            //사용시간
                            //총사용금액
                            m.getBirthday(),
                            m.getReg_date()
                    };
                    model.addRow(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void create_BtnPanel() { //수정, 추가, 삭제 버튼
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
                EditMemberDialog();
            }
        });
        addBtn.addActionListener(new ActionListener() { //회원 추가
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        deleteBtn.addActionListener(new ActionListener() { //회원 삭제
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow; //열 선택
                selectedRow = member_table.getSelectedRow(); //선택된 행
                String memberNo = String.valueOf(model.getValueAt(selectedRow, 0)); //회원 번호
                String memberName = String.valueOf(model.getValueAt(selectedRow, 1)); //회원 이름

                if (selectedRow != -1) { //선택 됐다면
                    int option  = JOptionPane.showConfirmDialog(MemberManagement_UI.this,
                            "정말로 회원 " + memberName + "을(를) 삭제하시겠습니까?", "회원 삭제 확인", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                    if(option == JOptionPane.YES_OPTION) {//확인 버튼을 눌렀다면
                        model.removeRow(selectedRow); //삭제
                        memberDAO.delete(memberNo);
                    }
                }
                else
                    JOptionPane.showMessageDialog(MemberManagement_UI.this, "삭제할 회원을 선택해주세요.");
            }
        });

        add(btnPanel);
    }

    private void create_search() { //검색 패널 생성 및 검색 필드, 버튼 추가
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

    private void create_memberTable() { //테이블 생성 및 설정
        String[] colums = {"회원번호", "이름", "성별", "연락처", "남은시간", "사용시간", "총사용금액", "생년월일", "가입날짜"};

        model = new DefaultTableModel(colums, 0); //테이블 모델 초기화
        member_table = new JTable(model); //테이블 생성
        member_table.setRowHeight(20); //행 높이 설정

        JScrollPane jScrollPane = new JScrollPane(member_table); //스크롤 패널
        jScrollPane.setPreferredSize(new Dimension(1580, 860)); //테이블 사이즈

        add(jScrollPane, BorderLayout.CENTER);
    }
    private void AddMemberDialog() { //회원 추가 다이얼로그 생성
        AddMemberDialog = new JDialog(new Frame(), "회원 추가", true);
        AddMemberDialog.setSize(700, 520);
    }
    private void EditMemberDialog() { //회원 수정 다이얼로그 생성
        int selectedRow = member_table.getSelectedRow(); //선택한 회원
        if (selectedRow == -1){ //선택하지 않았다면 return
            JOptionPane.showMessageDialog(this, "수정할 회원을 선택해주세요.");
            return;
        }

        String memberNo = String.valueOf(model.getValueAt(selectedRow, 0)); //가져온 회원의 번호
        try {
            MemberDTO member = memberDAO.findById(memberNo); //회원 정보 가져오기

            if(member == null) {
                JOptionPane.showMessageDialog(this, "회원 정보를 가져오지 못했습니다.");
                return;
            }

            EditMemberDialog = new JDialog(new Frame(), "회원 수정", true);
            EditMemberDialog.setSize(1280, 720); //사이즈 설정
            EditMemberDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);


            EditMemberPanel = new JPanel(new BorderLayout());
            EditMember_leftPanel = new JPanel(new GridBagLayout());
            EditMember_rightPanel = new JPanel(new GridLayout(5, 1));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            EditMember_leftPanel.setBorder(BorderFactory.createTitledBorder("회원 정보"));

            // 아이디 필드
            gbc.gridx = 0; gbc.gridy = 0;
            EditMember_leftPanel.add(new JLabel("아이디"), gbc);
            gbc.gridx = 1; gbc.gridy = 0;
            idField = new JTextField(member.getMember_id());
            idField.setEditable(false); //수정불가능
            EditMember_leftPanel.add(idField, gbc);

            // 비밀번호 필드
            gbc.gridx = 0; gbc.gridy = 1;
            EditMember_leftPanel.add(new JLabel("비밀번호"), gbc);
            gbc.gridx = 1; gbc.gridy = 1;
            pwdField = new JTextField(member.getMember_pwd());
            EditMember_leftPanel.add(pwdField, gbc);
            gbc.gridx = 2; gbc.gridy = 1;
            resetpwdBtn = new JButton("초기화");
            EditMember_leftPanel.add(resetpwdBtn, gbc);


            // 이름 필드
            gbc.gridx = 0; gbc.gridy = 2;
            EditMember_leftPanel.add(new JLabel("이름"), gbc);
            gbc.gridx = 1; gbc.gridy = 2;
            nameField = new JTextField(member.getMember_name());
            nameField.setEditable(false);
            EditMember_leftPanel.add(nameField, gbc);

            // 생년월일 필드
            gbc.gridx = 0; gbc.gridy = 3;
            EditMember_leftPanel.add(new JLabel("생년월일"), gbc);
            gbc.gridx = 1; gbc.gridy = 3;
            birthdayField = new JTextField(String.valueOf(member.getBirthday()));
            birthdayField.setEditable(false);
            EditMember_leftPanel.add(birthdayField, gbc);

            // 전화번호 필드
            gbc.gridx = 0; gbc.gridy = 4;
            EditMember_leftPanel.add(new JLabel("전화번호"), gbc);
            gbc.gridx = 1; gbc.gridy = 4;
            phoneField = new JTextField(member.getPhone());
            EditMember_leftPanel.add(phoneField, gbc);

            // 주소 필드
            gbc.gridx = 0; gbc.gridy = 5;
            EditMember_leftPanel.add(new JLabel("주소"), gbc);
            gbc.gridx = 1; gbc.gridy = 5;
            addressField = new JTextField(member.getAddress());
            EditMember_leftPanel.add(addressField, gbc);

            // 성별 선택
            gbc.gridx = 0; gbc.gridy = 6;
            EditMember_leftPanel.add(new JLabel("성별"), gbc);
            gbc.gridx = 1; gbc.gridy = 6;
            JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            man = new JRadioButton("남성");
            woman = new JRadioButton("여성");
            group = new ButtonGroup();
            group.add(man);
            group.add(woman);
            genderPanel.add(man);
            genderPanel.add(woman);

            if (member.getSex().equals("남자")) //남자인지 여자인지
                man.setSelected(true);
            else
                woman.setSelected(true);
            EditMember_leftPanel.add(genderPanel, gbc);

            EditMember_rightPanel.setBorder(BorderFactory.createTitledBorder("회원 이용 내역"));
            EditMember_rightPanel.add(new JLabel("총 사용시간"));
            totalUsage_time = new JTextField();
            EditMember_rightPanel.add(new JLabel("총 사용금액"));
            totalUsage_Money = new JTextField();
            EditMember_rightPanel.add(new JLabel("가입일"));
            joinDate = new JTextField(String.valueOf(member.getReg_date()));
            joinDate.setEditable(false);
            EditMember_rightPanel.add(joinDate);





            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            saveEditBtn = new JButton("저장");
            cancelEditBtn = new JButton("취소");

            resetpwdBtn.addActionListener(new ActionListener() { //비밀번호를 초기화하고 재생성
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newpwd = "a1234567890";
                    pwdField.setText(newpwd);

                }
            });
            cancelEditBtn.addActionListener(new ActionListener() { //취소버튼 클릭시 종료
                @Override
                public void actionPerformed(ActionEvent e) {
                    EditMemberDialog.dispose();
                }
            });
            saveEditBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                        String newpwd = pwdField.getText();
                        member.setMember_pwd(newpwd);

                        EditMemberDialog.dispose();
                }
            });

            buttonPanel.add(saveEditBtn);
            buttonPanel.add(cancelEditBtn);

            //패널 추가 및 컴포넌트 추가
            EditMemberPanel.add(EditMember_leftPanel, BorderLayout.CENTER); //왼쪽 패널
            EditMemberPanel.add(EditMember_rightPanel, BorderLayout.EAST); //오른쪽 패널
            EditMemberPanel.add(buttonPanel, BorderLayout.SOUTH);

            EditMemberDialog.add(EditMemberPanel);
            EditMemberDialog.setLocationRelativeTo(null); // 화면의 중앙에 배치
            EditMemberDialog.setVisible(true); //다이얼로그 표시
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
