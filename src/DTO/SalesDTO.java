package DTO;

import java.sql.Date;

public class SalesDTO {
    int order_no, total_price;
    String payment_type;
    Date order_time;
    int total_sum;

    public SalesDTO(int order_no, int total_price, String payment_type, Date order_time, int total_sum) {
        this.order_no = order_no;
        this.total_price = total_price;
        this.payment_type = payment_type;
        this.order_time = order_time;
        this.total_sum = total_sum;
    }


    public int getOrder_no() {return order_no;}
    public void setOrder_no(int order_no) {this.order_no = order_no;}

    public int getTotal_price() {return total_price;}
    public void setTotal_price(int total_price) {this.total_price = total_price;}

    public String getPayment_type() {return payment_type;}
    public void setPayment_type(String payment_type) {this.payment_type = payment_type;}

    public Date getOrder_time() {return order_time;}
    public void setOrder_time(Date order_time) {this.order_time = order_time;}

    public int getTotal_sum() {return total_sum;}
    public void setTotal_sum(int total_sum) {this.total_sum = total_sum;}
}


