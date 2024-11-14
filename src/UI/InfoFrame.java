package UI;

import javax.swing.*;
import java.awt.*;

public class InfoFrame extends JFrame {

    public InfoFrame(){

        JLabel name = new JLabel("이름: ");

        add(name);

        setSize(200, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
    public static void main(String[] args) {
        new InfoFrame();
    }
}
