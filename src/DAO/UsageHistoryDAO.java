package DAO;

import DTO.UsageHistoryDTO;

import java.sql.*;
import java.util.ArrayList;

public class UsageHistoryDAO implements DAO<UsageHistoryDTO, String> {
    private Connection conn;

    public UsageHistoryDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public UsageHistoryDTO findById(String memberNo) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        UsageHistoryDTO history = null;

        try {
            String sql = "select * from usage_history where member_no = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, memberNo);
            rs = ps.executeQuery();

            if(rs.next()) {
                int history_no = rs.getInt("history_no");
                int usage_time = rs.getInt("usage_time");
                int member_no = rs.getInt("member_no");
                Date usage_date = rs.getDate("usage_date");


                history = new UsageHistoryDTO(history_no, member_no, usage_time, usage_date);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return history;
    }

    @Override
    public boolean delete(String s) {
        return false;
    }

    @Override
    public boolean update(UsageHistoryDTO usageHistoryDTO) {
        return false;
    }



    @Override
    public boolean insert(UsageHistoryDTO usageHistoryDTO) {
        return false;
    }

    @Override
    public ArrayList<UsageHistoryDTO> findAll() {
        ArrayList<UsageHistoryDTO> histories = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String sql = "select * from usage_history";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int history_no = rs.getInt("history_no");
                int usage_time = rs.getInt("usage_time");
                int member_no = rs.getInt("member_no");
                Date usage_date = rs.getDate("usage_date");

                UsageHistoryDTO historyDTO = new UsageHistoryDTO(history_no, member_no, usage_time, usage_date);
                histories.add(historyDTO); //arraylist에 저장
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return histories;
    }
}
