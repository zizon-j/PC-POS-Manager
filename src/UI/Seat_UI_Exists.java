package UI;

import Common_Panel.Time_Plus_Jpanel;
import DAO.MemberDAO;
import DAO.SeatDAO;
import DTO.MemberDTO;
import DTO.SeatDTO;
import Jdbc.PCPosDBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
class UsingSeatsException2 extends Exception{}

public class Seat_UI_Exists extends JPanel {
    public JPanel editPanel;
    public JButton btnEdit, btnReset;

    public Seat_UI_Exists(JFrame parentFrame) { //JFrame 참조 추가

        //생성 버튼 필요업이 바로 좌석 시트로 감
        setLayout(new BorderLayout());

        // 자리 배치창
        editPanel = new JPanel(); // 버튼 편집 버튼 묶기
        editPanel.setLayout(new FlowLayout());

        //시간 추가 패널
        Time_Plus_Jpanel time_plus_jpanel = new Time_Plus_Jpanel();
        editPanel.add(time_plus_jpanel);

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

        seat_panel.removeAll(); // 필요없긴함 필요한가?

        //좌석 x, y 정보 가져오기
        Connection conn = PCPosDBConnection.getConnection();
        SeatDAO seatDAO = new SeatDAO(conn);
        SeatDTO seatXY = seatDAO.findById("1");

        int inputx = seatXY.getX();
        int inputy = seatXY.getY();

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

            //활성화 비활성화 구분
            if (seatDAO.findById(String.valueOf(i+1)).getSeat_state().equals("비활성화")){
                btnPanels[i].setEnabled(false);
                seat_Infos[i].setEnabled(false);
                btnInfos[i].setEnabled(false);
            }

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

        editPanel.setVisible(true);

        //자리 수정, 자리 활성화 | 비활성화 메소드
        //자리 선점중인지 확인하고 활성화 비활성화 체크
        //Todo완료 자리 선점
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

        //버튼 초기화 메소드
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //입력값을 초기화 해야되는구나
                revalidate(); // ui 갱신
                repaint();

                //자리 초기화 deleteAll 메소드
                Connection conn = PCPosDBConnection.getConnection();
                SeatDAO seatDAO = new SeatDAO(conn);
                ArrayList<SeatDTO> seats = seatDAO.choose_using();
                try {
                    if (seats != null)
                        throw new UsingSeatsException2();

                    if (conn != null) {
                        boolean success = seatDAO.deleteAll();
                        seatDAO.resetAuto_increment();

                        if (success) {
                            JOptionPane.showMessageDialog(null, "초기화 완료");
                            parentFrame.getContentPane().removeAll();
                            parentFrame.add(new Seat_UI());
                            parentFrame.revalidate();
                            parentFrame.repaint();
                            try {

                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "db연결 시패");
                        }
                    }
                } catch (UsingSeatsException2 ex) {
                    JOptionPane.showMessageDialog(null, "사용중인 좌석이 있습니다. 초기화가 불가능합니다.");
                }
            }
            });

        add(editPanel, BorderLayout.NORTH);
        add(seat_panel, BorderLayout.CENTER);

        setVisible(true);
        setSize(1600, 900);
    }

    public static void main(String[] args) {
//    new Seat_UI_Exists();
    }
}
