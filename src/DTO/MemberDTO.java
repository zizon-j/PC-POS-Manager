package DTO;

import java.util.Date;

public class MemberDTO {

    //dao 와 db 사이의 데이터를 주고 받는 클래스
    //로직을 가지지 않는 순수한 getter setter 만 있는 클래스

    //유저가 자신의 브라우저에서 데이터를 입력하여 form에 잇는ㄷ 데이터를 dto 에 전달해서 저장한다.
    // dto를 받은 서버가 dao를 이용해 db로 데이터를 저장

    //main에서 dao 호출
    //dao에서 db 접근해서 데이터 가져옴
    //dto에서 데이터 받아서 저장하기 위한 테이블 만들기
    //dao에서 전달받은 dto를 사용해 db로 저장 (insert)
    int member_no, left_time;
    String member_name, member_id, member_pwd, sex, phone, address;
    java.sql.Date birthday, reg_date;

    public int getMember_no(){
        return member_no;
    }
    public void setMember_no(int setMember_no){
        this.member_no = setMember_no;
    }

    // Getter and Setter for left_time
    public int getLeft_time() {
        return left_time;
    }
    public void setLeft_time(int left_time) {
        this.left_time = left_time;
    }

    // Getter and Setter for member_name
    public String getMember_name() {
        return member_name;
    }
    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    // Getter and Setter for member_id
    public String getMember_id() {
        return member_id;
    }
    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    // Getter and Setter for member_pwd
    public String getMember_pwd() {
        return member_pwd;
    }
    public void setMember_pwd(String member_pwd) {
        this.member_pwd = member_pwd;
    }

    // Getter and Setter for sex
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }

    // Getter and Setter for phone
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Getter and Setter for address
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    // Getter and Setter for birthday
    public java.sql.Date getBirthday() {
        return birthday;
    }
    public void setBirthday(java.sql.Date birthday) {
        this.birthday = birthday;
    }

    // Getter and Setter for reg_date
    public java.sql.Date getReg_date() {
        return reg_date;
    }
    public void setReg_date(java.sql.Date reg_date) {
        this.reg_date = reg_date;
    }


}
