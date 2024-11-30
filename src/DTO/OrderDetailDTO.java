package DTO;

/**
 * 주문 상세 정보를 담는 데이터 클래스
 * 데이터베이스의 order_details 테이블과 매핑됨
 */
public class OrderDetailDTO {
    // 기본 정보
    private int orderdetail_no; // 주문 상세 번호 (기본키)
    private int order_no; // 주문 번호 (외래키)
    private int product_no; // 상품 번호 (외래키)
    private int quantity; // 주문 수량

    // 기본 생성자
    public OrderDetailDTO() {
    }

    // 모든 필드를 초기화하는 생성자
    public OrderDetailDTO(int orderdetail_no, int order_no, int product_no, int quantity) {
        this.orderdetail_no = orderdetail_no;
        this.order_no = order_no;
        this.product_no = product_no;
        this.quantity = quantity;
    }

    // Getter와 Setter 메소드들
    public int getOrderdetail_no() {
        return orderdetail_no;
    }

    public void setOrderdetail_no(int orderdetail_no) {
        this.orderdetail_no = orderdetail_no;
    }

    public int getOrder_no() {
        return order_no;
    }

    public void setOrder_no(int order_no) {
        this.order_no = order_no;
    }

    public int getProduct_no() {
        return product_no;
    }

    public void setProduct_no(int product_no) {
        this.product_no = product_no;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
