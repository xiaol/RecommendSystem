package temp;

import util.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by zhangshl on 17/2/14.
 */
public class DeleteForUserData {
    public static void main(String[] args) {
        Connection conn = ConnectionPool.getConnection();
        try {
            for (int i=0; i<10; i++){
                Statement stmt = conn.createStatement();
                String sql = "create table newsrecommendforuser_"+i+"_1 as select * from newsrecommendforuser_"+i+" where ctime>now()-interval'3 day'";
                System.out.println(sql);
                stmt.execute(sql);
                stmt.close();

                stmt = conn.createStatement();
                sql = "truncate table newsrecommendforuser_"+i;
                System.out.println(sql);
                stmt.execute(sql);
                stmt.close();

                stmt = conn.createStatement();
                sql = "insert into newsrecommendforuser_"+i+" select * from newsrecommendforuser_"+i+"_1";
                System.out.println(sql);
                stmt.execute(sql);
                stmt.close();

                stmt = conn.createStatement();
                sql = "drop table newsrecommendforuser_"+i+"_1";
                System.out.println(sql);
                stmt.execute(sql);
                stmt.close();
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
