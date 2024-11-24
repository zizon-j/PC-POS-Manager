package DAO;

import DTO.EmployeeDTO;
import java.sql.*;
import java.util.ArrayList;

public class EmployeeDAO implements DAO<EmployeeDTO, String> {
    private Connection conn;

    public EmployeeDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean insert(EmployeeDTO employee) {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO employee (emp_name, emp_id, emp_pwd, emp_phone, priority) VALUES (?,?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, employee.getEmp_name());
            pstmt.setString(2, employee.getEmp_id());
            pstmt.setString(3, employee.getEmp_pwd());
            pstmt.setString(4, employee.getEmp_phone());
            pstmt.setString(5, employee.getPriority());

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

    @Override
    public EmployeeDTO findById(String emp_id_search) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        EmployeeDTO employee = null;

        try {
            String sql = "SELECT * FROM employee WHERE emp_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, emp_id_search);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int emp_no = rs.getInt("emp_no");
                String emp_name = rs.getString("emp_name");
                String emp_id = rs.getString("emp_id");
                String emp_pwd = rs.getString("emp_pwd");
                String emp_phone = rs.getString("emp_phone");
                String priority = rs.getString("priority");

                employee = new EmployeeDTO(emp_no, emp_name, emp_id, emp_pwd, emp_phone, priority);
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
        return employee;
    }

    @Override
    public ArrayList<EmployeeDTO> findAll() {
        ArrayList<EmployeeDTO> employees = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM employee";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int emp_no = rs.getInt("emp_no");
                String emp_name = rs.getString("emp_name");
                String emp_id = rs.getString("emp_id");
                String emp_pwd = rs.getString("emp_pwd");
                String emp_phone = rs.getString("emp_phone");
                String priority = rs.getString("priority");

                EmployeeDTO employee = new EmployeeDTO(emp_no, emp_name, emp_id, emp_pwd, emp_phone, priority);
                employees.add(employee);
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
        return employees;
    }

    @Override
    public boolean delete(String s) {
        return false;
    }

    @Override
    public boolean update(EmployeeDTO employeeDTO) {
        return false;
    }
}