package UI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Sales_UI extends JPanel {
    public Sales_UI() {
        setLayout(new BorderLayout());

        // 상단 버튼
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton daybtn = new JButton("현재매출(일일)");
        JButton monthbtn = new JButton("월별매출");
        JButton searchbtn = new JButton("매출조회 및 거래취소");

        btnPanel.add(daybtn);
        btnPanel.add(monthbtn);
        btnPanel.add(searchbtn);

        add(btnPanel, BorderLayout.NORTH);

        String[] columnNames = {"결제일시", "상품", "가격", "결제방법", "합계"};
        Object[][] data = {};  // 초기데이터 빈값

        // 중앙 데이터 테이블
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVisible(false); // 처음엔 숨김
        add(scrollPane, BorderLayout.CENTER);

        // 버튼 클릭이벤트
        daybtn.addActionListener(e -> {
            Object[][] sampleData = {
                    {"2024-11-16 12:34", "상품 A", "10000", "카드", "10000"},
                    {"2024-11-16 13:45", "상품 B", "20000", "현금", "20000"}
            };
            // 데이터 갱신
            table.setModel(new javax.swing.table.DefaultTableModel(
                    sampleData,columnNames
            ));
            // 숨겨놧던 테이블 보이기
            scrollPane.setVisible(true);
            revalidate();
            repaint();
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("매출 관리");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,600);
        frame.add(new Sales_UI());
        frame.setVisible(true);
    }
}
