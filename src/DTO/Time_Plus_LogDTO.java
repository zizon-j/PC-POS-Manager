package DTO;

import Common_Panel.Time_Plus_Jpanel;

public class Time_Plus_LogDTO {
    int time_plus_log, money;
    String member_id;

    public Time_Plus_LogDTO() {
    }

    public Time_Plus_LogDTO(int time_plus_log, String member_id, int money) {
        this.time_plus_log = time_plus_log;
        this.member_id = member_id;
        this.money = money;
    }

    // Getter and Setter for time_plus_log
    public int getTime_plus_log() {
        return time_plus_log;
    }

    public void setTime_plus_log(int time_plus_log) {
        this.time_plus_log = time_plus_log;
    }

    // Getter and Setter for money
    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    // Getter and Setter for member_id
    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

}
