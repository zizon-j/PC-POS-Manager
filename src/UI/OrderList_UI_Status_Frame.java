package UI;

import Login.MessageDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class OrderList_UI_Status_Frame extends JFrame {

    private List<Order> orderList;
    private DefaultTableModel newOrderTableModel;
    private DefaultTableModel preparingOrderTableModel;
    private DefaultTableModel completedOrderTableModel;
    private DefaultTableModel cancelledOrderTableModel;

    public OrderList_UI_Status_Frame(List<Order> sharedOrders) {
        // 데이터 공유 및 기본 설정
        this.orderList = sharedOrders;
        setTitle("현재 주문 현황");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // TabbedPane 생성
        JTabbedPane tabbedPane = new JTabbedPane();

        // 탭 생성 및 추가
        tabbedPane.addTab("신규 주문", createOrderTab("준비 중", "주문 취소", "주문 대기", newOrderTableModel = new DefaultTableModel()));
        tabbedPane.addTab("준비 중", createOrderTab("결제 완료", "주문 취소", "준비 중", preparingOrderTableModel = new DefaultTableModel()));
        tabbedPane.addTab("결제 완료", createOrderTab(null, null, "결제 완료", completedOrderTableModel = new DefaultTableModel()));
        tabbedPane.addTab("주문 취소", createOrderTab(null, null, "주문 취소", cancelledOrderTableModel = new DefaultTableModel()));

        add(tabbedPane, BorderLayout.CENTER);

        // 테이블 데이터 초기화
        refreshOrderTables();
    }

    private JPanel createOrderTab(String firstButtonText, String secondButtonText, String orderStatus, DefaultTableModel tableModel) {
        JPanel tabPanel = new JPanel(new BorderLayout());
        JTable orderTable = new JTable(tableModel);

        // 테이블 설정
        tableModel.setColumnIdentifiers(new String[]{"주문 번호", "회원 ID", "좌석 번호", "상품", "가격", "결제 방법", "상태", "결재 일시"});
        JScrollPane scrollPane = new JScrollPane(orderTable);
        tabPanel.add(scrollPane, BorderLayout.CENTER);

        // 버튼 패널
        if (firstButtonText != null || secondButtonText != null) {
            JPanel buttonPanel = new JPanel();

            if (firstButtonText != null) {
                JButton firstButton = new JButton(firstButtonText);
                firstButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        updateOrderStatus(orderTable, firstButtonText);
                    }
                });
                buttonPanel.add(firstButton);
            }

            if (secondButtonText != null) {
                JButton secondButton = new JButton(secondButtonText);
                secondButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        updateOrderStatus(orderTable, secondButtonText);
                    }
                });
                buttonPanel.add(secondButton);
            }

            tabPanel.add(buttonPanel, BorderLayout.SOUTH);
        }

        return tabPanel;
    }

    private void updateOrderStatus(JTable orderTable, String newStatus) {
        int selectedRow = orderTable.getSelectedRow();

        // 선택된 행이 없는 경우 MessageDialog 사용
        if (selectedRow == -1) {
            MessageDialog messageDialog = new MessageDialog(this, "알림", true, "먼저 주문을 선택하세요.");
            messageDialog.setLocationRelativeTo(this);
            messageDialog.setVisible(true);
            return;
        }

        // 주문 ID로 상태 변경
        String orderId = orderTable.getValueAt(selectedRow, 0).toString();
        for (Order order : orderList) {
            if (order.getOrderID().equals(orderId)) {
                order.setStatus(newStatus);
                break;
            }
        }

        // 테이블 업데이트
        refreshOrderTables();
    }

    private void refreshOrderTables() {
        // 모든 테이블 초기화
        newOrderTableModel.setRowCount(0);
        preparingOrderTableModel.setRowCount(0);
        completedOrderTableModel.setRowCount(0);
        cancelledOrderTableModel.setRowCount(0);

        // 상태별 데이터 추가
        for (Order order : orderList) {
            String[] rowData = {order.getOrderID(), order.getMemberID(), order.getSeatNumber(), order.getProduct(), order.getPrice(), order.getPaymentMethod(), order.getStatus(), order.getTime()};

            switch (order.getStatus()) {
                case "주문 대기":
                    newOrderTableModel.addRow(rowData);
                    break;
                case "준비 중":
                    preparingOrderTableModel.addRow(rowData);
                    break;
                case "결제 완료":
                    completedOrderTableModel.addRow(rowData);
                    break;
                case "주문 취소":
                    cancelledOrderTableModel.addRow(rowData);
                    break;
            }
        }
    }
    public static class Order {
        private String orderID;
        private String memberID;
        private String seatNumber;
        private String product;
        private String price;
        private String paymentMethod;
        private String status;
        private String time;

        public Order(String orderID, String memberID, String seatNumber, String product, String price, String paymentMethod, String status, String time) {
            this.orderID = orderID;
            this.memberID = memberID;
            this.seatNumber = seatNumber;
            this.product = product;
            this.price = price;
            this.paymentMethod = paymentMethod;
            this.status = status;
            this.time = time;
        }

        public String getOrderID() {
            return orderID;
        }

        public String getMemberID() {
            return memberID;
        }

        public String getSeatNumber() {
            return seatNumber;
        }

        public String getProduct() {
            return product;
        }

        public String getPrice() {
            return price;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTime() {
            return time;
        }

    }
}
