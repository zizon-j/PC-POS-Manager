package UI;

import DAO.OrderDAO;
import DAO.ProductDAO;
import DTO.OrderDTO;
import DTO.ProductDTO;
import DTO.SalesDTO;
import Jdbc.PCPosDBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public class Sales_UI extends JPanel {

    private JScrollPane scrollPane;
    private JTable table;
    private Sales_UI_Month smonth_UI;
    private JPanel centerPanel;
    private CardLayout cardLayout;
    private JButton deleteButton;


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



        // 월별매출 UI 패널 추가
        smonth_UI = new Sales_UI_Month();
        centerPanel.add(smonth_UI, "Calendar");

        add(centerPanel, BorderLayout.CENTER);

        // 초기화면은 비어있는 화면으로
        cardLayout.show(centerPanel, "Empty");

        // 삭제 버튼 추가
        deleteButton = new JButton("결제 취소");
        deleteButton.setEnabled(false); // 초기 비활성화
        deleteButton.addActionListener(e -> deleteRows());

        // 삭제버튼을 패널에 추가
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(deleteButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // 버튼 이벤트
        daybtn.addActionListener(e -> showTable());
        monthbtn.addActionListener(e -> showCalendar());
        searchbtn.addActionListener(e -> {
            // 기간설정 다이얼로그
            Sales_UI_sd sd = new Sales_UI_sd((JFrame) SwingUtilities.getWindowAncestor(this));
            sd.setVisible(true);

            // 날짜 선택
            LocalDate[] dates = sd.getSelectedDates();
            if (dates[0] != null && dates[1] != null) {
                LocalDate startDate = dates[0];
                LocalDate endDate = dates[1];
                exSalesData(startDate, endDate);
            }
        });
    }

    // 검색
    private void exSalesData(LocalDate startDate, LocalDate endDate) {
        // 샘플 데이터
        Object[][] allData = {
                {"2024-11-16 12:34", "상품 A", "10000", "카드", "10000"},
                {"2024-11-15 13:45", "상품 B", "20000", "현금", "30000"},
                {"2024-11-10 11:20", "상품 C", "15000", "카드", "45000"}
        };

        // 필터링된 데이터
        java.util.List<Object[]> filteredData = new java.util.ArrayList<>();
        for (Object[] row : allData) {
            LocalDate saleDate = LocalDate.parse(((String) row[0]).split(" ")[0]);
            if (!saleDate.isBefore(startDate) && !saleDate.isAfter(endDate)) {
                filteredData.add(row);
            }
        }

        // 테이블 갱신
        table.setModel(new javax.swing.table.DefaultTableModel(
                filteredData.toArray(new Object[0][]),
                new String[]{"결제일시", "상품", "가격", "결제방법", "합계"}
        ));

        deleteButton.setEnabled(true);
        cardLayout.show(centerPanel, "Table");
    }

    // 테이블에서 선택된 매출 삭제
    private void deleteRows() {
        int[] selectedRows = table.getSelectedRows();

        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "취소할 결제를 선택하세요.");
            return;
        }

        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) table.getModel();
        for (int i = selectedRows.length - 1; i >= 0; i--) {
            model.removeRow(selectedRows[i]);
        }
    }

    // 일별매출
    private void showTable() {

         DefaultTableModel model;

//        Object[][] sampleData = {
//                {"2024-11-16 12:34", "상품 A", "10000", "카드", "10000"},
//                {"2024-11-16 13:45", "상품 B", "20000", "현금", "30000"}
//        };
//        // 데이터 갱신
//        table.setModel(new javax.swing.table.DefaultTableModel(
//                sampleData, new String[]{"결제일시" , "상품", "가격", "결제방법", "합계"}
//        ));
//
//        deleteButton.setEnabled(false);
//        cardLayout.show(centerPanel, "Table");

        // 중앙 데이터 테이블


        // 테이블 상품 표시
        String[] columns = {"결제일" , "상품", "가격", "결제방법", "합계"};
        model = new DefaultTableModel(columns, 0); //테이블 모델 초기화

        Connection conn = PCPosDBConnection.getConnection();
        OrderDAO orderDAO = new OrderDAO(conn);
        List<SalesDTO> orders = orderDAO.findFinsih();

        if (orders != null) {
            for (SalesDTO o : orders) {
                Object[] row = {
                        o.getOrder_time(),
                        o.getOrder_no(),
                        o.getTotal_price(),
                        o.getPayment_type(),
                        o.getTotal_sum()
                };
                model.addRow(row);// 테이블에 행 추가
            }
        }
        table = new JTable(model);
        scrollPane = new JScrollPane(table);
        centerPanel.add(scrollPane, "Table");

        deleteButton.setEnabled(false);
        cardLayout.show(centerPanel, "Table");


    }

    // 월별매출
    private void showCalendar() {
        deleteButton.setEnabled(false); // 매출취소버튼 비활성화
        cardLayout.show(centerPanel, "Calendar");
    }
}
