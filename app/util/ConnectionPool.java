package util;

/**
 * Created by zhangshl on 17/1/17.
 */

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author zhangshaolong
 *
 */
public class ConnectionPool {
//    private static BoneCP connectionPool = null;
    private static HikariDataSource connectionPool = null;

    private ConnectionPool() throws ClassNotFoundException, SQLException {
//        Class.forName("org.postgresql.Driver");
//        BoneCPConfig config = new BoneCPConfig();
////        config.setJdbcUrl("jdbc:postgresql://10.47.54.175:9999/BDP");
//      config.setJdbcUrl("jdbc:postgresql://120.27.163.25/BDP");
//        config.setUsername("postgres");
//        config.setPassword("ly@postgres&2015");
//        config.setLazyInit(true);
//
////        config.setMinConnectionsPerPartition(2);
//        config.setMaxConnectionsPerPartition(10);//每个分区最大连接数
//        config.setPartitionCount(1);//分区数
//        config.setCloseConnectionWatch(true);
//        config.setLogStatementsEnabled(false);
//        connectionPool = new BoneCP(config);

        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
//        config.addDataSourceProperty("serverName", "10.47.54.175");
        config.addDataSourceProperty("serverName", "120.27.163.25");
        config.addDataSourceProperty("portNumber", "5432");
        config.addDataSourceProperty("databaseName", "BDP");
        config.addDataSourceProperty("user", "postgres");
        config.addDataSourceProperty("password", "ly@postgres&2015");
        config.setMaximumPoolSize(5);

        connectionPool = new HikariDataSource(config);
    }

    public static synchronized Connection getConnection(){
        if(connectionPool == null){
            try {
                new ConnectionPool();
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
