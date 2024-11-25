package Jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PCPosDBConnection {
    // connection은 중간에 qurery문이 없으면 바로 종료함.
    // connection을 받아오는 메소드

    public static Connection getConnection() {
        Connection conn = null;
        try {
            String url = "jdbc:mysql://223.130.151.16:3306/pc_pos_db";
            String id = "pc_pos_root";
            String pwd = "pc_post**@@";
            conn = DriverManager.getConnection(url, id, pwd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void main(String[] args) {
        // Connection connection = null;
        // String url = "jdbc:mysql://223.130.151.16:3306/pc_pos_db";
        // String id = "pc_pos_root";
        // String pwd = "pc_post**@@";
        //
        //
        // // 디비 드라이버 불러오기
        // try {
        // Class.forName("com.mysql.cj.jdbc.Driver");
        // }catch (ClassNotFoundException e){
        // System.out.println("jdbc 드라이버 로드 오류");
        // e.printStackTrace();
        // }
        //
        // //디비 접속
        // try {
        // connection = DriverManager.getConnection(url,id,pwd);
        // System.out.println("연결 완료");
        // }
        // catch (SQLException e){
        // System.out.println("연결오류");
        // e.printStackTrace();
        // }
    }
}
