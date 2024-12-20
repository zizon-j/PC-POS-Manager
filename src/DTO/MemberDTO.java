package DTO;

import java.sql.Date;

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

    public MemberDTO() {
    }

    public MemberDTO(int member_no, String member_name, String member_id, String member_pwd, Date birthday, String sex, Date reg_date, String phone, String address, int left_time) {
        this.member_no = member_no;
        this.member_name = member_name;
        this.member_id = member_id;
        this.member_pwd = member_pwd;
        this.birthday = birthday;
        this.sex = sex;
        this.reg_date = reg_date;
        this.phone = phone;
        this.address = address;
        this.left_time = left_time;

    }


    public int getMember_no() {
        return member_no;
    }

    public void setMember_no(int setMember_no) {
        this.member_no = setMember_no;
    }

    public int getLeft_time() {
        return left_time;
    }

    public void setLeft_time(int left_time) {
        this.left_time = left_time;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getMember_pwd() {
        return member_pwd;
    }

    public void setMember_pwd(String member_pwd) {
        this.member_pwd = member_pwd;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public java.sql.Date getBirthday() {
        return birthday;
    }

    public void setBirthday(java.sql.Date birthday) {
        this.birthday = birthday;
    }

    public java.sql.Date getReg_date() {
        return reg_date;
    }

    public void setReg_date(java.sql.Date reg_date) {
        this.reg_date = reg_date;
    }


}
