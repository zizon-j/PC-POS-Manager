package DAO;

import DTO.MemberDTO;
import DTO.SeatDTO;
import com.mysql.cj.protocol.Resultset;

import java.sql.*;
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
    public boolean update(SeatDTO seat) {
        PreparedStatement pstmt = null;
        try {
            String sql = "update seat set seat_state = '사용중', member_no = ? where seat_no = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, seat.getMember_no());
            pstmt.setInt(2, seat.getSeat_no());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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

    public ArrayList<SeatDTO> choose_not_using(){
        ArrayList<SeatDTO> seats_not_using = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "select seat_no from seat where seat_state='미사용' ";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()){
                int seat_no = rs.getInt("seat_no");

                SeatDTO seat = new SeatDTO(seat_no);
                seats_not_using.add(seat);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return seats_not_using;

    }

    public boolean updateSeat(int member_no, int seat_no) {
        PreparedStatement pstmt = null;
        try {
            String sql = "update seat set seat_state = '사용중', member_no = ? where seat_no = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, member_no);
            pstmt.setInt(2, seat_no);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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

}
