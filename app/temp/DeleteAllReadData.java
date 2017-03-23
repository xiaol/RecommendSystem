package temp;

import util.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by zhangshl on 17/2/14.
 */
public class DeleteAllReadData {
    public static void main(String[] args) {
        Connection conn = ConnectionPool.getConnection();
        try {
            Statement stmt = conn.createStatement();
            String sql = "create table newsrecommendread_ as select * from newsrecommendread where readtime>now()-interval'1 day'";
            System.out.println(sql);
            stmt.execute(sql);
            stmt.close();

            stmt = conn.createStatement();
            sql = "truncate table newsrecommendread";
            System.out.println(sql);
            stmt.execute(sql);
            stmt.close();

            stmt = conn.createStatement();
            sql = "insert into newsrecommendread select * from newsrecommendread_";
            System.out.println(sql);
            stmt.execute(sql);
            stmt.close();

            stmt = conn.createStatement();
            sql = "drop table newsrecommendread_";
            System.out.println(sql);
            stmt.execute(sql);
            stmt.close();

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
