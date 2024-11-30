package DAO;

import DTO.MemberDTO;
import DTO.TimeDTO;

import java.sql.*;
import java.util.ArrayList;

public class MemberDAO implements DAO<MemberDTO, String> {
    //db 연결을 위한 connection을 가져온다
    //sql문을 담은 Preparedstatement를 만든다
    //만들어진 Preparedstatement를 실행한다.
    //조회의 경우 SQL쿼리의 실행 결과를 REsultSet으로 받아서 저장할 오브젝트에 옮겨준다
    //작업중에 생성된 Connection, PreparedStatement, ResultSet같은 리소스는 작업을 마친 후 반드시 닫아준다.
    //JDBC API가 만들어내는 예외를 잡아서 직접처리 or throws를 사용해 던짐

    //DAO가 DB에서 데이터를 가져옴
    //가져온 데이터를 DTO로 만들어서 반환
    private final Connection conn;

    public MemberDAO(Connection conn) {
        this.conn = conn;
    }

    //회원가입 RegisterFrame
    @Override
    public boolean insert(MemberDTO member) {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO member (member_name, member_id, member_pwd, birthday, sex, phone, address) VALUES (?,?,?,?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, member.getMember_name());
            pstmt.setString(2, member.getMember_id());
            pstmt.setString(3, member.getMember_pwd());
            pstmt.setDate(4, member.getBirthday());
            pstmt.setString(5, member.getSex());
            pstmt.setString(6, member.getPhone());
            pstmt.setString(7, member.getAddress());

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
        }
        return true;
    }

    //검색
    //member_id로 검색
    //executequery(): select 쿼리문 실행
    // 쿼리를 실행하고, r겨로가를 ResultSet 객체로 변환

    //executeUpdate(): insert, update, delete와 같은 dml에서 실행 결과로 영향을받은 레코드 수를변환
    //행의 개수를 반환하기 때문에 rs를 사용할 필요가 없다.
    @Override
    public MemberDTO findById(String member_no_search) { // 회원이 사용하는 pc에서 회원가입 시 필요한 메서드
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        MemberDTO member = null;

        try {
            String sql = "SELECT * FROM member WHERE member_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, member_no_search);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int member_no = rs.getInt("member_no");
                String member_name = rs.getString("member_name");
                String member_id = rs.getString("member_id");
                String member_pwd = rs.getString("member_pwd");
                Date birthday = rs.getDate("birthday");
                String sex = rs.getString("sex");
                Date reg_date = rs.getDate("reg_date");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                int left_time = rs.getInt("left_time");

                member = new MemberDTO(member_no, member_name, member_id, member_pwd, birthday, sex, reg_date, phone, address, left_time);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return member;
    }


    @Override
    public boolean delete(String s) { //회원 삭제
        PreparedStatement pstmt = null; //SQL문 바구니
        try {
            String sql = "DELETE FROM member WHERE member_no = ?"; //쿼리문
            pstmt = conn.prepareStatement(sql); //바구니에 담아서
            pstmt.setString(1, s);
            pstmt.executeUpdate(); //실행

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) //정상작동 했다면
                    pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public boolean update(MemberDTO memberDTO) { //회원 수정


        return false;
    }

    public boolean updateMemberInfo(MemberDTO memberDTO) { //회원 수정
        PreparedStatement pstmt = null;

        try {
            String sql = "update member " +
                    "set member_pwd = ?, phone = ?, address = ?, sex = ? " +
                    "where member_no = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, memberDTO.getMember_pwd());
            pstmt.setString(2, memberDTO.getPhone());
            pstmt.setString(3, memberDTO.getAddress());
            pstmt.setString(4, memberDTO.getSex());
            pstmt.setInt(5, memberDTO.getMember_no());

            int editRow = pstmt.executeUpdate();
            return editRow > 0; //수정된 행이 있다면 true 반환
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
        }
    }

    @Override
    public ArrayList<MemberDTO> findAll() {
        ArrayList<MemberDTO> members = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM member";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int member_no = rs.getInt("member_no");
                String member_name = rs.getString("member_name");
                String member_id = rs.getString("member_id");
                String member_pwd = rs.getString("member_pwd");
                Date birthday = rs.getDate("birthday");
                String sex = rs.getString("sex");
                Date reg_date = rs.getDate("reg_date");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                int left_time = rs.getInt("left_time");

                MemberDTO member = new MemberDTO(member_no, member_name, member_id, member_pwd, birthday, sex, reg_date, phone, address, left_time);
                members.add(member);
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
        return members;


    }

    public MemberDTO findByNo(String member_no_search) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        MemberDTO member = null;

        try {
            String sql = "SELECT * FROM member WHERE member_no = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, member_no_search);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int member_no = rs.getInt("member_no");
                String member_name = rs.getString("member_name");
                String member_id = rs.getString("member_id");
                String member_pwd = rs.getString("member_pwd");
                Date birthday = rs.getDate("birthday");
                String sex = rs.getString("sex");
                Date reg_date = rs.getDate("reg_date");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                int left_time = rs.getInt("left_time");

                member = new MemberDTO(member_no, member_name, member_id, member_pwd, birthday, sex, reg_date, phone, address, left_time);
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
        return member;
    }

    public boolean update_left_time(MemberDTO member, TimeDTO time) { //회원 수정
        PreparedStatement pspmt = null;
        try {
            String sql = "update member set left_time = left_time + ? where member_id = ?";
            pspmt = conn.prepareStatement(sql);
            pspmt.setInt(1, time.getPlus_time());
            pspmt.setString(2, member.getMember_id());

            pspmt.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pspmt != null)
                    pspmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public MemberDTO joinSeat(String seat_no_search) { // 회원이 사용하는 pc에서 회원가입 시 필요한 메서드
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        MemberDTO member = null;

        try {
            String sql = "select * from member m join pc_pos_db.seat s on m.member_no = s.member_no where s.seat_no= ? ;";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, seat_no_search);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int member_no = rs.getInt("member_no");
                String member_name = rs.getString("member_name");
                String member_id = rs.getString("member_id");
                String member_pwd = rs.getString("member_pwd");
                Date birthday = rs.getDate("birthday");
                String sex = rs.getString("sex");
                Date reg_date = rs.getDate("reg_date");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                int left_time = rs.getInt("left_time");

                member = new MemberDTO(member_no, member_name, member_id, member_pwd, birthday, sex, reg_date, phone, address, left_time);
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
        return member;
    }
    
}
