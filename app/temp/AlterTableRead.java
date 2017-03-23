package temp;

import util.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by zhangshl on 17/2/14.
 */
public class AlterTableRead {
    public static void main(String[] args) {
        Connection conn = ConnectionPool.getConnection();
        try {
            for (int i=0; i<100; i++){
                Statement stmt = conn.createStatement();
                String sql = "ALTER TABLE newsrecommendread_"+i+"  ADD logchid integer";
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
