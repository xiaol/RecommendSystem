package temp;

import util.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by zhangshl on 17/2/14.
 */
public class CreateTableForUser {
    public static void main(String[] args) {
        Connection conn = ConnectionPool.getConnection();
        try {
            for (int i=0; i<10; i++){
                Statement stmt = conn.createStatement();
                String sql = "create table newsrecommendforuser_"+i+" (uid integer NOT NULL, nid integer NOT NULL, predict double precision, ctime timestamp without time zone, sourcetype integer DEFAULT 1 )";
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
