package Common_Panel;

import DAO.MemberDAO;
import DAO.TimeDAO;
import DAO.Time_Plus_LogDAO;
import DTO.MemberDTO;
import DTO.TimeDTO;
import DTO.Time_Plus_LogDTO;
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
                String selected_money = null;
                selected_money = money_select.getSelectedItem().toString();

                TimeDAO timeDAO = new TimeDAO(conn);
                MemberDAO memberDAO = new MemberDAO(conn);
                Time_Plus_LogDAO Time_Plus_LogDAO = new Time_Plus_LogDAO(conn);

                MemberDTO member = memberDAO.findById(id_search_field.getText());
                TimeDTO time = timeDAO.findById(selected_money);


                if(member != null){
                    memberDAO.update_left_time(member, time);

                    Time_Plus_LogDTO time_plus = new Time_Plus_LogDTO();
                    time_plus.setMember_id(member.getMember_id());
                    time_plus.setMoney(time.getMoney());

                    Time_Plus_LogDAO.insert(time_plus);

                    JOptionPane.showMessageDialog(null, time.getPlus_time()+"분 추가 되었습니다.");


                }else
                    JOptionPane.showMessageDialog(null, "없는 ID 입니다.");


            }
        });

    }

}
