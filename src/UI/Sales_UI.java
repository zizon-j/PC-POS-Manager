package UI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Sales_UI extends JPanel {
    public Sales_UI() {
        setLayout(new BorderLayout());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton daybtn = new JButton("현재매출(일일)");
        JButton monthbtn = new JButton("월별매출");
        JButton searchbtn = new JButton("매출조회 및 거래취소");

        btnPanel.add(daybtn);
        btnPanel.add(monthbtn);
        btnPanel.add(searchbtn);

        add(btnPanel, BorderLayout.NORTH);
    }
}
