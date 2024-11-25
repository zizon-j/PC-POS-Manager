package Common_Panel;

import DAO.MemberDAO;
import DAO.TimeDAO;
import DTO.MemberDTO;
import DTO.TimeDTO;
import Jdbc.PCPosDBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class Time_Plus_Jpanel extends JPanel {

    //TODO
    // - 시간 추가 패널 만들기
    // - 좌석 ui jtextarea로 다시 만들기
    // - 조회 DAO
    // memberupdateDAO
    // time_plus_log table 생성

    JTextField id_search_field;
    JComboBox money_select;
    String[] money_list= {"1000","2000","5000","10000"};
    JButton money_plus_btn;

    public Time_Plus_Jpanel(){

        Connection conn = PCPosDBConnection.getConnection();


        setLayout(new FlowLayout());


        id_search_field = new JTextField(10);


        add(id_search_field);

        money_select = new JComboBox(money_list);
        add(money_select);

        money_plus_btn = new JButton("시간 추가");
        add(money_plus_btn);

        money_plus_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TimeDAO timeDAO = new TimeDAO(conn);
                MemberDAO memberDAO = new MemberDAO(conn);

                MemberDTO member = memberDAO.findById(id_search_field.getText());
                TimeDTO time = timeDAO.findById((String) money_select.getSelectedItem());

                if(member != null){
                    memberDAO.update_left_time(member, time);
                    JOptionPane.showMessageDialog(null, time.getPlus_time()+"분 추가 되었습니다.");


                }else
                    JOptionPane.showMessageDialog(null, "없는 ID 입니다.");


            }
        });

    }

}
