package DAO;

import DTO.MemberDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class MemberDAO implements DAO<MemberDTO, String>{
    //db 연결을 위한 connection을 가져온다
    //sql문을 담은 Preparedstatement를 만든다
    //만들어진 Preparedstatement를 실행한다.
    //조회의 경우 SQL쿼리의 실행 결과를 REsultSet으로 받아서 저장할 오브젝트에 옮겨준다
    //작업중에 생성된 Connection, PreparedStatement, ResultSet같은 리소스는 작업을 마친 후 반드시 닫아준다.
    //JDBC API가 만들어내는 예외를 잡아서 직접처리 or throws를 사용해 던짐

    //DAO가 DB에서 데이터를 가져옴
    //가져온 데이터를 DTO로 만들어서 반환
    private Connection conn;

    public MemberDAO(Connection conn){
        this.conn = conn;
    }

    //회원가입 RegisterFrame
    @Override
    public boolean insert(MemberDTO member) {
        PreparedStatement pstmt = null;
        try{
            String sql = "INSERT INTO member_test (member_name, member_id, member_pwd, birthday, sex, phone, address) VALUES (?,?,?,?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, member.getMember_name());
            pstmt.setString(2, member.getMember_id());
            pstmt.setString(3, member.getMember_pwd());
            pstmt.setDate(4,member.getBirthday());
            pstmt.setString(5, member.getSex());
            pstmt.setString(6, member.getPhone());
            pstmt.setString(7, member.getAddress());

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
        return true;
    }

    @Override
    public MemberDTO findById(String s) {
        return null;
    }

    @Override
    public boolean delete(String s) {
        return false;
    }

    @Override
    public boolean update(MemberDTO memberDTO) {
        return false;
    }

    @Override
    public ArrayList<MemberDTO> findAll() {
        return null;
    }
}
