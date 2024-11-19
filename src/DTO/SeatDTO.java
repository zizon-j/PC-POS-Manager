package DTO;

public class SeatDTO {
    int seat_no, member_no;
    String seat_state;

    public int getSeat_no() {
        return seat_no;
    }

    public void setSeat_no(int seat_no) {
        this.seat_no = seat_no;
    }

    public int getMember_no() {
        return member_no;
    }

    public void setMember_no(int member_no) {
        this.member_no = member_no;
    }

    public String getSeat_state() {
        return seat_state;
    }

    public void setSeat_state(String seat_state) {
        this.seat_state = seat_state;
    }
}
