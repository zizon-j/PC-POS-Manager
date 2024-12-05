package UI;

import Common_Panel.Time_Plus_Jpanel;
import DAO.MemberDAO;
import DAO.Time_Plus_LogDAO;
import DAO.UsageHistoryDAO;
import DTO.MemberDTO;
import Jdbc.PCPosDBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;
class NotSelectedRowException extends Exception {}
public class MemberManagement_UI extends JPanel {
    private JButton searchBtn, addBtn, editBtn, deleteBtn, saveEditBtn, cancelEditBtn, resetpwdBtn;
    private JPanel search, btnPanel, EditMember_leftPanel, EditMember_rightPanel, EditMemberPanel, AddMemberPanel;
    private JTable member_table; //회원 테이블
    private DefaultTableModel model;
    private JTextField searchField;
    private JDialog EditMemberDialog, AddMemberDialog;
    private JTextField idField, pwdField, nameField, birthdayField, phoneField, addressField,
            totalUsage_Money, totalUsage_time, joinDate;
    private JRadioButton man, woman;
    private ButtonGroup group;
    Time_Plus_Jpanel time_plus_jpanel; //시간 추가 패널
    private MemberDAO memberDAO; //회원DAO 객체
    private UsageHistoryDAO usageHistoryDAO; // 사용기록DAO 객체
    private Time_Plus_LogDAO time_Plus_LogDAO;

    public MemberManagement_UI() {
        try {
            //데이터베이스 연결
            Connection conn = new PCPosDBConnection().getConnection();
            memberDAO = new MemberDAO(conn);
            usageHistoryDAO = new UsageHistoryDAO(conn);
            time_Plus_LogDAO = new Time_Plus_LogDAO(conn);
            time_plus_jpanel = new Time_Plus_Jpanel();
            add(time_plus_jpanel);


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
                int totalUsageTime = usageHistoryDAO.calTotalUsageTime(m.getMember_no());
                double totalPaymentAmount = time_Plus_LogDAO.calTotalUsageMoney(m.getMember_id());

                Object[] row = {
                        m.getMember_no(), //번호
                        m.getMember_name(), //이름
                        m.getSex(), //성별
                        m.getPhone(),
                        m.getLeft_time(),
                        totalUsageTime + "분",
                        totalPaymentAmount,
                        m.getBirthday(),
                        m.getReg_date()
                };
                model.addRow(row); //테이블에 행 추가
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
                AddMemberDialog();
            }
        });
        deleteBtn.addActionListener(new ActionListener() { //회원 삭제
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow; //열 선택

                try {
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
                    else {
                        throw new NotSelectedRowException();
                    }
                } catch (NotSelectedRowException notselected) {
                    JOptionPane.showMessageDialog(MemberManagement_UI.this, "삭제할 회원을 선택해주세요.");
                }
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
                String keyword = searchField.getText(); // 검색어 입력

                try {
                    List<MemberDTO> member = memberDAO.findAll();

                    for (MemberDTO m : member) {
                        if (keyword.equals(m.getMember_name())) { //검색 내용과 같다면
                            int totalUsageTime = usageHistoryDAO.calTotalUsageTime(m.getMember_no());
                            double totalPaymentAmount = time_Plus_LogDAO.calTotalUsageMoney(m.getMember_id());

                            model.setRowCount(0); // 기존 데이터 초기화
                            Object[] row = {
                                    m.getMember_no(), //번호
                                    m.getMember_name(), //이름
                                    m.getSex(), //성별
                                    m.getPhone(),
                                    m.getLeft_time(),
                                    totalUsageTime + "분",
                                    totalPaymentAmount,
                                    m.getBirthday(),
                                    m.getReg_date()
                            };
                            model.addRow(row);
                        } else if (keyword.isEmpty()) //비어 있으면 테이블 업데이트
                            fetchAndUpdateTable();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // 검색창에서 엔터 키로 검색
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchBtn.doClick(); // 검색 버튼 동작 수행
                }
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
        AddMemberDialog.setSize(640, 360);
        AddMemberDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // 메인 패널: 8행 2열
        AddMemberPanel = new JPanel(new GridLayout(8, 2, 10, 10));

        // 이름 필드
        AddMemberPanel.add(new JLabel("이름"));
        JTextField nameField = new JTextField(20);
        AddMemberPanel.add(nameField);

        // 아이디 필드
        AddMemberPanel.add(new JLabel("아이디"));
        JTextField idField = new JTextField(20);
        AddMemberPanel.add(idField);

        // 비밀번호 필드
        AddMemberPanel.add(new JLabel("비밀번호"));
        JPasswordField pwdField = new JPasswordField(20);
        AddMemberPanel.add(pwdField);

        // 생년월일 필드
        AddMemberPanel.add(new JLabel("생년월일 (YYYY-MM-DD)"));
        JTextField birthdayField = new JTextField(20);
        AddMemberPanel.add(birthdayField);

        // 성별 선택
        AddMemberPanel.add(new JLabel("성별"));
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JRadioButton manBtn = new JRadioButton("남자");
        JRadioButton womanBtn = new JRadioButton("여자");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(manBtn);
        genderGroup.add(womanBtn);
        genderPanel.add(manBtn);
        genderPanel.add(womanBtn);
        AddMemberPanel.add(genderPanel);

        // 전화번호 필드
        AddMemberPanel.add(new JLabel("전화번호 (- 기입)"));
        JTextField phoneField = new JTextField(20);
        AddMemberPanel.add(phoneField);

        // 주소 필드
        AddMemberPanel.add(new JLabel("주소"));
        JTextField addressField = new JTextField(20);
        AddMemberPanel.add(addressField);

        // 빈 공간 추가
        AddMemberPanel.add(new JLabel());


        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("생성");
        JButton cancelButton = new JButton("취소");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        saveButton.addActionListener(e -> {
           String name = nameField.getText();
           String id = idField.getText();
           String pwd = pwdField.getText();
           String birthday = birthdayField.getText();
           String gender = manBtn.isSelected() ? "남자" : "여자";
           String phone = phoneField.getText();
           String address = addressField.getText();

           if (name.isEmpty() || id.isEmpty() || pwd.isEmpty() || birthday.isEmpty() || phone.isEmpty() || address.isEmpty()) {
               JOptionPane.showMessageDialog(AddMemberDialog, "모든 항목을 입력해주세요.");
               return;
           }
           if (!phone.matches("^01[0-9]-\\d{3,4}-\\d{4}$")) { //정규식 검증
               JOptionPane.showMessageDialog(AddMemberDialog, "전화번호를 형식에 맞게 입력하세요.");
               return;
           }

           try { //회원 정보 추가
               MemberDTO newMeber = new MemberDTO();
               newMeber.setMember_name(name);
               newMeber.setMember_id(id);
               newMeber.setMember_pwd(pwd);
               newMeber.setBirthday(Date.valueOf(birthday));
               newMeber.setSex(gender);
               newMeber.setPhone(phone);
               newMeber.setAddress(address);

               memberDAO.insert(newMeber);
               JOptionPane.showMessageDialog(AddMemberDialog, "회원이 추가되었습니다.");
               AddMemberDialog.dispose();
           } catch (Exception ex) {
               ex.printStackTrace();
           }
        });
        cancelButton.addActionListener(e -> {
            AddMemberDialog.dispose();
        });

        AddMemberPanel.add(buttonPanel);

        AddMemberDialog.add(AddMemberPanel, BorderLayout.CENTER);
        AddMemberDialog.setLocationRelativeTo(null);
        AddMemberDialog.setVisible(true);
    }
    private void EditMemberDialog() { //회원 수정 다이얼로그 생성, 그리드백 레이아웃으로 그리드레이아웃보다 더 유연한 배치 가능
        int selectedRow = member_table.getSelectedRow(); //선택한 회원
        if (selectedRow == -1){ //선택하지 않았다면 return
            JOptionPane.showMessageDialog(this, "수정할 회원을 선택해주세요.");
            return;
        }

        String memberNo = String.valueOf(model.getValueAt(selectedRow, 0)); //가져온 회원의 번호
        try {
            MemberDTO member = memberDAO.findByNo(memberNo); //회원 정보 가져오기

            if(member == null) {
                JOptionPane.showMessageDialog(this, "회원 정보를 가져오지 못했습니다.");
                return;
            }

            EditMemberDialog = new JDialog(new Frame(), "회원 수정", true);
            EditMemberDialog.setSize(1280, 720); //사이즈 설정
            EditMemberDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);


            EditMemberPanel = new JPanel(new BorderLayout()); //전체 레이아웃을 border로 설정
            EditMember_leftPanel = new JPanel(new GridBagLayout());
            EditMember_rightPanel = new JPanel(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5); //컴포넌트 간 여백 설정
            gbc.fill = GridBagConstraints.HORIZONTAL; // 컴포넌트의 너비를 가득 채우도록 설정

            EditMember_leftPanel.setBorder(BorderFactory.createTitledBorder("회원 정보"));

            // 아이디 필드
            gbc.gridx = 0; gbc.gridy = 0; // 컴포넌트의 위치 지정, 열과 행의 위치
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

            //오른쪽 패널 정보
            EditMember_rightPanel.setBorder(BorderFactory.createTitledBorder("회원 이용 내역"));

            GridBagConstraints gbcRight = new GridBagConstraints();
            gbcRight.insets = new Insets(5, 5, 5, 5); // 여백 설정
            gbcRight.fill = GridBagConstraints.HORIZONTAL; // 컴포넌트 크기 조정


            int totalUsageTime = usageHistoryDAO.calTotalUsageTime(Integer.parseInt(memberNo));
            double totalPaymentAmount = time_Plus_LogDAO.calTotalUsageMoney(member.getMember_id());

            //총 사용시간
            gbcRight.gridx = 0; gbcRight.gridy = 0;
            EditMember_rightPanel.add(new JLabel("총 사용시간"), gbcRight);
            gbcRight.gridx = 1; gbcRight.gridy = 0;
            totalUsage_time = new JTextField(String.valueOf(totalUsageTime));
            totalUsage_time.setEditable(false); // 수정 불가능
            EditMember_rightPanel.add(totalUsage_time, gbcRight);

            // 총 사용금액 레이블 및 필드
            gbcRight.gridx = 0; gbcRight.gridy = 1;
            EditMember_rightPanel.add(new JLabel("총 사용금액"), gbcRight);
            gbcRight.gridx = 1; gbcRight.gridy = 1;
            totalUsage_Money = new JTextField(String.valueOf(totalPaymentAmount));
            totalUsage_Money.setEditable(false);
            EditMember_rightPanel.add(totalUsage_Money, gbcRight);

            // 가입일 레이블 및 필드
            gbcRight.gridx = 0; gbcRight.gridy = 2;
            EditMember_rightPanel.add(new JLabel("가입일"), gbcRight);
            gbcRight.gridx = 1; gbcRight.gridy = 2;
            joinDate = new JTextField(String.valueOf(member.getReg_date()), 15);
            joinDate.setEditable(false);
            EditMember_rightPanel.add(joinDate, gbcRight);



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
            saveEditBtn.addActionListener(new ActionListener() { //입력된 정보 가져와서 정보 수정
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newPwd = pwdField.getText().toString();
                    String newPhone = phoneField.getText().toString();
                    String newAddress = addressField.getText().toString();
                    String newSex = man.isSelected() ? "남자":"여자"; //선택된 성별 확인

                    if (!newPhone.matches("^01[0-9]-\\d{3,4}-\\d{4}$")) { //정규식 검증
                        JOptionPane.showMessageDialog(AddMemberDialog, "전화번호를 형식에 맞게 입력하세요.");
                        return;
                    }

                    MemberDTO member = new MemberDTO();
                    member.setMember_pwd(newPwd);
                    member.setPhone(newPhone);
                    member.setAddress(newAddress);
                    member.setSex(newSex);
                    member.setMember_no(Integer.parseInt(memberNo));

                    memberDAO.updateMemberInfo(member);

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
