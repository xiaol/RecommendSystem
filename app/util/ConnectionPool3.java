package util;

/**
 * Created by zhangshl on 17/1/17.
 */

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author zhangshaolong
 *
 */
public class ConnectionPool3 {
//    private static BoneCP connectionPool = null;
    private static HikariDataSource connectionPool = null;

    private ConnectionPool3() throws ClassNotFoundException, SQLException {
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
        config.addDataSourceProperty("serverName", "10.47.54.32");
//        config.addDataSourceProperty("serverName", "120.27.162.201");
        config.addDataSourceProperty("portNumber", "5432");
        config.addDataSourceProperty("databaseName", "BDP");
        config.addDataSourceProperty("user", "postgres");
        config.addDataSourceProperty("password", "ly@postgres&2015");
        config.setMaximumPoolSize(60);

        connectionPool = new HikariDataSource(config);
    }

    public static synchronized Connection getConnection(){
        if(connectionPool == null){
            try {
                new ConnectionPool3();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            return connectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static synchronized void closeConnection(Connection connection){
        try {
            if(!connection.isClosed()){
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
