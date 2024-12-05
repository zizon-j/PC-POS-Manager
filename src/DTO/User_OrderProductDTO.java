package DTO;

// 사용자가 주문한 상품의 정보를 담는 클래스
public class User_OrderProductDTO {
    // 상품 이름
    private final String productName;
    // 주문 수량
    private int quantity;
    // 상품 가격
    private final int price;
    // 상품 번호 (데이터베이스에서 사용)
    private int productNo;

    // 새로운 주문 상품을 만들 때 사용하는 생성자
    public User_OrderProductDTO(String productName, int quantity, int price) {
        // 입력받은 값들을 저장
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    // 각 정보를 가져오는 메소드들
    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public int getProductNo() {
        return productNo;
    }

    // 수량과 상품번호를 변경하는 메소드들
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setProductNo(int productNo) {
        this.productNo = productNo;
    }
}