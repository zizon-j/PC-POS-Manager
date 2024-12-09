package User_Login;

import DAO.ProductDAO;
import DAO.SeatDAO;
import DAO.UsageHistoryDAO;
import DTO.ProductDTO;
import DTO.UsageHistoryDTO;
import DTO.User_OrderProductDTO;
import DAO.User_OrderProductDAO;
import Jdbc.PCPosDBConnection;
import com.sun.prism.Texture;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class User_OrderProduct extends JFrame {
    private final JButton orderBtn;
    private final JTextField order_details;
    private final DefaultTableModel model;
    private final JPanel categoryPanel;
    private final JButton endBtn;
    private final int seatNo;
    private final int member_no;
    private DefaultTableModel selectedModel; // 선택한 상품들을 보여줄 테이블 모델
    private JTable selectedTable; // 선택한 상품들을 보여줄 테이블
    private List<User_OrderProductDTO> selectedItems = new ArrayList<>(); // 선택한 상품들을 저장할 리스트

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
        String[] columns = { "상품명", "가격", "재고" };
        model = new DefaultTableModel(columns, 0); // 테이블 모델 초기화

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
        order_details.setBounds(1194, 432, 348, 241);
        add(order_details);

        orderBtn = new JButton("주문하기");
        orderBtn.setBounds(1371, 683, 171, 40);
        add(orderBtn);

        // 주문 버튼시 상호작용
        orderBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedItems.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "선택한 상품이 없습니다.");
                    return;
                }

                // 결제 방식 선택 다이얼로그 생성
                String[] options = { "현금", "카드", "취소" };
                int choice = JOptionPane.showOptionDialog(null,
                        "결제 방식을 선택해주세요.",
                        "결제 방식 선택",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);

                // 취소를 선택한 경우
                if (choice == 2 || choice == JOptionPane.CLOSED_OPTION) {
                    return;
                }

                // 선택된 결제 방식
                String paymentType = options[choice];

                try {
                    Connection conn = PCPosDBConnection.getConnection();
                    User_OrderProductDAO orderProductDAO = new User_OrderProductDAO(conn);

                    String memberId = orderProductDAO.getMemberId(member_no);
                    boolean success = orderProductDAO.processOrder(selectedItems, memberId,
                            seatNo, order_details.getText(), paymentType);

                    if (success) {
                        JOptionPane.showMessageDialog(null, paymentType + " 결제가 완료되었습니다.");

                        // 선택한 상품 목록 초기화
                        selectedItems.clear();
                        selectedModel.setRowCount(0);

                        // 테이블 새로고침
                        refreshTable();
                    } else {
                        JOptionPane.showMessageDialog(null, "주문 처리 중 오류가 발생했습니다.");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "주문 처리 중 오류가 발생했습니다.");
                    ex.printStackTrace();
                }
            }
        });

        JButton btnCategoryAll = new JButton("전체");
        JButton btnCategoryRamyeon = new JButton("라면");
        JButton btnCategoryBab = new JButton("밥");
        JButton btnCategoryDrink = new JButton("음료");
        JButton btnCatergoryGansik = new JButton("간식");
        JButton btnCategorySet = new JButton("세트");

        categoryPanel = new JPanel();

        categoryPanel.setLayout(new GridLayout());

        categoryPanel.add(btnCategoryAll);
        categoryPanel.add(btnCategoryRamyeon);
        categoryPanel.add(btnCategoryBab);
        categoryPanel.add(btnCategoryDrink);
        categoryPanel.add(btnCatergoryGansik);
        categoryPanel.add(btnCategorySet);

        categoryPanel.setBounds(85, 21, 500, 73);
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

        // 전체 선택
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

        // 라면 선택
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

        // 밥 선택
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

        // 음료 선택
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

        btnCatergoryGansik.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<ProductDTO> products = productDAO.findbyCategoryAll(4);

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

        btnCategorySet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<ProductDTO> products = productDAO.findbyCategoryAll(5);

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

        // 선택하기 버튼 추가
        JButton selectBtn = new JButton("선택하기");
        selectBtn.setBounds(1194, 683, 171, 40);
        add(selectBtn);

        // 선택한 상품들을 보여줄 테이블 생성
        String[] selectedColumns = { "상품명", "수량", "가격" };
        selectedModel = new DefaultTableModel(selectedColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        selectedTable = new JTable(selectedModel);
        JScrollPane selectedScrollPane = new JScrollPane(selectedTable);
        selectedScrollPane.setBounds(1194, 111, 348, 281);
        add(selectedScrollPane);

        // 선택하기 버튼 이벤트
        selectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = product_table.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "상품을 선택해주세요.");
                    return;
                }

                String quantityStr = JOptionPane.showInputDialog("수량을 입력하세요:");
                if (quantityStr == null || quantityStr.trim().isEmpty()) {
                    return;
                }

                try {
                    int quantity = Integer.parseInt(quantityStr);
                    if (quantity <= 0) {
                        JOptionPane.showMessageDialog(null, "올바른 수량을 입력해주세요.");
                        return;
                    }

                    String productName = (String) model.getValueAt(selectedRow, 0);
                    int price = (int) model.getValueAt(selectedRow, 1);
                    int stock = (int) model.getValueAt(selectedRow, 2);

                    if (quantity > stock) {
                        JOptionPane.showMessageDialog(null, "재고가 부족합니다.");
                        return;
                    }

                    // 선택한 상품 리스트에 추가
                    User_OrderProductDTO item = new User_OrderProductDTO(productName, quantity, price);
                    selectedItems.add(item);

                    // 선택한 상품 테이블에 추가
                    Object[] row = {
                            productName,
                            quantity,
                            price * quantity
                    };
                    selectedModel.addRow(row);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "올바른 수량을 입력해주세요.");
                }
            }
        });

        // 수정 버튼과 삭제 버튼을 나란히 배치
        JButton editBtn = new JButton("수량 수정");
        editBtn.setBounds(1194, 392, 171, 40); // 왼쪽에 배치
        add(editBtn);

        JButton deleteBtn = new JButton("선택 삭제");
        deleteBtn.setBounds(1371, 392, 171, 40); // 오른쪽에 배치
        add(deleteBtn);

        // 수정 버튼 이벤트 추가
        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = selectedTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "수정할 항목을 선택해주세요.");
                    return;
                }

                String productName = (String) selectedModel.getValueAt(selectedRow, 0);
                int currentQuantity = (int) selectedModel.getValueAt(selectedRow, 1);
                int pricePerUnit = ((int) selectedModel.getValueAt(selectedRow, 2)) / currentQuantity;

                String quantityStr = JOptionPane.showInputDialog("새로운 수량을 입력하세요:", currentQuantity);
                if (quantityStr == null || quantityStr.trim().isEmpty()) {
                    return;
                }

                try {
                    int newQuantity = Integer.parseInt(quantityStr);
                    if (newQuantity <= 0) {
                        JOptionPane.showMessageDialog(null, "올바른 수량을 입력해주세요.");
                        return;
                    }

                    // 재고 확인
                    Connection conn = PCPosDBConnection.getConnection();
                    User_OrderProductDAO orderProductDAO = new User_OrderProductDAO(conn);
                    int productNo = orderProductDAO.getProductNo(productName);
                    int stock = orderProductDAO.getStock(productNo);

                    // 현재 수량을 제외한 재고 확인
                    int availableStock = stock + currentQuantity;
                    if (newQuantity > availableStock) {
                        JOptionPane.showMessageDialog(null, "재고가 부족합니다.");
                        return;
                    }

                    // 선택된 항목 업데이트
                    selectedItems.get(selectedRow).setQuantity(newQuantity);

                    // 테이블 업데이트
                    selectedModel.setValueAt(newQuantity, selectedRow, 1);
                    selectedModel.setValueAt(pricePerUnit * newQuantity, selectedRow, 2);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "올바른 수량을 입력해주세요.");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "데이터베이스 오류가 발생했습니다.");
                    ex.printStackTrace();
                }
            }
        });

        // 삭제 버튼 이벤트 추가
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = selectedTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "삭제할 항목을 선택해주세요.");
                    return;
                }

                // 선택된 항목을 리스트와 테이블에서 제거
                selectedItems.remove(selectedRow);
                selectedModel.removeRow(selectedRow);
            }
        });
    }

    private void refreshTable() {
        Connection conn = PCPosDBConnection.getConnection();
        ProductDAO productDAO = new ProductDAO(conn);
        List<ProductDTO> products = productDAO.findAll();

        model.setRowCount(0);
        if (products != null) {
            for (ProductDTO p : products) {
                Object[] row = {
                        p.getProduct_name(),
                        p.getPrice(),
                        p.getStock()
                };
                model.addRow(row);
            }
        }
    }

    public static void main(String[] args) {
    }
}
