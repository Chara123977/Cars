import java.util.*;
import java.io.*;
import util.*;
import sql.*;

public class CarRentSystem {
  public static void main(String[] args) {
    SQL mysql = new SQL();
    while (true) {
      System.out.println("欢迎使用汽车租赁管理系统V1.0");
      System.out.print("请选择要进行的操作: \n1. 登录 \n2. 注册 \n3. 退出系统 \n");
      Scanner sc = new Scanner(System.in);
      int choice = sc.nextInt();
      switch (choice) {
        case 1:
          System.out.print("请输入用户名: ");
          String username = sc.next();
          System.out.print("请输入密码: ");
          String password = sc.next();
          if (mysql.Login(username, password)) {
            System.out.println("登录成功！");
            Menu.username = username;
            Menu.GetMenu(mysql);
          } else {
            System.out.println("用户名或密码错误！");
          }
          continue;
        case 2:
          System.out.print("请输入用户名: ");
          String newUsername = sc.next();
          System.out.print("请输入密码: ");
          String newPassword = sc.next();
          if (mysql.Register(newUsername, newPassword)) {
            System.out.println("注册成功！正在进入系统");
            Menu.username = newUsername;
            Menu.GetMenu(mysql);
          } else {
            throw new RuntimeException("注册失败！");
          }
          continue;
        case 3:
          System.out.println("欢迎下次使用！");
          mysql.Close();
          System.exit(0);
        default:
          System.out.println("输入错误，请重新输入！");
      }
    }
  }
}
