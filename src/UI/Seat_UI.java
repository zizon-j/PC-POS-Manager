package UI;

import javax.swing.*;
import java.awt.*;

public class Seat_UI extends JPanel{
    JButton[] seat = new JButton[100];
    public  Seat_UI(){
        for (int i = 0; i <seat.length; i++){
            seat[i] = new JButton(Integer.toString(i+1));
            }

    //setBounds(가로위치, 세로위치, 가로길이, 세로길이);
        //좌석 버튼 크리 76 x 59
        setLayout(null);
        seat[0].setBounds(20, 20, 76, 59);
        seat[1].setBounds(96, 20, 76, 59);
        seat[2].setBounds(172, 20, 76, 59);
        seat[3].setBounds(96, 79, 76, 59);

        add(seat[0]);
        add(seat[1]);
        add(seat[2]);
        add(seat[3]);
        add(seat[4]);
        add(seat[5]);
        add(seat[6]);
        add(seat[7]);

        setVisible(true);
        setSize(1600,900);

    }
    public static void main(String[] args) {
        new Seat_UI();
    }
}
