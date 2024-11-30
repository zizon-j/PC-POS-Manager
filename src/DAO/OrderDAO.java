package DAO;

import DTO.OrderDTO;
import DTO.OrderDetailDTO;
import DTO.SeatDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 주문 관련 데이터베이스 작업을 처리하는 클래스
 */
public class OrderDAO implements DAO<OrderDTO, String> {
    private Connection conn; // 데이터베이스 연결 객체

    // 생성자: 데이터베이스 연결을 받아옴
    public OrderDAO(Connection conn) {
        this.conn = conn;
    }

    // 새로운 주문을 데이터베이스에 추가
    public boolean insertOrder(OrderDTO order) {
        String sql = "INSERT INTO orders (member_id, seat_no, total_price, order_request, " +
                "payment_type, order_state) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // 데이터 설정
            pstmt.setString(1, order.getMember_id());
            pstmt.setInt(2, order.getSeat_no());
            pstmt.setInt(3, order.getTotal_price());
            pstmt.setString(4, order.getOrder_request());
            pstmt.setString(5, order.getPayment_type());
            pstmt.setString(6, order.getOrder_state());

            // 실행 및 결과 반환
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("주문 추가 중 오류 발생: " + e.getMessage());
            return false;
        }
    }

    // 주문 번호로 주문 정보 조회
    public OrderDTO findOrderById(int order_no) {
        String sql = "SELECT * FROM orders WHERE order_no = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, order_no);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return createOrderFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("주문 조회 중 오류 발생: " + e.getMessage());
        }
        return null;
    }

    // 모든 주문 목록 조회
    public ArrayList<OrderDTO> findAllOrders() {
        ArrayList<OrderDTO> orderList = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY order_time DESC";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                orderList.add(createOrderFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("주문 목록 조회 중 오류 발생: " + e.getMessage());
        }
        return orderList;
    }

    // 주문 상태 변경
    public boolean updateOrderState(int orderNo, String newStatus) {
        String sql = "UPDATE orders SET order_state = ? WHERE order_no = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, orderNo);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("주문 상태 변경 중 오류 발생: " + e.getMessage());
            return false;
        }
    }

    // 주문과 상세 내역을 함께 조회
    public List<OrderDTO> findOrdersWithDetails() {
        List<OrderDTO> orderList = new ArrayList<>();

        String sql = "SELECT o.order_no, o.member_id, o.seat_no, " +
                "GROUP_CONCAT(CONCAT(p.product_name, '(', od.quantity, ')') SEPARATOR ', ') as product_details, " +
                "o.total_price, o.payment_type, o.order_state, o.order_time " +
                "FROM orders o " +
                "LEFT JOIN order_details od ON o.order_no = od.order_no " +
                "LEFT JOIN product p ON od.product_no = p.product_no " +
                "GROUP BY o.order_no, o.member_id, o.seat_no, o.total_price, " +
                "o.payment_type, o.order_state, o.order_time " +
                "ORDER BY o.order_time DESC";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                OrderDTO order = new OrderDTO();
                order.setOrder_no(rs.getInt("order_no"));
                order.setMember_id(rs.getString("member_id"));
                order.setSeat_no(rs.getInt("seat_no"));
                order.setProductDetails(rs.getString("product_details"));
                order.setTotal_price(rs.getInt("total_price"));
                order.setPayment_type(rs.getString("payment_type"));
                order.setOrder_state(rs.getString("order_state"));
                order.setOrder_time(rs.getTimestamp("order_time"));
                orderList.add(order);
            }
        } catch (SQLException e) {
            System.out.println("주문 상세 조회 중 오류 발생: " + e.getMessage());
        }
        return orderList;
    }

    // ResultSet에서 OrderDTO 객체 생성
    private OrderDTO createOrderFromResultSet(ResultSet rs) throws SQLException {
        return new OrderDTO(
                rs.getInt("order_no"),
                rs.getString("member_id"),
                rs.getInt("seat_no"),
                rs.getInt("total_price"),
                rs.getString("order_request"),
                rs.getString("payment_type"),
                rs.getString("order_state"),
                rs.getTimestamp("order_time"));
    }

    @Override
    public OrderDTO findById(String s) {
        return null;
    }

    @Override
    public boolean delete(String s) {
        return false;
    }

    @Override
    public boolean update(OrderDTO orderDTO) {
        return false;
    }

    @Override
    public boolean insert(OrderDTO orderDTO) {
        return false;
    }

    @Override
    public ArrayList<OrderDTO> findAll() {
        return null;
    }

    // 결제 완료 콜럼 조회용 메소드
    public ArrayList<OrderDTO> findFinsih() {
        ArrayList<OrderDTO> orders = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM orders where order_state = '결제 완료'";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            // set Int, STring , (인수, 들어갈값)
            // ? 위치가 인수임 ㅇㅇ
//            pstmt.setTimestamp(1, timestamp);

            while (rs.next()) {
                int order_no= rs.getInt("order_no");
                String member_id = rs.getString("member_id");
                int seat_no = rs.getInt("seat_no");
                int total_price = rs.getInt("total_price");
                String order_request = rs.getString("order_request");
                String payment_type = rs.getString("payment_type");
                String order_state = rs.getString("order_state");
                Timestamp order_time = rs.getTimestamp("order_time");

                OrderDTO order = new OrderDTO(order_no,member_id, seat_no, total_price, order_request, payment_type, order_state, order_time);
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return orders;
    }


}
