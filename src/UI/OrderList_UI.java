package UI;

import DAO.OrderDAO;
import DTO.OrderDTO;
import Jdbc.PCPosDBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

/**
 * 주문 목록을 보여주는 메인 화면
 * 모든 주문을 테이블 형태로 보여주고, 검색 기능과 주문 현황 창을 열 수 있는 기능 제공
 */

public class OrderList_UI extends JPanel {
    // UI 구성요소들
    private JTable orderTable; // 주문 목록을 보여주는 테이블
    private DefaultTableModel tableModel; // 테이블의 데이터 모델
    private JTextField searchTextField; // 검색어 입력 필드
    private JButton searchBtn; // 검색 버튼
    private JButton orderStatusBtn; // 주문 현황 창 열기 버튼

    // 데이터베이스 관련
    private Connection conn; // DB 연결 객체
    private OrderDAO orderDAO; // 주문 데이터 처리 객체
    private List<OrderDTO> orders; // 주문 목록 데이터

    // 생성자: UI 초기화 및 데이터 로드
    public OrderList_UI() {
        connectDatabase(); // DB 연결
        makeUI(); // UI 구성요소 생성
        refreshTable(); // 초기 데이터 로드
    }

    // 데이터베이스 연결 설정
    private void connectDatabase() {
        try {
            conn = PCPosDBConnection.getConnection();
            orderDAO = new OrderDAO(conn);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "DB 연결 실패!");
        }
    }

    // UI 구성요소 생성 및 배치
    private void makeUI() {
        setLayout(new BorderLayout());

        // 1. 상단 검색 패널 구성
        JPanel topPanel = new JPanel();
        searchTextField = new JTextField(20);
        searchBtn = new JButton("검색");
        orderStatusBtn = new JButton("주문 현황 보기");

        topPanel.add(searchTextField);
        topPanel.add(searchBtn);
        topPanel.add(orderStatusBtn);
        add(topPanel, BorderLayout.NORTH);

        // 2. 주문 목록 테이블 구성
        String[] columns = { "주문번호", "회원ID", "좌석번호", "주문내역", "금액", "결제방법", "상태", "주문시간" };
        tableModel = new DefaultTableModel(columns, 0);
        orderTable = new JTable(tableModel);
        add(new JScrollPane(orderTable), BorderLayout.CENTER);

        // 3. 버튼 동작 설정
        addButtonActions();
    }

    // 버튼 클릭 이벤트 처리 설정

    private void addButtonActions() {
        // 검색 버튼 클릭 시
        searchBtn.addActionListener(e -> {
            String keyword = searchTextField.getText().toLowerCase();
            searchOrders(keyword);
        });

        // 주문 현황 버튼 클릭 시
        orderStatusBtn.addActionListener(e -> {
            openOrderStatusWindow();
        });
    }

    // 주문 검색 기능
    private void searchOrders(String keyword) {
        tableModel.setRowCount(0); // 테이블 초기화

        // 키워드가 포함된 주문만 필터링하여 표시
        for (OrderDTO order : orders) {
            if (keyword.isEmpty() ||
                    order.getProductDetails().toLowerCase().contains(keyword)) {
                addOrderToTable(order);
            }
        }
    }

    // 주문 현황 창 열기
    private void openOrderStatusWindow() {
        try {
            OrderList_UI_Status_Frame statusFrame = new OrderList_UI_Status_Frame(conn);
            // 창이 닫힐 때 테이블 새로고침
            statusFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosed(java.awt.event.WindowEvent e) {
                    refreshTable();
                }
            });
            statusFrame.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "주문 현황 창을 열 수 없습니다!");
        }
    }

    // 테이블 데이터 새로고침
    private void refreshTable() {
        try {
            orders = orderDAO.findOrdersWithDetails(); // DB에서 최신 데이터 조회
            tableModel.setRowCount(0); // 테이블 초기화
            for (OrderDTO order : orders) {
                addOrderToTable(order); // 각 주문을 테이블에 추가
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "데이터 로드 실패!");
        }
    }

    // 주문 데이터를 테이블에 추가

    private void addOrderToTable(OrderDTO order) {
        tableModel.addRow(new Object[] {
                order.getOrder_no(),
                order.getMember_id(),
                order.getSeat_no(),
                order.getProductDetails(),
                order.getTotal_price(),
                order.getPayment_type(),
                order.getOrder_state(),
                order.getOrder_time()
        });
    }
}