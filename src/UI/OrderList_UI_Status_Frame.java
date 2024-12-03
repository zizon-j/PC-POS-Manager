package UI;

import DAO.OrderDAO;
import DTO.OrderDTO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

/**
 * 주문 현황을 상태별로 보여주는 창
 * 주문 대기, 준비 중, 완료, 취소 상태를 탭으로 구분하여 표시
 * 각 상태별로 주문 상태 변경 가능
 */
public class OrderList_UI_Status_Frame extends JFrame {
    // 각 상태별 테이블 컴포넌트
    private JTable waitingTable; // 주문 대기 테이블
    private JTable preparingTable; // 준비 중 테이블
    private JTable completedTable; // 완료된 주문 테이블
    private JTable cancelledTable; // 취소된 주문 테이블

    // 각 테이블의 데이터 모델
    private DefaultTableModel waitingModel;
    private DefaultTableModel preparingModel;
    private DefaultTableModel completedModel;
    private DefaultTableModel cancelledModel;

    // 데이터 처리 관련
    private OrderDAO orderDAO; // 주문 데이터 처리 객체
    private List<OrderDTO> allOrders; // 전체 주문 목록

    // 생성자: 주문 현황 창 초기화
    public OrderList_UI_Status_Frame(Connection conn) {
        // 창 기본 설정
        setTitle("주문 현황");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        orderDAO = new OrderDAO(conn);
        makeUI(); // UI 구성
        refreshData(); // 초기 데이터 로드
    }

    // UI 구성요소 생성 및 배치
    private void makeUI() {
        JTabbedPane tabs = new JTabbedPane();

        // 각 상태별 탭 추가
        tabs.addTab("주문 대기", makeOrderPanel("주문 대기", true));
        tabs.addTab("준비 중", makeOrderPanel("준비 중", true));
        tabs.addTab("완료", makeOrderPanel("결제 완료", false));
        tabs.addTab("취소", makeOrderPanel("주문 취소", false));

        add(tabs);
    }

    // 각 상태별 주문 패널 생성
    private JPanel makeOrderPanel(String status, boolean needButtons) {
        JPanel panel = new JPanel(new BorderLayout());

        // 테이블 생성
        DefaultTableModel model = new DefaultTableModel(
                new String[] { "주문번호", "회원ID", "좌석번호", "주문내역", "금액", "요청사항", "결제방법", "상태", "주문시간" }, 0);

        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        saveTableReference(status, table, model);

        // 상태 변경 버튼이 필요한 경우에만 추가
        if (needButtons) {
            panel.add(makeButtonPanel(table, status), BorderLayout.SOUTH);
        }

        return panel;
    }

    // 테이블과 모델 참조 저장
    private void saveTableReference(String status, JTable table, DefaultTableModel model) {
        switch (status) {
            case "주문 대기":
                waitingTable = table;
                waitingModel = model;
                break;
            case "준비 중":
                preparingTable = table;
                preparingModel = model;
                break;
            case "결제 완료":
                completedTable = table;
                completedModel = model;
                break;
            case "주문 취소":
                cancelledTable = table;
                cancelledModel = model;
                break;
        }
    }

    // 상태 변경 버튼 패널 생성
    private JPanel makeButtonPanel(JTable table, String status) {
        JPanel buttonPanel = new JPanel();

        // 현재 상태에 따라 가능한 상태 변경 버튼 추가
        if ("주문 대기".equals(status)) {
            addStateChangeButton(buttonPanel, table, "준비 중");
            addStateChangeButton(buttonPanel, table, "주문 취소");
        } else if ("준비 중".equals(status)) {
            addStateChangeButton(buttonPanel, table, "결제 완료");
            addStateChangeButton(buttonPanel, table, "주문 취소");
        }

        return buttonPanel;
    }

    // 상태 변경 버튼 추가
    private void addStateChangeButton(JPanel panel, JTable table, String newStatus) {
        JButton button = new JButton(newStatus);
        button.addActionListener(e -> changeOrderStatus(table, newStatus));
        panel.add(button);
    }

    // 주문 상태 변경
    private void changeOrderStatus(JTable table, String newStatus) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "주문을 선택해주세요!");
            return;
        }

        try {
            int orderNo = Integer.parseInt(table.getValueAt(row, 0).toString());
            if (orderDAO.updateOrderState(orderNo, newStatus)) {
                refreshData(); // 상태 변경 성공시 데이터 새로고침
            } else {
                JOptionPane.showMessageDialog(this, "상태 변경 실패!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "오류 발생!");
        }
    }

    // 모든 테이블 데이터 새로고침
    private void refreshData() {
        try {
            allOrders = orderDAO.findOrdersWithDetails();
            clearAllTables();

            // 각 주문을 해당하는 상태의 테이블에 추가
            for (OrderDTO order : allOrders) {
                addOrderToTable(order);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "데이터 로드 실패!");
        }
    }

    // 모든 테이블 초기화
    private void clearAllTables() {
        waitingModel.setRowCount(0);
        preparingModel.setRowCount(0);
        completedModel.setRowCount(0);
        cancelledModel.setRowCount(0);
    }

    // 주문을 해당하는 상태의 테이블에 추가
    private void addOrderToTable(OrderDTO order) {
        Object[] rowData = {
                order.getOrder_no(),
                order.getMember_id(),
                order.getSeat_no(),
                order.getProductDetails(),
                order.getTotal_price(),
                order.getOrder_request(),
                order.getPayment_type(),
                order.getOrder_state(),
                order.getOrder_time()
        };

        // 주문 상태에 따라 해당하는 테이블에 추가
        switch (order.getOrder_state()) {
            case "주문 대기":
                waitingModel.addRow(rowData);
                break;
            case "준비 중":
                preparingModel.addRow(rowData);
                break;
            case "결제 완료":
                completedModel.addRow(rowData);
                break;
            case "주문 취소":
                cancelledModel.addRow(rowData);
                break;
        }
    }
}
