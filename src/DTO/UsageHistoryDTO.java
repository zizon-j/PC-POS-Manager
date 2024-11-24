package DTO;

public class UsageHistoryDTO {
    private int history_no, member_no, usage_time;
    private java.sql.Date usage_date;

    public UsageHistoryDTO(int history_no, int member_no, int usage_time, java.sql.Date usage_date) {
        this.history_no = history_no;
        this.member_no = member_no;
        this.usage_time = usage_time;
        this.usage_date = usage_date;
    }
    //사용기록 번호
    public int getHistory_no() {
        return history_no;
    }
    public void setHistory_no(int history_no) {
        this.history_no = history_no;
    }

    //유저 번호
    public int getMember_no() {
        return member_no;
    }
    public void setMember_no(int member_no) {
        this.member_no = member_no;
    }

    //사용 시간
    public int getUsage_time() {
        return usage_time;
    }
    public void setUsage_time(int usage_time) {
        this.usage_time = usage_time;
    }

    //사용 날짜
    public java.sql.Date getUsage_date() {
        return usage_date;
    }
    public void setUsage_date(java.sql.Date usage_date) {
        this.usage_date = usage_date;
    }
}
