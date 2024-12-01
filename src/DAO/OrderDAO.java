package DAO;

import DTO.OrderDTO;
import DTO.OrderDetailDTO;
import DTO.SalesDTO;
import DTO.SeatDTO;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public ArrayList<SalesDTO> findFinsih() {
        ArrayList<SalesDTO> orders = new ArrayList<>();
        Statement stmt = null; // sql문을 저장하기 위한 객체
        ResultSet rs = null; // sql문에서 조회한 거를 저장하는 객체

        try {
            String sql = "SELECT o.order_no, " +
                    "       o.order_time, " +
                    "       o.total_price, " +
                    "       o.payment_type, " +
                    "       (SELECT SUM(o2.total_price) " +
                    "        FROM orders o2 " +
                    "        WHERE o2.order_time <= o.order_time " +
                    "          AND o2.order_state = '결제 완료') AS total_sum " +
                    "FROM orders o " +
                    "WHERE o.order_state = '결제 완료' " +
                    "ORDER BY o.order_time";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);


            // set Int, STring , (인수, 들어갈값)
            // ? 위치가 인수임 ㅇㅇ

            // pstmt.setTimestamp(1, timestamp);

            while (rs.next()) {
                int order_no = rs.getInt("order_no");
                int total_price = rs.getInt("total_price");
                String payment_type = rs.getString("payment_type");
                Date order_time = rs.getDate("order_time");
                int total_sum = rs.getInt("total_sum");


                SalesDTO order = new SalesDTO(order_no, total_price, payment_type, order_time, total_sum);
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

    // 날짜 조회
    public ArrayList<SalesDTO> searchRange(LocalDate startDate, LocalDate endDate) {
        ArrayList<SalesDTO> salesList = new ArrayList<>();
        Statement stmt = null;  // SQL 문을 저장할 객체
        ResultSet rs = null;    // SQL 문에서 조회한 결과를 저장할 객체

        try {
            String sql = "SELECT o.order_no, " +
                    "       o.order_time, " +
                    "       o.total_price, " +
                    "       o.payment_type, " +
                    "       (SELECT SUM(o2.total_price) " +
                    "        FROM orders o2 " +
                    "        WHERE o2.order_time <= o.order_time " +
                    "          AND o2.order_state = '결제 완료') AS total_sum " +
                    "FROM orders o " +
                    "WHERE o.order_state = '결제 완료' ";

            // 날짜 범위가 주어졌다면 조건 추가
            if (startDate != null) {
                sql += "AND o.order_time >= ? ";
            }
            if (endDate != null) {
                sql += "AND o.order_time <= ? ";
            }

            sql += "ORDER BY o.order_time";  // 결제 시간 기준으로 정렬

            stmt = conn.createStatement();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                int index = 1;
                // 날짜 파라미터를 설정 (startDate와 endDate가 있을 경우)
                if (startDate != null) {
                    pstmt.setDate(index++, Date.valueOf(startDate));
                }
                if (endDate != null) {
                    LocalDateTime endDateTime = endDate.atTime(23, 59, 59);  // 23:59:59로 설정
                    pstmt.setTimestamp(index++, Timestamp.valueOf(endDateTime)); // localdate를 타임스탬프로 변환하지않으면 당일 조회가 안됌
                }

                rs = pstmt.executeQuery();  // 쿼리 실행

                while (rs.next()) {
                    int order_no = rs.getInt("order_no");
                    int total_price = rs.getInt("total_price");
                    String payment_type = rs.getString("payment_type");
                    Date order_time = rs.getDate("order_time");
                    int total_sum = rs.getInt("total_sum");

                    // 조회된 결과로 SalesDTO 객체 생성 후 리스트에 추가
                    SalesDTO salesDTO = new SalesDTO(order_no, total_price, payment_type, order_time, total_sum);
                    salesList.add(salesDTO);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // SQL 쿼리 실행 중 오류 발생 시 출력
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();  // 자원 해제 중 오류 발생 시 출력
            }
        }

        return salesList;

    }

    // 주문 취소 메소드
    public boolean cancelOrder(int orderNo) {
        String sql = "UPDATE orders SET order_state = '결제 취소' WHERE order_no = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderNo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;  // 업데이트가 성공했으면 true 반환
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 달력에 그 날마다의 매출
    public List<SalesDTO> dayTotalSales(int year, int month) {
        List<SalesDTO> salesList = new ArrayList<>();
        String query = "SELECT order_no, total_price, payment_type, order_time, SUM(total_price) as total_sum " +
                "FROM orders WHERE YEAR(order_time) = ? AND MONTH(order_time) = ? GROUP BY order_time";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, year);
            pstmt.setInt(2, month);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                SalesDTO sales = new SalesDTO(
                        rs.getInt("order_no"),
                        rs.getInt("total_price"),
                        rs.getString("payment_type"),
                        rs.getDate("order_time"),
                        rs.getInt("total_sum")
                );
                salesList.add(sales);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salesList;
    }

    public List<SalesDTO> dddSales(int year, int month) {
        List<SalesDTO> salesList = new ArrayList<>();
        String query = "SELECT order_no, total_price, payment_type, order_time, " +
                "SUM(total_price) OVER (ORDER BY order_time) AS total_sum " +
                "FROM orders WHERE YEAR(order_time) = ? AND MONTH(order_time) = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, year);
            stmt.setInt(2, month);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                SalesDTO sales = new SalesDTO(
                        rs.getInt("order_no"),
                        rs.getInt("total_price"),
                        rs.getString("payment_type"),
                        rs.getDate("order_time"),
                        rs.getInt("total_sum")
                );
                salesList.add(sales);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salesList;
    }

}
