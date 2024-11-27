package DAO;

import DTO.MemberDTO;
import DTO.TimeDTO;

import java.sql.*;
import java.util.ArrayList;

public class TimeDAO implements DAO<TimeDTO, String>{
    Connection conn;
    public TimeDAO(Connection conn){
        this.conn = conn;
    }

    @Override
    public TimeDTO findById(String s) {
        PreparedStatement pspmt = null;
        ResultSet rs = null;
        TimeDTO time = null;
        try{
            String sql = "SELECT * FROM time WHERE money = ?";
            pspmt = conn.prepareStatement(sql);
            pspmt.setString(1, s);
            rs = pspmt.executeQuery();

            if(rs.next()){
                int money = rs.getInt("money");
                int plus_time = rs.getInt("plus_time");

                time = new TimeDTO(money, plus_time);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if (rs != null)
                    rs.close();
                if (pspmt != null)
                    pspmt.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return time;
    }

    @Override
    public boolean delete(String s) {
        return false;
    }

    @Override
    public boolean update(TimeDTO timeDTO) {
        return false;
    }

    @Override
    public boolean insert(TimeDTO timeDTO) {
        return false;
    }

    @Override
    public ArrayList<TimeDTO> findAll() {
        return null;
    }
}
