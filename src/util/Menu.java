package util;

import sql.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

@FunctionalInterface
interface Menu_util1{
    void ShowMenu(SQL mysql);
}

@FunctionalInterface
interface Menu_util2{
    void Initialize(SQL mysql);
}

public class Menu {
    public static String username;
    public static List<Vehicles> cars = new ArrayList<>();
    public static void GetMenu(SQL mysql) {
        Menu_util1 menu = (SQL sql) -> {
            while (true) {
                System.out.println("\n=== 主菜单 ===");
                System.out.println("1. 查看所有车辆");
                System.out.println("2. 查看车辆详细信息");
                System.out.println("3. 租赁车辆");
                System.out.println("4. 支付租金");
                System.out.println("5. 退出系统");
                System.out.print("请选择操作：");
                int choice = Integer.parseInt(System.console().readLine());
                switch (choice) {
                    case 1:
                        ShowAllCars(mysql);
                        continue;
                    case 2:
                        ShowCarDetail(mysql);
                        continue;
                    case 3:
                        RentACar(mysql);
                        continue;
                    case 4:
                        Pay(mysql);
                        continue;
                    case 5:
                        mysql.Update(cars);
                        System.out.println("欢迎下次光临！");
                        System.exit(0);
                    default:
                        System.out.println("输入错误，请重新输入！");
                }
            }
        };
        Menu_util2 util = (SQL sql) -> { // 初始化菜单
            try {
                ResultSet set = mysql.carQuery(new String[]{"*","carserver.menu"});
                while (set.next()) {
                    if (set.getString("car_type").equals("轿车"))
                        cars.add(new Coupe(set.getInt("id"),
                                set.getString("car_name"),
                                set.getString("registration"),
                                set.getInt("rent_pd")));

                    else if (set.getString("car_type").equals("客车"))
                        cars.add(new Bus(set.getInt("id"),
                                set.getString("car_name"),
                                set.getString("registration"),
                                set.getInt("rent_pd")));
                }
                set.close();
                set = mysql.userQuery(new String[]{"*","user.util"});
                if(set.next()) {
                    do{
                        for(Vehicles v : cars) {
                            if(v.id == set.getInt("id")) {
                                v.is_available = false;
                                v.start_date = LocalDate.parse(set.getString("start_date"));
                                v.rent_type = RentType.fromString(set.getString("rent_type"));
                                if(v.rent_type == RentType.Long_term)
                                    v.day = null;
                                else
                                    v.day = set.getInt("Days_set");
                                v.Check();
                            }
                        }
                    }while(set.next());
                }
                else {
                    for (Vehicles v : cars) {
                        v.day = null;
                        v.is_available = true;
                        v.start_date = null;
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("初始化失败！ 请联系管理员处理", e);
            }
        };
        util.Initialize(mysql);
        menu.ShowMenu(mysql);
    }

    static void ShowAllCars(@org.jetbrains.annotations.NotNull SQL mysql) {
        System.out.println("本系统所有车辆信息如下：");
        System.out.printf("%-4s%-10s%-20s%-15s%n", "ID", "类型", "名称", "牌照");
        System.out.println("--------------------------------------------------");
        ResultSet res = mysql.carQuery(new String[]{"*","carserver.menu"});
        try {
            while (res.next()) {
                System.out.printf("%-4s%-10s%-20s%-15s%n",
                        res.getString("id") ,
                        res.getString("car_name") ,
                        res.getString("car_type") ,
                        res.getString("registration")
                );
            }
            res.close();
            System.out.println("按任意鍵返回上一级菜单...");
            System.console().readLine();
        }
        catch(SQLException e){
            throw new RuntimeException("查询失败！ 请联系管理员处理", e);
        }
    }

    static void ShowCarDetail(SQL mysql) {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入车辆ID：");
        int id = sc.nextInt();
        for (Vehicles v : cars) {
            if (v.id == id) {
                System.out.println("车辆详细信息如下：");
                v.GetInfo();
                System.out.println("按任意鍵返回上一级菜单...");
                System.console().readLine();
            }
        }

    }
    static void RentACar(SQL mysql) {
        System.out.println("请输入车辆ID：");
        Scanner sc = new Scanner(System.in);
        int id = sc.nextInt();
        boolean flag = false;
        for (Vehicles v : cars) {
            if (v.id == id) {
                flag = true;
                if(v.is_available) {
                    System.out.println("请选择租借方式: \n 1. 定期租赁 \n 2. 长期租赁");
                    int choice = sc.nextInt();
                    if(choice == 1) {
                        System.out.println("请输入租期（天）：");
                        int day = sc.nextInt();
                        v.SetDay_1(day);
                        break;
                    }
                    else if(choice == 2) {
                        v.SetDay_2();
                        break;
                    }
                    else {
                        System.out.println("输入错误，请重新输入！");
                    }
                }
                else {
                    System.out.println("该车辆已被租用！");
                }
            }
        }
        if (!flag) {
            System.out.println("该车辆不存在！");
        }
        System.out.println("按任意鍵返回上一级菜单...");
        System.console().readLine();
    }
    static void Pay(SQL mysql) {
        try{
            System.out.println("当前账户租用车辆ID为:");
            Double sum = 0.0;
            for (Vehicles v : cars) {
                if (!v.is_available) {
                    sum += v.Calculate();
                }
            }
//            ResultSet xst =mysql.userQuery(new String[]{"username","user.util"});
//            if(xst.next()) {
//                do {
//                    for (Vehicles v : cars) {
//                        if (v.id == xst.getInt("id")) {
//                            sum += v.Calculate();
//                        }
//                    }
//                } while (xst.next());
//            }
            System.out.println("总租金为：" + sum + "\n 按任意键完成支付");
            System.console().readLine();
        } catch (Exception e) {
            throw new RuntimeException("获取车辆信息失败！ 请联系管理员处理", e);
        }
    }
}
