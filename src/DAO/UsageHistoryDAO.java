package DAO;

import DTO.MemberDTO;
import DTO.UsageHistoryDTO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class UsageHistoryDAO implements DAO<UsageHistoryDTO, String> {
    private final Connection conn;

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

            if (rs.next()) {
                int history_no = rs.getInt("history_no");
                int usage_time = rs.getInt("usage_time");
                int member_no = rs.getInt("member_no");
                Date usage_date = rs.getDate("usage_date");


                history = new UsageHistoryDTO(history_no, member_no, usage_time, usage_date);
            }
        } catch (SQLException e) {
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
    public boolean update(UsageHistoryDTO usageHistoryDTO) { //로그아웃
        PreparedStatement pstmt = null;
        try {
            String sql = "update usage_history " +
                    "set end_time = NOW(), state = '사용종료' " +
                    "where member_no = ? and state = '사용중'";
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, usageHistoryDTO.getMember_no());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("종료 시간 안찍힘");
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return false;
    }


    @Override
    public boolean insert(UsageHistoryDTO usageHistoryDTO) {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO usage_history (member_no, start_time) VALUES (?, NOW());";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, usageHistoryDTO.getMember_no());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("사용 시작 시간 안찍힘");
            return false;
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
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

    public int calTotalUsageTime(int memberNo) { // 총 사용시간 계산 메서드
        PreparedStatement ps = null;
        ResultSet rs = null;
        int totalUsageTime = 0;

        try {
            String sql = "select sum(timestampdiff(minute, start_time, end_time)) as total_time from usage_history where member_no = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, memberNo);
            rs = ps.executeQuery();

            if (rs.next())
                totalUsageTime = rs.getInt("total_time");
        } catch (SQLException e) {
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
        return totalUsageTime;
    }

    public LocalDateTime getLoginTime(MemberDTO member_no) { // 로그인 시간을 가져오는 메서드
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LocalDateTime loginTime = null;


        try {
            String sql = "select start_time from usage_history where member_no = ? order by start_time desc limit 1";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, member_no.getMember_no());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                loginTime = rs.getTimestamp("start_time").toLocalDateTime();
            }

        } catch (SQLException e) {
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
        return loginTime;
    }
}
