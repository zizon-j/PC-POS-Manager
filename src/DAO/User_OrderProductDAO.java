package DAO;

import DTO.OrderDTO;
import DTO.OrderDetailDTO;
import DTO.User_OrderProductDTO;

import java.sql.*;
import java.util.List;

// 사용자 주문 처리를 담당하는 클래스
public class User_OrderProductDAO {
    // 데이터베이스 연결
    private final Connection conn;
    // 주문 처리를 위한 DAO 객체들
    private final OrderDAO orderDAO;
    private final OrderDetailsDAO orderDetailsDAO;

    // 데이터베이스 연결을 받아서 초기화하는 생성자
    public User_OrderProductDAO(Connection conn) {
        this.conn = conn;
        this.orderDAO = new OrderDAO(conn);
        this.orderDetailsDAO = new OrderDetailsDAO(conn);
    }

    // 회원 번호로 회원 ID를 찾는 메소드
    public String getMemberId(int memberNo) throws SQLException {
        // SQL 쿼리 준비
        String sql = "SELECT member_id FROM member WHERE member_no = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // 회원 번호 설정
            pstmt.setInt(1, memberNo);
            // 쿼리 실행
            ResultSet rs = pstmt.executeQuery();

            // 결과가 있으면 회원 ID 반환
            if (rs.next()) {
                return rs.getString("member_id");
            }
            return null;
        }
    }

    // 상품 이름으로 상품 번호를 찾는 메소드
    public int getProductNo(String productName) throws SQLException {
        String sql = "SELECT product_no FROM product WHERE product_name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, productName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("product_no");
            }
            return 0;
        }
    }

    // 상품의 현재 재고를 확인하는 메소드
    public int getStock(int productNo) throws SQLException {
        String sql = "SELECT stock FROM product WHERE product_no = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productNo);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("stock");
            }
            return 0;
        }
    }

    // 상품의 재고를 업데이트하는 메소드
    public void updateStock(int productNo, int newStock) throws SQLException {
        String sql = "UPDATE product SET stock = ? WHERE product_no = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newStock);
            pstmt.setInt(2, productNo);
            pstmt.executeUpdate();
        }
    }

    // 주문을 처리하는 메소드
    public boolean processOrder(List<User_OrderProductDTO> items, String memberId,
                                int seatNo, String orderRequest, String paymentType) throws SQLException {

        // 트랜잭션 시작
        try {
            conn.setAutoCommit(false);

            // 주문 총액 계산
            int totalPrice = 0;
            for (User_OrderProductDTO item : items) {
                totalPrice += item.getPrice() * item.getQuantity();
            }

            // 새로운 주문 생성
            OrderDTO order = new OrderDTO();
            order.setMember_id(memberId);
            order.setSeat_no(seatNo);
            order.setTotal_price(totalPrice);
            order.setOrder_request(orderRequest);
            order.setPayment_type(paymentType);
            order.setOrder_state("주문 대기");

            // 주문 저장하고 주문번호 받기
            int orderNo = orderDAO.insertOrderAndGetId(order);

            // 각 주문 상품에 대해 처리
            for (User_OrderProductDTO item : items) {
                // 상품 번호 찾기
                int productNo = getProductNo(item.getProductName());

                // 주문 상세 정보 생성
                OrderDetailDTO detail = new OrderDetailDTO();
                detail.setOrder_no(orderNo);
                detail.setProduct_no(productNo);
                detail.setQuantity(item.getQuantity());

                // 주문 상세 정보 저장
                orderDetailsDAO.insertOrderDetail(detail);

                // 재고 수정
                int currentStock = getStock(productNo);
                updateStock(productNo, currentStock - item.getQuantity());
            }

            // 모든 처리가 성공하면 커밋
            conn.commit();
            return true;
        } catch (SQLException e) {
            // 오류 발생시 롤백
            conn.rollback();
            throw e;
        } finally {
            // 자동 커밋 설정 복구
            conn.setAutoCommit(true);
        }
    }
}