package DTO;

import java.sql.Timestamp;

// 주문 정보를 담는 클래스
public class OrderDTO {
    // 주문 기본 정보들
    private int order_no; // 주문 번호
    private String member_id; // 회원 ID
    private int seat_no; // 좌석 번호
    private int total_price; // 총 주문 금액
    private String order_request; // 요청사항
    private String payment_type; // 결제 방식
    private String order_state; // 주문 상태
    private Timestamp order_time; // 주문 시간
    private String productDetails; // 주문 상품 정보

    // 기본 생성자
    public OrderDTO() {
    }

    // 모든 정보를 받는 생성자
    public OrderDTO(int order_no, String member_id, int seat_no, int total_price,
            String order_request, String payment_type, String order_state, Timestamp order_time) {
        this.order_no = order_no;
        this.member_id = member_id;
        this.seat_no = seat_no;
        this.total_price = total_price;
        this.order_request = order_request;
        this.payment_type = payment_type;
        this.order_state = order_state;
        this.order_time = order_time;
    }

    // 각 정보를 가져오고 설정하는 메소드들
    public int getOrder_no() {
        return order_no;
    }

    public void setOrder_no(int order_no) {
        this.order_no = order_no;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public int getSeat_no() {
        return seat_no;
    }

    public void setSeat_no(int seat_no) {
        this.seat_no = seat_no;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public String getOrder_request() {
        return order_request;
    }

    public void setOrder_request(String order_request) {
        this.order_request = order_request;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getOrder_state() {
        return order_state;
    }

    public void setOrder_state(String order_state) {
        this.order_state = order_state;
    }

    public Timestamp getOrder_time() {
        return order_time;
    }

    public void setOrder_time(Timestamp order_time) {
        this.order_time = order_time;
    }

    public String getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }
}