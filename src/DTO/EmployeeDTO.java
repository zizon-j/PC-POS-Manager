package DTO;

public class EmployeeDTO {

    int emp_no;
    String emp_name, emp_id, emp_pwd, priority;

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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
