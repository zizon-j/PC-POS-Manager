package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Seat_UI extends JPanel{
    JButton[] seat = new JButton[100];

    GridBagLayout grid = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
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
        seat[3].setBounds(248, 20, 76, 59);
        seat[4].setBounds(324,20,76,59);
        seat[5].setBounds(400, 20, 76, 59);
        seat[6].setBounds(476, 20, 76, 59);
        seat[7].setBounds(552,20,76,59);
        seat[8].setBounds(628, 20, 76, 59);

        seat[9].setBounds(20, 79, 76, 59);
        seat[10].setBounds(96,79,76,59);
        seat[11].setBounds(172,79,76,59);
        seat[12].setBounds(248, 79, 76, 59);
        seat[13].setBounds(324,79,76,59);
        seat[14].setBounds(400, 79, 76, 59);
        seat[15].setBounds(476, 79, 76, 59);
        seat[16].setBounds(552,79,76,59);
        seat[17].setBounds(628, 79, 76, 59);

        seat[18].setBounds(20, 171, 76,59);
        seat[19].setBounds(96,171,76,59);
        seat[11].setBounds(172,171,76,59);
        seat[12].setBounds(248, 171, 76, 59);
        seat[13].setBounds(324,171,76,59);
        seat[14].setBounds(400, 171, 76, 59);
        seat[15].setBounds(476, 171, 76, 59);
        seat[16].setBounds(552,171,76,59);
        seat[17].setBounds(628, 171, 76, 59);

        seat[18].setBounds(20, 230, 76,59);
        seat[19].setBounds(96,230,76,59);
        seat[20].setBounds(172,230,76,59);

        seat[21].setBounds(476, 322, 76, 59);
        seat[22].setBounds(552,322,76,59);
        seat[23].setBounds(628, 322, 76, 59);

        seat[24].setBounds(20, 381, 76,59);
        seat[25].setBounds(96,381,76,59);
        seat[26].setBounds(172,381,76,59);
        seat[27].setBounds(248, 381, 76, 59);
        seat[28].setBounds(324,381,76,59);
        seat[29].setBounds(400, 381, 76, 59);
        seat[30].setBounds(476, 381, 76, 59);
        seat[31].setBounds(552,381,76,59);
        seat[32].setBounds(628, 381, 76, 59);

        add(seat[0]);
        add(seat[1]);
        add(seat[2]);
        add(seat[3]);
        add(seat[4]);
        add(seat[5]);
        add(seat[6]);
        add(seat[7]);
        add(seat[8]);

        add(seat[9]);
        add(seat[10]);
        add(seat[11]);
        add(seat[12]);
        add(seat[13]);
        add(seat[14]);
        add(seat[15]);
        add(seat[16]);
        add(seat[17]);

        add(seat[18]);
        add(seat[19]);
        add(seat[20]);

        add(seat[21]);
        add(seat[22]);
        add(seat[23]);

        add(seat[24]);
        add(seat[25]);
        add(seat[26]);
        add(seat[27]);
        add(seat[28]);
        add(seat[29]);
        add(seat[30]);
        add(seat[31]);
        add(seat[32]);

        for (int i = 0; i<seat.length; i++){
            seat[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new InfoFrame();

                }
            });
        }


        setVisible(true);
        setSize(1600,900);

    }


    public static void main(String[] args) {
        new Seat_UI();
    }
}
