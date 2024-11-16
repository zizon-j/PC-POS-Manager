package UI;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class MessageDialog extends JDialog implements ActionListener {
    JButton ok;

    public MessageDialog(JFrame parent, String title, boolean modal, String msg) {
        super(parent, title, modal);
        JPanel pc = new JPanel();
        JLabel label = new JLabel(msg);
        pc.add(label);
        add(pc, BorderLayout.CENTER);

        JPanel ps = new JPanel();
        ok = new JButton("OK");
        ok.addActionListener(this);
        ps.add(ok);
        add(ps, BorderLayout.SOUTH);

        pack();
    }

    public void actionPerformed(ActionEvent ae) {
        dispose();
    }
}
