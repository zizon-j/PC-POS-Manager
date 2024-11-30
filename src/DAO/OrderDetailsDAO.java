package DAO;

import DTO.OrderDTO;
import DTO.OrderDetailDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 주문 상세 정보 관련 데이터베이스 작업을 처리하는 클래스
 */
public class OrderDetailsDAO implements DAO<OrderDetailDTO, String> {
    private Connection conn; // 데이터베이스 연결 객체

    /**
     * 생성자: 데이터베이스 연결을 받아옴
     */
    public OrderDetailsDAO(Connection conn) {
        this.conn = conn;
    }

    // 주문 상세 번호로 상세 정보 조회
    public OrderDetailDTO findById(int orderDetailNo) {
        String sql = "SELECT * FROM order_details WHERE orderdetail_no = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderDetailNo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return createFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("주문 상세 조회 중 오류 발생: " + e.getMessage());
        }
        return null;
    }

    // 주문 상세 정보 삭제
    public boolean delete(int orderDetailNo) {
        String sql = "DELETE FROM order_details WHERE orderdetail_no = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderDetailNo);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("주문 상세 삭제 중 오류 발생: " + e.getMessage());
            return false;
        }
    }

    @Override
    public OrderDetailDTO findById(String s) {
        return null;
    }

    @Override
    public boolean delete(String s) {
        return false;
    }

    // 주문 상세 정보 수정
    public boolean update(OrderDetailDTO orderDetail) {
        String sql = "UPDATE order_details SET order_no = ?, product_no = ?, quantity = ? " +
                "WHERE orderdetail_no = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderDetail.getOrder_no());
            pstmt.setInt(2, orderDetail.getProduct_no());
            pstmt.setInt(3, orderDetail.getQuantity());
            pstmt.setInt(4, orderDetail.getOrderdetail_no());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("주문 상세 수정 중 오류 발생: " + e.getMessage());
            return false;
        }
    }

    // 새로운 주문 상세 정보 추가
    public boolean insert(OrderDetailDTO orderDetail) {
        String sql = "INSERT INTO order_details (order_no, product_no, quantity) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderDetail.getOrder_no());
            pstmt.setInt(2, orderDetail.getProduct_no());
            pstmt.setInt(3, orderDetail.getQuantity());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("주문 상세 추가 중 오류 발생: " + e.getMessage());
            return false;
        }
    }

    // 모든 주문 상세 정보 조회
    public ArrayList<OrderDetailDTO> findAll() {
        ArrayList<OrderDetailDTO> orderDetails = new ArrayList<>();
        String sql = "SELECT * FROM order_details ORDER BY orderdetail_no";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                orderDetails.add(createFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("전체 주문 상세 조회 중 오류 발생: " + e.getMessage());
        }
        return orderDetails;
    }

    // 특정 주문의 모든 상세 내역 조회
    public List<OrderDetailDTO> findByOrderId(int orderId) {
        List<OrderDetailDTO> orderDetails = new ArrayList<>();
        String sql = "SELECT * FROM order_details WHERE order_no = ? ORDER BY orderdetail_no";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    orderDetails.add(createFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("주문별 상세 조회 중 오류 발생: " + e.getMessage());
        }
        return orderDetails;
    }

    // ResultSet에서 OrderDetailDTO 객체 생성
    private OrderDetailDTO createFromResultSet(ResultSet rs) throws SQLException {
        return new OrderDetailDTO(
                rs.getInt("orderdetail_no"),
                rs.getInt("order_no"),
                rs.getInt("product_no"),
                rs.getInt("quantity"));
    }

}
