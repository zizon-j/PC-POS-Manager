package DAO;

import DTO.SeatDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class SeatDAO implements DAO<SeatDTO, String> {

    private Connection conn;

    public SeatDAO(Connection conn){
        this.conn = conn;
    }

    @Override
    public boolean insert(SeatDTO seatDTO) {
        PreparedStatement pstmt = null;
        try{
            String sql = "INSERT INTO seat () VALUES ();";
            pstmt = conn.prepareStatement(sql);

            pstmt.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println("좌석 안만들어짐");
            return false;
        }finally {
            try {
                if(pstmt != null)
                    pstmt.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return true;
    }
    @Override
    public SeatDTO findById(String s) {
        return null;
    }

    @Override
    public boolean delete(String s) {
        return false;
    }

    @Override
    public boolean update(SeatDTO seatDTO) {
        return false;
    }

    @Override
    public ArrayList<SeatDTO> findAll() {
        return null;
    }

    //자리 초기화용 deleteAll 메소드
    public boolean deleteAll() {
        PreparedStatement pstmt = null;
        try {
            String sql = "DElETE FROM seat";
            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return true;
        }
    }

    public boolean resetAuto_increment(){
        PreparedStatement pstmt = null;
        try {
            String sql = "ALTER TABLE seat AUTO_INCREMENT = 1";
            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return true;
        }
    }

}
