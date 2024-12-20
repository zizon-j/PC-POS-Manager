package UI;

import DAO.MemberDAO;
import DAO.SeatDAO;
import DTO.MemberDTO;
import DTO.SeatDTO;
import Jdbc.PCPosDBConnection;
import User_Login.User_LoginFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
class UsingSeatsException extends Exception{}

public class Seat_UI extends JPanel {

    public JPanel editPanel;
    public JButton btnEdit, btnReset;

    public Seat_UI() {
        setLayout(new BorderLayout());

        //초기 입력창
        JPanel inputPanel = new JPanel(new FlowLayout());
        setLayout(new BorderLayout());
        JLabel inputLabel = new JLabel("자리");
        JTextField textFieldx = new JTextField(10);
        JLabel x = new JLabel("X");
        JTextField textFieldy = new JTextField(10);
        JButton btnGenerate = new JButton("자리 생성");

        inputPanel.add(inputLabel);
        inputPanel.add(textFieldx);
        inputPanel.add(x);
        inputPanel.add(textFieldy);
        inputPanel.add(btnGenerate);

        add(inputPanel, BorderLayout.NORTH);

        // 자리 배치창
        editPanel= new JPanel(); // 버튼 편집 버튼 묶기
        editPanel.setLayout(new FlowLayout());
        btnEdit = new JButton("편집");
        btnReset = new JButton("초기화");

        editPanel.add(btnEdit);
        editPanel.add(btnReset);
        JPanel seat_panel = new JPanel();

        //버튼 패널
        //borderlayout은하나의 영역에하나의 컴포넌트만 배치가능
        //하나의 영역에여러 개의컴포넌트를 한번에 배치하기 위해서는 Jpanel 사용
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BorderLayout());
        JButton btnactivate = new JButton("활성화/비활성화");
        JButton btnInfo = new JButton("정보보기");
        JTextArea seat_info = new JTextArea();

        JPanel btnPanelTop = new JPanel(new FlowLayout());
        btnPanelTop.add(btnInfo);
        btnPanelTop.add(btnactivate);

        btnPanel.add(btnPanelTop, BorderLayout.NORTH);
        btnPanel.add(seat_info);

        //편집 완료 버튼
        JButton btnEditFinish = new JButton("완료");

        // 자리 생성 메소드후 이것저것
        btnGenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seat_panel.removeAll(); // 필요없긴함 필요한가?

                int inputx = Integer.parseInt(textFieldx.getText());
                int inputy = Integer.parseInt(textFieldy.getText());

                seat_panel.setLayout(new GridLayout(inputx, inputy, 5, 5));
                add(editPanel, BorderLayout.NORTH); // 여기서 2번째 인수 없으면 배치 안되서 있어야됨 아마도
                add(seat_panel, BorderLayout.CENTER);

                JPanel[] btnPanels = new JPanel[inputx * inputy];
                JButton[] btnInfos = new JButton[inputx * inputy];
                JButton[] btnActivates = new JButton[inputx * inputy];
                JTextArea[] seat_Infos = new JTextArea[inputx * inputy];

                for (int i = 0; i < btnPanels.length; i++) {

                    //버튼 panel 에 정보, textField 추가
                    btnPanels[i] = new JPanel(new BorderLayout());
                    btnInfos[i] = new JButton("정보보기");
                    btnActivates[i] = new JButton("황성화/비활성화");
                    seat_Infos[i] = new JTextArea((i + 1) + "번 좌석");
                    seat_Infos[i].setEditable(false);

                    //좌석 추가
                    JPanel topPanel = new JPanel(new FlowLayout());
                    topPanel.add(btnInfos[i]);
                    topPanel.add(btnActivates[i]);

                    btnPanels[i].add(topPanel, BorderLayout.NORTH);
                    btnPanels[i].add(seat_Infos[i], BorderLayout.CENTER);
                    seat_panel.add(btnPanels[i]);
                    btnActivates[i].setVisible(false);
                    btnActivates[i].setEnabled(false);

                    //정보보기 버튼 클릭시 정보창 팝업
                    int index = i;
                    btnInfos[index].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Connection conn = PCPosDBConnection.getConnection();
                            MemberDAO memberDAO = new MemberDAO(conn);
                            SeatDAO seatDAO = new SeatDAO(conn);
                            if (conn != null) {
                                MemberDTO member = memberDAO.joinSeat(String.valueOf(index + 1));
                                if (member != null) {
                                    new Seat_UI_InfoFrame(index + 1);

                                } else
                                    JOptionPane.showMessageDialog(null, "사용중이 아닙니다. 정보가 없습니다.");
                            }
                        }
                    });


                }
                //seat table에 자리 개수 만큼 column 생성
                SeatDTO seat = new SeatDTO();
                Connection conn = PCPosDBConnection.getConnection();
                boolean success = true; // 초기화

                for (int i = 0; i < btnPanels.length; i++) {
                    SeatDAO seatDAO = new SeatDAO(conn);
                    seat.setX(inputx);
                    seat.setY(inputy);
                    boolean insertSuccess = seatDAO.insert(seat);
                    if (!insertSuccess) {
                        success = false; // 실패가 발생하면 success를 false로 변경
                        break; // 실패 시 더 이상 반복할 필요가 없으므로 종료
                    }
                }

                if (success) {
                    JOptionPane.showMessageDialog(null, "생성되었습니다."); // 올바른 사용법
                    try {
                        // 현재 창 닫기
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "오류가 발생했습니다."); // 올바른 사용법
                }

                editPanel.setVisible(true);
                inputPanel.setVisible(false);

                //자리 수정, 자리 활성화 | 비활성화 메소드
                //자리 선점중인지 확인하고 활성화 비활성화 체크
                //Todo. 자리 선점
                // 편집 완료 버튼 추가
                btnEdit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        btnEditFinish.setEnabled(true);
                        btnEditFinish.setVisible(true);
                        editPanel.add(btnEditFinish);
                        editPanel.revalidate();
                        editPanel.repaint();

                        for (int i = 0; i < btnPanels.length; i++) {
                            btnActivates[i].setEnabled(true);
                            btnActivates[i].setVisible(true);

                            editPanel.revalidate();
                            editPanel.repaint();

                            int index = i;
                            btnActivates[index].addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {

                                    Connection conn = PCPosDBConnection.getConnection();
                                    SeatDAO seatDAO = new SeatDAO(conn);
                                    SeatDTO is_seat_using = seatDAO.findById(String.valueOf(index + 1));
                                    if(is_seat_using.getSeat_state().equals("사용중")){
                                        JOptionPane.showMessageDialog(null, "사용중입니다. 비활성화 불가능합니다.");
                                    }
                                    else {
                                        boolean t = btnPanels[index].isEnabled();
                                        btnPanels[index].setEnabled(!t);
                                        seat_Infos[index].setEnabled(!t);
                                        btnInfos[index].setEnabled(!t);
                                        JOptionPane.showMessageDialog(null, "변경되었습니다. ");

                                        if(t==true){
                                            SeatDTO seat = new SeatDTO();
                                            seat.setSeat_no(index + 1);
                                            seatDAO.update3(seat);
                                        }
                                        else {
                                            SeatDTO seat = new SeatDTO();
                                            seat.setSeat_no(index + 1);
                                            seatDAO.update4(seat);
                                        }
                                    }
                                }
                            });
                        }

                        //편집 완료 버튼 메소드
                        btnEditFinish.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                for (int i = 0; i < btnPanels.length; i++) {
                                    btnActivates[i].setVisible(false);
                                    btnActivates[i].setEnabled(false);
                                }

                                btnEditFinish.setEnabled(false);
                                btnEditFinish.setVisible(false);
                            }
                        });
                    }
                });
            }
        });

        //버튼 초기화 메소드
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //입력값을 초기화 해야되는구나
                textFieldx.setText("");
                textFieldy.setText("");
                seat_panel.removeAll();
                inputPanel.setVisible(true);
                editPanel.setVisible(false);
                revalidate(); // ui 갱신
                repaint();

                //자리 초기화 deleteAll 메소드
                Connection conn = PCPosDBConnection.getConnection();
                SeatDAO seatDAO = new SeatDAO(conn);
                ArrayList<SeatDTO> seats = seatDAO.choose_using();
                try{
                    if (seats != null)
                        throw new UsingSeatsException();

                    if (conn != null) {
                        boolean success = seatDAO.deleteAll();
                        seatDAO.resetAuto_increment();
                        if (success) {
                            JOptionPane.showMessageDialog(null, "초기화 완료");
                            try {

                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "db연결 시패");
                        }
                    }

                }catch (UsingSeatsException ea){
                    JOptionPane.showMessageDialog(null, "사용중인 좌석이 있습니다. 초기화가 불가능합니다.");
                }
            }
        });

        setVisible(true);
        setSize(1600, 900);
    }



    public static void main(String[] args) {
        new Seat_UI();
    }
}
