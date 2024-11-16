package UI;

import javax.swing.*;
import java.awt.*;

public class Sales_UI extends JPanel {

    private JScrollPane scrollPane;
    private JTable table;
    private S_month_UI smonth_UI;
    private JPanel centerPanel;
    private CardLayout cardLayout;

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

        // 중앙 CardLayout 패널생성
        cardLayout = new CardLayout();
        centerPanel = new JPanel(cardLayout);

        // 초기화면 (아무것도 없는 화면)
        JPanel emptyPanel = new JPanel();
        centerPanel.add(emptyPanel, "Empty");

        // 중앙 데이터 테이블
        table = new JTable(data, columnNames);
        scrollPane = new JScrollPane(table);
        centerPanel.add(scrollPane, "Table");

        // 월별매출 UI 패널 추가
        smonth_UI = new S_month_UI();
        centerPanel.add(smonth_UI, "Calendar");

        add(centerPanel, BorderLayout.CENTER);

        // 초기화면은 비어있는 화면으로
        cardLayout.show(centerPanel, "Empty");

        // 버튼 이벤트
        daybtn.addActionListener(e -> showTable());
        monthbtn.addActionListener(e -> showCalendar());
    }

    // 일별매출
    private void showTable() {
        Object[][] sampleData = {
                {"2024-11-16 12:34", "상품 A", "10000", "카드", "10000"},
                {"2024-11-16 13:45", "상품 B", "20000", "현금", "30000"}
        };
        // 데이터 갱신
        table.setModel(new javax.swing.table.DefaultTableModel(
                sampleData, new String[]{"결제일시" , "상품", "가격", "결제방법", "합계"}
        ));

        cardLayout.show(centerPanel, "Table");
    }

    private void showCalendar() {
        cardLayout.show(centerPanel, "Calendar");
    }
}
