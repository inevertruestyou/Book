package activitytest.example.com.book;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static android.content.ContentValues.TAG;

/**
 * 此类用于MySQL数据库链接
 * Created by haha on 2017-08-26.
 */

public class DBUtil {

    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql:// 192.168.0.117:3306/test", "root", "123456");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }


    public static String selectpwd(String S_Num){
        String result = null;
        try {
            Connection con = getConnection();
            Log.i(TAG, "selectpwd: con= " + con);
            Statement st = con.createStatement();
            String sql = "select S_Pwd from student where S_Num = '"+ S_Num +"'";
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()){                                     //判断是否得到结果
                result = rs.getString(1);                       //得到结果
            }
            rs.close();
            st.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    }

