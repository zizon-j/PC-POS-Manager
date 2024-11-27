package DAO;

import DTO.Time_Plus_LogDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class Time_Plus_LogDAO implements DAO<Time_Plus_LogDTO, String> {

    Connection conn;
    public Time_Plus_LogDAO(Connection conn){
        this.conn = conn;
    }
    @Override
    public Time_Plus_LogDTO findById(String s) {
        return null;
    }

    @Override
    public boolean delete(String s) {
        return false;
    }

    @Override
    public boolean update(Time_Plus_LogDTO timePlusLogDTO) {
        return false;
    }

    @Override
    public boolean insert(Time_Plus_LogDTO timePlusLog) {
        PreparedStatement pstmt = null;
        try{
            String sql = "INSERT INTO time_plus_log (member_id, money) VALUES (?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, timePlusLog.getMember_id());
            pstmt.setInt(2, timePlusLog.getMoney());


            pstmt.executeUpdate();

        }catch (SQLException e){
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


        return false;
    }

    @Override
    public ArrayList<Time_Plus_LogDTO> findAll() {
        return null;
    }
}
