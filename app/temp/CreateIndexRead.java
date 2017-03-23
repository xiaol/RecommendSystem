package temp;

import util.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by zhangshl on 17/2/14.
 */
public class CreateIndexRead {
    public static void main(String[] args) {
        Connection conn = ConnectionPool.getConnection();
        try {
            for (int i=0; i<100; i++){
                Statement stmt = conn.createStatement();
                String sql = "CREATE INDEX newsrecommendread_"+i+"_readtime_index ON newsrecommendread_"+i+" USING btree (readtime DESC NULLS LAST);";
                System.out.println(sql);
                stmt.execute(sql);
                stmt.close();
            }

            for (int i=0; i<100; i++){
                Statement stmt = conn.createStatement();
                String sql = "CREATE INDEX newsrecommendread_"+i+"_nid_index ON newsrecommendread_"+i+" USING btree (nid)";
                System.out.println(sql);
                stmt.execute(sql);
                stmt.close();
            }

            for (int i=0; i<100; i++){
                Statement stmt = conn.createStatement();
                String sql = "CREATE INDEX newsrecommendread_"+i+"_uid_index ON newsrecommendread_"+i+" USING btree (uid)";
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
