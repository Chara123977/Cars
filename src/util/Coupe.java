package util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Coupe extends Vehicles  {
    public Coupe(int id, String name, String registration, int rent) {
        super(id, name, registration, rent);
        this.type = CarType.Coupe;
    }
    Calculator op = (rent, Day) -> {
        if (Day > 150)
            return rent * Day * 0.7;
        else if (Day > 30)
            return rent * Day * 0.8;
        else if (Day > 7)
            return rent * Day * 0.9;
        else
            return rent * Day * 1.0;
    };
    void GetInfo() {
        System.out.println("车辆名称：" + this.name);
        System.out.println("牌照：" + this.registration);
        System.out.println("车型：" + this.type.toString());
        System.out.println("日租金：" + this.rent + "元/天");
        if (this.rent_type != null) {
            System.out.println("租车方式：" + this.rent_type.toString());
            System.out.println("起租日期" + this.start_date.toString());
        }
        if (this.rent_type == RentType.Regular && this.is_available)
            System.out.print("距离到期还有:" + ChronoUnit.DAYS.between(this.start_date.plusDays(this.day), LocalDate.now()) + 1 + "天\n (不足一天的时间按一天算)");
        if (this.rent_type == RentType.Long_term && this.is_available)
            System.out.print("已租用" + ChronoUnit.DAYS.between(LocalDate.now(), this.start_date) + 1 + "天\n (不足一天的时间按一天算)");
    }
    Double Calculate() {
        if (this.rent_type == RentType.Regular)
            return op.Calculate(this.rent, this.day);
        else
            return op.Calculate(
                    this.rent,
                    ChronoUnit.DAYS.between(this.start_date.plusDays(this.day), LocalDate.now())+1
            );
    }
}
