package temp;

import util.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by zhangshl on 17/2/14.
 */
public class DeleteReadData {
    public static void main(String[] args) {
        Connection conn = ConnectionPool.getConnection();
        try {
            for (int i=77; i<100; i++){
                Statement stmt = conn.createStatement();
                String sql = "create table newsrecommendread_"+i+"_1 as select * from newsrecommendread_"+i+" where readtime>now()-interval'7 day'";
                System.out.println(sql);
                stmt.execute(sql);
                stmt.close();

                stmt = conn.createStatement();
                sql = "truncate table newsrecommendread_"+i;
                System.out.println(sql);
                stmt.execute(sql);
                stmt.close();

                stmt = conn.createStatement();
                sql = "insert into newsrecommendread_"+i+" select * from newsrecommendread_"+i+"_1";
                System.out.println(sql);
                stmt.execute(sql);
                stmt.close();

                stmt = conn.createStatement();
                sql = "drop table newsrecommendread_"+i+"_1";
                System.out.println(sql);
                stmt.execute(sql);
                stmt.close();

                System.out.println("---------------------------------------");
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
