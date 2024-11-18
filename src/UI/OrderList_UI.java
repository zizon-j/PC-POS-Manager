package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class OrderList_UI extends JPanel {

    private DefaultTableModel tableModel;
    private List<OrderStatusFrame.Order> orders;

    public OrderList_UI() {
        setLayout(new BorderLayout());

        // 초기 주문 데이터 생성
        orders = createSampleOrders();

        // 상단 패널 (검색 및 주문 현황 버튼)
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchTextField = new JTextField(15);
        JButton searchBtn = new JButton("검색");
        JButton currentOrdersBtn = new JButton("현재 주문 현황");

        headerPanel.add(searchTextField);
        headerPanel.add(searchBtn);
        headerPanel.add(currentOrdersBtn);
        add(headerPanel, BorderLayout.NORTH);

        // 테이블의 컬럼 이름들
        String[] tableColumns = {"주문 번호", "회원 ID", "좌석 번호", "상품", "가격", "결제 방법", "상태"};

        tableModel = new DefaultTableModel(tableColumns, 0);
        JTable orderTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(orderTable);
        add(tableScrollPane, BorderLayout.CENTER);

        loadOrdersToTable();

        // 검색 버튼 동작 정의
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = searchTextField.getText().toLowerCase();

                // 기존 데이터 초기화
                tableModel.setRowCount(0);

                // 데이터 필터 부분
                for (OrderStatusFrame.Order order : orders) {
                    if (keyword.isEmpty() || order.getProduct().toLowerCase().contains(keyword)) {
                        tableModel.addRow(new Object[]{order.getOrderID(), order.getMemberID(), order.getSeatNumber(), order.getProduct(), order.getPrice(), order.getPaymentMethod(), order.getStatus()});
                    }
                }
            }
        });

        // 현재 주문 현황 버튼 동작 정의
        currentOrdersBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // OrderStatusFrame 열기
                OrderStatusFrame orderStatusFrame = new OrderStatusFrame(orders);
                orderStatusFrame.setVisible(true);
            }
        });
    }
    
        // 추후 데이터베이스 연동 시 수정
    private List<OrderStatusFrame.Order> createSampleOrders() {
        List<OrderStatusFrame.Order> sampleOrders = new ArrayList<>();

        // 주문 1
        sampleOrders.add(new OrderStatusFrame.Order("001", "user123", "1", "짜파게티, 웰치스 포도", "5,000원", "카드", "주문 대기"));

        // 주문 2
        sampleOrders.add(new OrderStatusFrame.Order("002", "user456", "2", "매콤라볶이, 꿀복숭아에이드", "5,000원", "카드", "주문 대기"));

        // 주문 3
        sampleOrders.add(new OrderStatusFrame.Order("003", "user789", "3", "매콤닭갈비덮밥", "5,000원", "현금", "주문 대기"));

        return sampleOrders;
    }

    private void loadOrdersToTable() {
        for (OrderStatusFrame.Order order : orders) {
            tableModel.addRow(new Object[]{order.getOrderID(), order.getMemberID(), order.getSeatNumber(), order.getProduct(), order.getPrice(), order.getPaymentMethod(), order.getStatus()});
        }
    }
}