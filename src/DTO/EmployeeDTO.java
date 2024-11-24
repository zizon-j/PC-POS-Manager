package DTO;

import java.sql.Date;

public class EmployeeDTO {
    // 필드 선언
    private int emp_no;
    private String emp_name, emp_id, emp_pwd, emp_phone, priority;

    // 기본 생성자
    public EmployeeDTO() {
    }

    // 모든 필드를 초기화하는 생성자
    public EmployeeDTO(int emp_no, String emp_name, String emp_id, String emp_pwd, String emp_phone, String priority) {
        this.emp_no = emp_no;
        this.emp_name = emp_name;
        this.emp_id = emp_id;
        this.emp_pwd = emp_pwd;
        this.emp_phone = emp_phone;
        this.priority = priority;

    }

    // Getter와 Setter 메서드들
    public int getEmp_no() {
        return emp_no;
    }

    public void setEmp_no(int emp_no) {
        this.emp_no = emp_no;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getEmp_pwd() {
        return emp_pwd;
    }

    public void setEmp_pwd(String emp_pwd) {
        this.emp_pwd = emp_pwd;
    }

    public String getEmp_phone() {
        return emp_phone;
    }

    public void setEmp_phone(String emp_phone) {
        this.emp_phone = emp_phone;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
