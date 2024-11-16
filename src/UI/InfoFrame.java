package UI;

import javax.swing.*;
import java.awt.*;

public class InfoFrame extends JFrame {

    public InfoFrame(){

        setTitle("좌석정보");
        JPanel seat_Info = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel seat_0 = new JLabel("좌석번호: ");
        JLabel seat_1 = new JLabel();
        seat_Info.add(seat_0);
        seat_Info.add(seat_1);

        JPanel name_Info = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel name_0 = new JLabel("이름: ");
        JLabel name_1 = new JLabel();
        name_Info.add(name_0);
        name_Info.add(name_1);

        JPanel nickname_Info = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel nickname_0 = new JLabel("닉네임: ");
        JLabel nickname_1 = new JLabel();
        nickname_Info.add(nickname_0);
        nickname_Info.add(nickname_1);

        JPanel usedTime_Info = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel usedTime_0 = new JLabel("사용 시간: ");
        JLabel usedTime_1 = new JLabel();
        usedTime_Info.add(usedTime_0);
        usedTime_Info.add(usedTime_1);

        JPanel leftTime_Info = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel leftTime_0 = new JLabel("남은 시간: ");
        JLabel leftTime_1 = new JLabel();
        leftTime_Info.add(leftTime_0);
        leftTime_Info.add(leftTime_1);

        JPanel seat_State_Info = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel seat_State_Label = new JLabel("좌석 상태: ");
        JLabel seat_State = new JLabel("사용 중");
        seat_State_Info.add(seat_State_Label);
        seat_State_Info.add(seat_State);



        setLayout(new GridLayout(6,1,0,10));
        add(seat_Info);
        add(name_Info);
        add(nickname_Info);
        add(usedTime_Info);
        add(leftTime_Info);
        add(seat_State_Info);

        Container c = getContentPane();
        c.setFont(new Font("Arial", Font.BOLD, 14));


        setLocationRelativeTo(null);
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
    public static void main(String[] args) {
        new InfoFrame();
    }
}
