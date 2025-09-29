package sql;

import org.jetbrains.annotations.NotNull;
import util.Menu;
import util.Vehicles;

import java.sql.*;
import java.util.List;
import java.util.regex.*;

public class SQL {
    Connection cn, //连到车辆数据库里
            cn1; //连到用户数据库里
    PreparedStatement st, //对车辆操作
            st1; //用户操作
    ResultSet rs ,
            rs1;
    public SQL() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/carserver?characterEncoding=UTF-8&useSSL=false&useUnicode=true&serverTimezone=UTC", "root", "8766306Mm");
            cn1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/user?characterEncoding=UTF-8&useSSL=false&useUnicode=true&serverTimezone=UTC", "root", "8766306Mm");
        } catch (Exception e) {
            throw new RuntimeException("无法连接数据库 请检查网络连接或通知管理员", e);
        }
    }

    public boolean Login(String username, String password) {
        try {
            String sql = "SELECT * FROM user.userinfo WHERE username =? AND password =?";
            st1 = cn1.prepareStatement(sql);
            st1.setString(1, username); // 设置第一个参数
            st1.setString(2, password); // 设置第二个参数
            rs1 = st1.executeQuery();
            return rs1.next();
        } catch (Exception e) {
            throw new RuntimeException("程序崩溃码0 请联系管理员", e);
        }
    }


    public boolean Register(String newUsername, String newPassword) {
        String regex_username = "^(?=.*[a-z])(?=.*\\d)[a-z0-9]+$";
        String regex_password = "^(?=.*[a-z])(?=.*\\d)[a-z0-9]{6,}$";
        Pattern regex_username_pattern = Pattern.compile(regex_username);
        Pattern regex_password_pattern = Pattern.compile(regex_password);
        if (regex_username_pattern.matcher(newUsername).matches() && regex_password_pattern.matcher(newPassword).matches()) {
            try {
                String sql = "INSERT INTO user.userinfo (username, password) VALUES (?, ?)";
                st1 = cn1.prepareStatement(sql);
                st1.setString(1, newUsername); // 设置第一个参数
                st1.setString(2, newPassword); // 设置第二个参数
                int i = st1.executeUpdate();
                if (i > 0) {
                    System.out.println("注册成功");
                    st1.close();
                    return true;
                }
            } catch (Exception e) {
                System.out.println("用户名重复");
            }
        } else {
            System.out.println("用户名或密码不符合要求");
        }
        return false;
    }

    public void Close(){
        try {
            if (rs!= null) {
                rs.close();
            }
            if (rs1 != null) {
                rs1.close();
            }
            if (st!= null) {
                st.close();
            }
            if (cn!= null) {
                cn.close();
            }
            if (st1!= null) {
                st1.close();
            }
            if (cn1!= null) {
                cn1.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("无法关闭资源 程序即将终止", e);
        }
    }

    public void Update(List<Vehicles> cars) {
        try{
            for (Vehicles car : cars) {
                if (car.Is_available() == false) {
                    String sql = "INSERT INTO user.util (u_name, car_id, start_day, rent_type, Days_set) " +
                            "VALUES (?, ?, ?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE " +
                            "u_name = VALUES(u_name), " +
                            "start_day = VALUES(start_day), " +
                            "rent_type = VALUES(rent_type), " +
                            "Days_set = VALUES(Days_set)";
                    st1 = cn1.prepareStatement(sql);
                    st1.setString(1, Menu.username);
                    st1.setInt(2, car.getId());
                    st1.setDate(3, Date.valueOf(car.getStart_date()));
                    st1.setString(4, car.getRent_type());
                    st1.setInt(5, car.getDay());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("程序崩溃码1 请联系管理员", e);
        }
    }

    public ResultSet carQuery(String @NotNull [] args) {
        String sql = " select "+ args[0] +" from " + args[1];
        try {
            st = cn.prepareStatement(sql);
            rs = st.executeQuery();
            return rs;
        } catch (Exception e) {
            throw new RuntimeException("程序崩溃码2 请联系管理员", e);
        }
    }

    public ResultSet userQuery(String @NotNull [] args) {
        String sql = " select "+ args[0] +" from " + args[1];
        try {
            st1 = cn1.prepareStatement(sql);
            rs1 = st1.executeQuery();
            return rs1;
        } catch (Exception e) {
            throw new RuntimeException("程序崩溃码3 请联系管理员", e);
        }
    }
}
