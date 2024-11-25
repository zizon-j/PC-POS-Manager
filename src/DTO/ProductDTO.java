package DTO;

public class ProductDTO {
    int product_no, price, stock, category_no;
    String product_name,category_name;

    public ProductDTO(){}


    public ProductDTO(int product_no, String product_name, int price, int stock, int category_no) {
        this.product_no = product_no;
        this.product_name = product_name;
        this.price = price;
        this.stock = stock;
        this.category_no = category_no;
    }

    public ProductDTO(String product_name, int price, int stock, int category_no) {
        //this.product_no = product_no;
        this.product_name = product_name;
        this.price = price;
        this.stock = stock;
        this.category_no = category_no;
    }
    //product_2 뷰를 불러오는 생성자
    public ProductDTO(int product_no,String category_name, String product_name, int price, int stock) {
        this.product_no = product_no;
        this.category_name = category_name;
        this.product_name = product_name;
        this.price = price;
        this.stock = stock;
    }

    //재고수정
    public ProductDTO(int stock, int product_no) {
        this.stock = stock;
        this.product_no = product_no;
    }


    public int getProduct_no() {
        return product_no;
    }

    public void setProduct_no(int product_no) {
        this.product_no = product_no;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getCategory_no() {return category_no; }

    public void setCategory_no(int category_no) {
        this.category_no = category_no;
    }

    public String getCategory_name() {return category_name; }

    public void setCategory_name(String category_name) {this.category_name = category_name;
    }
}
