package main;

import Product.Product_UI;
import UI.MemberManagement_UI;
import UI.OrderList_UI;
import UI.Sales_UI;
import UI.Seat_UI;
import Login.LoginFrame;

import javax.swing.*;

public class Main extends JFrame{

    //창 전환
    JTabbedPane main = new JTabbedPane();

    //panel 객체 선언
    Seat_UI seat_ui = new Seat_UI();
    MemberManagement_UI memberManagement_ui = new MemberManagement_UI();
    Product.Product_UI product_ui = new Product_UI();
    OrderList_UI orderList_ui = new OrderList_UI();
    Sales_UI sales_ui = new Sales_UI();
    public Main(){
        super("PC방 POS");
        // 각 add 뒤에 자신이 만든 페이지 추가 , JPanel을 받아와야됨
        main.add("좌석관리", seat_ui);
        main.add("회원관리",memberManagement_ui);
        main.add("운영매출", sales_ui);
        main.add("상품관리",product_ui);
        main.add("로그분석", new JTextArea("보류")); // 보류
        main.add("주문내역", orderList_ui);

        add(main);

        setSize(1600, 900);
        setLocationRelativeTo(null); // 중앙에 뜨기
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // LoginFrame 먼저 실행 후 로그인 성공 시 main.Main 실행
    // 현재는 로그인 버튼 클릭시 바로 이동하지만 추후 연동 후 기능 추가 예정
    public static void main(String[] args) {
        // LoginFrame 표시
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame("명전 PC방 POS - 로그인 화면");
            loginFrame.setVisible(true);
        });
    }
}