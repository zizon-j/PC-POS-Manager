package DAO;

import DTO.Time_Plus_LogDTO;

import java.sql.*;
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
    public double calTotalUsageMoney(String member_id) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        double totalUsageMoney = 0.0;

        try {
            String sql = "select sum(tl.money) " +
                    "from time_plus_log tl " +
                    "join time t on tl.money = t.money " +
                    "where tl.member_id = ? " +
                    "group by tl.member_id";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, member_id);
            rs = pstmt.executeQuery();

            if (rs.next())
                totalUsageMoney = rs.getDouble(1);
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return totalUsageMoney;
    }
}
