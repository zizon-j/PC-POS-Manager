package DAO;

import DTO.MemberDTO;
import DTO.SeatDTO;
import com.mysql.cj.protocol.Resultset;

import java.sql.*;
import java.util.ArrayList;

public class SeatDAO implements DAO<SeatDTO, String> {

    private final Connection conn;

    public SeatDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean insert(SeatDTO seat) {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO seat (x, y) VALUES (?, ?);";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, seat.getX());
            pstmt.setInt(2, seat.getY());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("좌석 안만들어짐");
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
    public SeatDTO findById(String seat_no_search) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        SeatDTO seat = null;
        try {
            String sql = "select * from seat where seat_no = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, seat_no_search);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                int seat_no = rs.getInt("seat_no");
                String seat_state = rs.getString("seat_state");
                int member_no = rs.getInt("member_no");
                int x = rs.getInt("x");
                int y = rs.getInt("y");

                seat = new SeatDTO(seat_no, seat_state, member_no, x, y);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return seat;
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
        ArrayList<SeatDTO> seats = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM seat";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int seat_no = rs.getInt("seat_no");
                String seat_state = rs.getString("seat_state");
                int member_no = rs.getInt("member_no");
                int x = rs.getInt("x");
                int y = rs.getInt("y");

                SeatDTO seat = new SeatDTO(seat_no, seat_state, member_no, x, y);
                seats.add(seat);
                //arraylist에 저장
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
        return seats;
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

    public boolean resetAuto_increment() {
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

    public ArrayList<SeatDTO> choose_not_using() {
        ArrayList<SeatDTO> seats_not_using = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "select seat_no from seat where seat_state='미사용' ";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int seat_no = rs.getInt("seat_no");

                SeatDTO seat = new SeatDTO(seat_no);
                seats_not_using.add(seat);
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

    public boolean updateSeat2(int seat_no) {
        PreparedStatement pstmt = null;
        try {
            String sql = "update seat set seat_state = '미사용', member_no = null where seat_no = ?";
            pstmt = conn.prepareStatement(sql);


            pstmt.setInt(1, seat_no);

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


    public boolean update3(SeatDTO seat) {
        PreparedStatement pstmt = null;
        try {
            String sql = "update seat set seat_state = '비활성화' where seat_no = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, seat.getSeat_no());

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

    public boolean update4(SeatDTO seat) {
        PreparedStatement pstmt = null;
        try {
            String sql = "update seat set seat_state = '미사용' where seat_no = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, seat.getSeat_no());

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
