package DAO;

import DTO.CategoryDTO;
import DTO.ProductDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements DAO<ProductDTO,String>{
    //db 연결을 위한 connection을 가져온다
    //sql문을 담은 Preparedstatement를 만든다
    //만들어진 Preparedstatement를 실행한다.
    //조회의 경우 SQL쿼리의 실행 결과를 REsultSet으로 받아서 저장할 오브젝트에 옮겨준다
    //작업중에 생성된 Connection, PreparedStatement, ResultSet같은 리소스는 작업을 마친 후 반드시 닫아준다.
    //JDBC API가 만들어내는 예외를 잡아서 직접처리 or throws를 사용해 던짐

    //DAO가 DB에서 데이터를 가져옴
    //가져온 데이터를 DTO로 만들어서 반환
    private Connection conn;

    public ProductDAO(Connection conn){this.conn = conn;}

    //메뉴 추가
    @Override
    public boolean insert(ProductDTO product) {
        PreparedStatement pstmt = null;
        try{
            String sql = "INSERT INTO product (product_name, price, stock, category_no) VALUES (?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, product.getProduct_name());
            pstmt.setInt(2,product.getPrice());
            pstmt.setInt(3,product.getStock());
            pstmt.setInt(4,product.getCategory_no());
            pstmt.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }finally {
            try{
                if (pstmt != null)
                    pstmt.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return true;
    }
    //카테고리 추가
    public boolean insertC(CategoryDTO category) {
        PreparedStatement pstmt = null;
        try{
            String sql = "INSERT INTO category (category_name) VALUES (?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, category.getCategory_name());
            pstmt.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }finally {
            try{
                if (pstmt != null)
                    pstmt.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return true;
    }

    //메뉴 검색
    //product_name으로 검색
    //executequery(): select 쿼리문 실행
    // 쿼리를 실행하고, 결과를 ResultSet 객체로 변환

    //executeUpdate(): insert, update, delete와 같은 dml에서 실행 결과로 영향을받은 레코드 수를변환
    //행의 개수를 반환하기 때문에 rs를 사용할 필요가 없다.


    @Override
    public ProductDTO findById(String s) {
        return null;
    }

    //메뉴 삭제
    @Override
    public boolean delete(String s) {
        PreparedStatement pstmt = null; //SQL문 바구니
        try {
            String sql = "DELETE FROM product WHERE product_name = ?"; //쿼리문
            pstmt = conn.prepareStatement(sql); //바구니에 담아서
            pstmt.setString(1, s);
            pstmt.executeUpdate(); //실행

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) //정상작동 했다면
                    pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    //카테고리 삭제
    public boolean delete_c(int s) {
        PreparedStatement pstmt = null; //SQL문 바구니
        try {
            String sql = "DELETE FROM category WHERE category_no = ?"; //쿼리문
            pstmt = conn.prepareStatement(sql); //바구니에 담아서
            pstmt.setInt(1, s);
            pstmt.executeUpdate(); //실행

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) //정상작동 했다면
                    pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
    //재고 수정

    public boolean update_menu(ProductDTO product) {
        PreparedStatement pstmt = null;
        boolean isUpdated = false;

        try {
            if (conn == null) {
                System.out.println("DB 연결 실패!");
                return false;
            }
            String sql = "UPDATE product set category_no=?, product_name=?, price=?, stock=? where product_no=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, product.getCategory_no());
            pstmt.setString(2, product.getProduct_name());
            pstmt.setInt(3, product.getPrice());
            pstmt.setInt(4, product.getStock());
            pstmt.setInt(5,product.getProduct_no());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                isUpdated = true; // 업데이트 성공
            }
        } catch (SQLException e) {
            e.printStackTrace(); // 오류 로그 출력
        } finally {
            try {
                if (pstmt != null) pstmt.close(); // PreparedStatement 닫기
            } catch (SQLException e) {
                e.printStackTrace(); // 자원 해제 중 오류 발생 시 처리
            }
        }
        return isUpdated; // 결과 반환
    }

    @Override
    public boolean update(ProductDTO product) {
        PreparedStatement pstmt = null;
        boolean isUpdated = false;

        try {
            if (conn == null) {
                System.out.println("DB 연결 실패!");
                return false;
            }
            String sql = "UPDATE product set stock=? where product_no=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, product.getStock());
            pstmt.setInt(2, product.getProduct_no());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                isUpdated = true; // 업데이트 성공
            }
        } catch (SQLException e) {
            e.printStackTrace(); // 오류 로그 출력
        } finally {
            try {
                if (pstmt != null) pstmt.close(); // PreparedStatement 닫기
            } catch (SQLException e) {
                e.printStackTrace(); // 자원 해제 중 오류 발생 시 처리
            }
        }
        return isUpdated; // 결과 반환
    }

    @Override
    public ArrayList findAll() {
        ArrayList<ProductDTO> products = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;

        try{
            String sql = "SELECT * FROM product_2 order by product_no asc";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()){
                int product_no = rs.getInt("product_no");
                String category_name = rs.getString("category_name");
                String product_name = rs.getString("product_name");
                int price = rs.getInt("price");
                int stock = rs.getInt("stock");

                ProductDTO product = new ProductDTO(product_no, category_name, product_name, price, stock);
                products.add(product);
                //Arraylist에 저장

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

    public List<ProductDTO> findById2(String product_name_search, String category_search) {

        ArrayList<ProductDTO> products = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT product_no, category_name, product_name, price, stock " +
                    "FROM product_2 " +
                    "WHERE (? IS NULL OR category_name = ?) " +
                    "AND product_name LIKE ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, category_search); //카테고리가 null이면 전체
            pstmt.setString(2,category_search);
            pstmt.setString(3,"%" + product_name_search + "%");
            rs = pstmt.executeQuery();

            while(rs.next()) {
                int product_no = rs.getInt("product_no");
                String category_name = rs.getString("category_name");
                String product_name = rs.getString("product_name");
                int price = rs.getInt("price");
                int stock = rs.getInt("stock");

                ProductDTO product = new ProductDTO(product_no,category_name,product_name, price, stock);
                products.add(product);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return products;
    }

    //카테고리 findAll
    public ArrayList findAll_c() {
        ArrayList<CategoryDTO> categorys = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;

        try{
            String sql = "SELECT * FROM category";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()){
                int category_no = rs.getInt("category_no");
                String category_name = rs.getString("category_name");

                CategoryDTO categroy = new CategoryDTO(category_no, category_name);
                categorys.add(categroy);
                //Arraylist에 저장

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
        return categorys;
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

                ProductDTO product = new ProductDTO(product_name, price, stock, category_no);
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
