package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by zhangshl on 16/8/22.
 */
public class JdbcUtil {
    public static Connection getConn() {
        String driver = "org.postgresql.Driver";
//        String url = "jdbc:postgresql://10.47.54.175/BDP";
        String url = "jdbc:postgresql://120.27.163.25/BDP";
        String username = "postgres";
        String password = "ly@postgres&2015";
        Connection conn = null;
        try {
            Class.forName(driver); //classLoader,加载对应驱动
            conn = (Connection) DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
