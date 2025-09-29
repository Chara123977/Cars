package util;

import java.time.LocalDate;

@FunctionalInterface
interface Calculator {
    Double Calculate(int rent,long day);
}
public abstract class Vehicles {
    Integer id; // 车辆编号
    String name; // 车辆名称
    String registration; // 牌照
    Integer rent; // 日租金
    Integer day; // 租车天数
    LocalDate start_date; // 起始日期
    Boolean is_available = true; // 是否可用
    CarType type; // 车型
    RentType rent_type = null; //以何种方式租用
    public Vehicles(int id, String name, String registration, int rent) {
        this.id = id;
        this.name = name;
        this.registration = registration;
        this.rent = rent;
    }
    void SetDay_1(int day) {
        this.day = day;
        this.start_date = LocalDate.now();
        this.is_available = false;
        this.rent_type = RentType.Regular;
    }//定时租用
    void SetDay_2() {
        this.start_date = LocalDate.now();
        this.is_available = false;
        this.rent_type = RentType.Long_term;
    }//无上限租用
    void Renew(int time) {
        if(this.rent_type== RentType.Regular)
            this.day += time;
        else if(this.rent_type==RentType.Long_term)
            throw new RuntimeException("当前为无上限租用，不能续租");
        else
            throw new RuntimeException("未租用车辆");
    }//续租
    void Check(){
        if (this.rent_type==RentType.Regular){
            is_available = !(this.start_date.isBefore(LocalDate.now()) || this.start_date.isEqual(LocalDate.now()));
        }
    }//检查是否到期

    abstract void GetInfo();
    abstract Double Calculate();

    public Integer getId() {
        return id;
    }

    public Integer getDay() {
        return day;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public Boolean Is_available() {
        return is_available;
    }

    public String getRent_type() {
        return rent_type.toString();
    }
}

