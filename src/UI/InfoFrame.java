package UI;

import javax.swing.*;
public class InfoFrame extends JFrame {

    public InfoFrame(){

        JLabel name = new JLabel("이름: ");

        add(name);


        setLocationRelativeTo(null);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
    public static void main(String[] args) {
        new InfoFrame();
    }
}
