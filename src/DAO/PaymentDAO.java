package DAO;

import DTO.PaymentDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PaymentDAO implements DAO<PaymentDTO, String> {
    private Connection conn;
    @Override
    public PaymentDTO findById(String s) {
        return null;
    }

    @Override
    public boolean delete(String s) {
        return false;
    }

    @Override
    public boolean update(PaymentDTO paymentDTO) {
        return false;
    }

    @Override
    public boolean insert(PaymentDTO paymentDTO) {
        return false;
    }

    @Override
    public ArrayList<PaymentDTO> findAll() {
        return null;
    }

    //로직 수정으로 인한 주석처리
    /*
    public double calTotalUsageMoney_(int memberNo) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        double totalUsageMoney = 0.0;

        try {
            String sql = "";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, memberNo);
            rs = ps.executeQuery();

            if (rs.next())
                totalUsageMoney = rs.getDouble(1);
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

        return totalUsageMoney;
    } */
}