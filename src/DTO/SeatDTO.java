package DTO;

public class SeatDTO {
    int seat_no, member_no, x, y;
    String seat_state;

    public SeatDTO(){}

    public SeatDTO( int seat_no
    ){
        this.seat_no = seat_no;
    }

    public SeatDTO(int seat_no, String seat_state, int member_no, int x, int y){
        this.seat_no = seat_no;
        this.seat_state = seat_state;
        this.member_no = member_no;
        this.x = x;
        this.y = y;
    }

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

    public int getX(){
        return x;
    }

    public void setX(int x){
        this.x = x;
    }

    public int getY(){
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
