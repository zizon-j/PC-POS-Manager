package User_Login;

import DAO.ProductDAO;
import DAO.SeatDAO;
import DAO.UsageHistoryDAO;
import DTO.ProductDTO;
import DTO.UsageHistoryDTO;
import Jdbc.PCPosDBConnection;
import com.sun.prism.Texture;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;

public class User_OrderProduct extends JFrame {
    private final JButton orderBtn;
    private final JTextField order_details;
    private final DefaultTableModel model;
    private final JPanel categoryPanel;
    private final JButton endBtn;
    private final int seatNo;
    private final int member_no;


    public User_OrderProduct(int seatNo, int member_no) {
        this.seatNo = seatNo;
        this.member_no = member_no;

        setTitle("상품 주문");
        Container c = getContentPane();
        c.setLayout(null);
        setVisible(true);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1600, 900);


        // 테이블 상품 표시
        String[] columns = {"상품명", "가격", "재고"};
        model = new DefaultTableModel(columns, 0); //테이블 모델 초기화

        Connection conn = PCPosDBConnection.getConnection();
        ProductDAO productDAO = new ProductDAO(conn);
        List<ProductDTO> products = productDAO.findAll();

        if (products != null) {
            for (ProductDTO p : products) {
                Object[] row = {
                        p.getProduct_name(),
                        p.getPrice(),
                        p.getStock()
                };
                model.addRow(row);// 테이블에 행 추가
            }
        }

        JTable product_table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(product_table);

        scrollPane.setBounds(85, 111, 1057, 675);
        add(scrollPane);


        order_details = new JTextField();
        order_details.setBounds(1194, 111, 348, 562);
        add(order_details);

        orderBtn = new JButton("주문하기");
        orderBtn.setBounds(1194, 753, 348, 40); // 버튼 크기 및 위치
        add(orderBtn);


        orderBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        JButton btnCategoryAll = new JButton("전체");
        JButton btnCategoryRamyeon = new JButton("라면");
        JButton btnCategoryBab = new JButton("밥");
        JButton btnCategoryDrink = new JButton("음료");

        categoryPanel = new JPanel();

        categoryPanel.setLayout(new GridLayout());

        categoryPanel.add(btnCategoryAll);
        categoryPanel.add(btnCategoryRamyeon);
        categoryPanel.add(btnCategoryBab);
        categoryPanel.add(btnCategoryDrink);

        categoryPanel.setBounds(85, 21, 348, 73);
        add(categoryPanel);

        endBtn = new JButton("사용 종료");
        endBtn.setBounds(1194, 21, 348, 73);
        add(endBtn);

        // ------------------------------------------ 종료 버튼ㅁ ㅔ소드
        endBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SeatDAO seatDAO = new SeatDAO(conn);
                seatDAO.updateSeat2(seatNo);

                UsageHistoryDAO usageHistoryDAO = new UsageHistoryDAO(conn);
                UsageHistoryDTO usageHistory = new UsageHistoryDTO();
                usageHistory.setMember_no(member_no);
                usageHistoryDAO.update(usageHistory);
                dispose();
            }
        });

        //전체 선택
        btnCategoryAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (products != null) {
                    model.setRowCount(0);
                    for (ProductDTO p : products) {
                        Object[] row = {
                                p.getProduct_name(),
                                p.getPrice(),
                                p.getStock()
                        };
                        model.addRow(row);// 테이블에 행 추가
                    }
                }

                JTable product_table = new JTable(model);
                JScrollPane scrollPane = new JScrollPane(product_table);
            }
        });

        //라면 선택
        btnCategoryRamyeon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<ProductDTO> products = productDAO.findbyCategoryAll(1);


                if (products != null) {
                    model.setRowCount(0);
                    for (ProductDTO p : products) {
                        Object[] row = {
                                p.getProduct_name(),
                                p.getPrice(),
                                p.getStock()
                        };
                        model.addRow(row);// 테이블에 행 추가
                    }
                }

                JTable product_table = new JTable(model);
                JScrollPane scrollPane = new JScrollPane(product_table);
            }
        });

        //밥 선택
        btnCategoryBab.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<ProductDTO> products = productDAO.findbyCategoryAll(2);


                if (products != null) {
                    model.setRowCount(0);
                    for (ProductDTO p : products) {
                        Object[] row = {
                                p.getProduct_name(),
                                p.getPrice(),
                                p.getStock()
                        };
                        model.addRow(row);// 테이블에 행 추가
                    }
                }

                JTable product_table = new JTable(model);
                JScrollPane scrollPane = new JScrollPane(product_table);
            }
        });

        //음료 선택
        btnCategoryDrink.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<ProductDTO> products = productDAO.findbyCategoryAll(3);


                if (products != null) {
                    model.setRowCount(0);
                    for (ProductDTO p : products) {
                        Object[] row = {
                                p.getProduct_name(),
                                p.getPrice(),
                                p.getStock()
                        };
                        model.addRow(row);// 테이블에 행 추가
                    }
                }

                JTable product_table = new JTable(model);
                JScrollPane scrollPane = new JScrollPane(product_table);
            }
        });
    }


    public static void main(String[] args) {
    }
}
