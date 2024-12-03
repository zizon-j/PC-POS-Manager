package DTO;

import DAO.TimeDAO;

public class TimeDTO {
    int money, plus_time;

    public TimeDTO() {
    }

    public TimeDTO(int money, int plus_time) {
        this.money = money;
        this.plus_time = plus_time;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getPlus_time() {
        return plus_time;
    }

    public void setPlus_time(int plus_time) {
        this.plus_time = plus_time;
    }
}
