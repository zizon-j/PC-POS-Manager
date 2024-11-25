package DAO;

import DTO.ProductDTO;

import java.sql.*;
import java.util.ArrayList;

public class ProductDAO implements DAO{
    private Connection conn;
    public ProductDAO(Connection conn){
        this.conn = conn;
    }

    @Override
    public Object findById(Object o) {
        return null;
    }

    @Override
    public boolean delete(Object o) {
        return false;
    }

    @Override
    public boolean update(Object o) {
        return false;
    }

    @Override
    public boolean insert(Object o) {
        return false;
    }

    @Override
    public ArrayList findAll() {
        ArrayList<ProductDTO> products = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;

        try{
            String sql = "SELECT * FROM product";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()){
                int product_no = rs.getInt("product_no");
                String product_name = rs.getString("product_name");
                int price = rs.getInt("price");
                int stock = rs.getInt("stock");
                int category_no = rs.getInt("category_no");

                ProductDTO product = new ProductDTO(product_no, product_name, price, stock, category_no);
                products.add(product);
                //Arraylist에 저저아

            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return products;
    }

    public ArrayList findbyCategoryAll(int category_no_search) {
        ArrayList<ProductDTO> products = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            String sql = "SELECT * FROM product where category_no = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, category_no_search);
            rs = pstmt.executeQuery();

            while (rs.next()){
                int product_no = rs.getInt("product_no");
                String product_name = rs.getString("product_name");
                int price = rs.getInt("price");
                int stock = rs.getInt("stock");
                int category_no = rs.getInt("category_no");

                ProductDTO product = new ProductDTO(product_no, product_name, price, stock, category_no);
                products.add(product);
                //Arraylist에 저저아

            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return products;
    }
}
