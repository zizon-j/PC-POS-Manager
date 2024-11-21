package UI;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Login_MessageDialog extends JDialog implements ActionListener {
    JButton ok; // "OK" 버튼을 위한 JButton 객체

    // 생성자: MessageDialog 객체를 초기화하고 구성 요소를 설정
    public Login_MessageDialog(JFrame parent, String title, boolean modal, String msg) {
        super(parent, title, modal); // 부모 프레임, 제목, 모달 여부를 설정
        JPanel pc = new JPanel(); // 메시지를 표시할 패널 생성
        JLabel label = new JLabel(msg); // 전달받은 메시지를 JLabel로 생성
        pc.add(label); // 패널에 레이블 추가
        add(pc, BorderLayout.CENTER); // 중앙에 패널 추가

        JPanel ps = new JPanel(); // 버튼을 위한 패널 생성
        ok = new JButton("OK"); // "OK" 버튼 생성
        ok.addActionListener(this); // 버튼 클릭 시 actionPerformed 메서드 호출
        ps.add(ok); // 패널에 버튼 추가
        add(ps, BorderLayout.SOUTH); // 하단에 패널 추가

        pack(); // 다이얼로그 크기를 내용에 맞게 조정
    }

    // 버튼 클릭 시 호출되는 메서드
    public void actionPerformed(ActionEvent ae) {
        dispose(); // 다이얼로그를 닫음
    }
}
