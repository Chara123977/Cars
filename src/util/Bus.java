package util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Bus extends Vehicles { //Coupe代码与Bus相比 只有中间的op计算逻辑不同
    public Bus(int id, String name, String registration, int rent) {
        super(id, name, registration, rent);
        this.type = CarType.Bus;
    }
    Calculator op = (rent, day) -> {
        if (day >= 150)
            return rent * day * 0.6;
        else if (day >= 30)
            return rent * day * 0.7;
        else if (day >= 7)
            return rent * day * 0.8;
        else if (day >= 3)
            return rent * day * 0.9;
        else
            return rent * day * 1.0;
    };
    void GetInfo() {
        System.out.println("车辆名称：" + this.name);
        System.out.println("牌照：" + this.registration);
        System.out.println("车型：" + this.type);
        System.out.println("日租金：" + this.rent + "元/天");
        if (this.rent_type != null) {
            System.out.println("租车方式：" + this.rent_type);
            System.out.println("起租日期" + this.start_date.toString());
        }
        if (this.rent_type == RentType.Regular && this.is_available)
            System.out.print("距离到期还有:" +
                    ChronoUnit.DAYS.between(this.start_date.plusDays(this.day), LocalDate.now()) +
                    1 +
                    "天\n (不足一天的时间按一天算)");
        if (this.rent_type == RentType.Long_term && this.is_available)
            System.out.print("已租用" + ChronoUnit.DAYS.between(LocalDate.now(), this.start_date) +
                    1 +
                    "天\n (不足一天的时间按一天算)");
    }
    Double Calculate() {
        if (this.rent_type==RentType.Regular)
            return op.Calculate(this.rent, this.day);
        else
            return op.Calculate(
                    this.rent,
                    ChronoUnit.DAYS.between(LocalDate.now(), this.start_date)+1
            );
    }
}
