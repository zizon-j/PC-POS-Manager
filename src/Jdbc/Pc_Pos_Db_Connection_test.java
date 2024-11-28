package Jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Pc_Pos_Db_Connection_test {
    public static void main(String[] args) {
        Connection connection = null;
        String url = "jdbc:mysql://223.130.151.16:3306/pc_pos_db";
        String id = "pc_pos_root";
        String pwd = "pc_post**@@";

        // 디비 드라이버 불러오기
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("jdbc 드라이버 로드 오류");
            e.printStackTrace();
        }

        //디비 접속
        try {
            connection = DriverManager.getConnection(url, id, pwd);
            System.out.println("연결 완료");
        } catch (SQLException e) {
            System.out.println("연결오류");
            e.printStackTrace();
        }

        // 접속 종료
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
        }

    }
}
