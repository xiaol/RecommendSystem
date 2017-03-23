package dao;

import util.ConnectionPool;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhangshl on 16/9/18.
 */
public class DataCountDao {

    private static SimpleDateFormat date_sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
    private static SimpleDateFormat hour_sdf = new SimpleDateFormat("yyyy-MM-dd HH:00:00");


    public static void insertPvUvDate(Connection conn) {
        String sql = "INSERT INTO pvuvcount(pv,adpv, iospv, androidpv, iosuv, androiduv, data_time_count, time_type, ctype) VALUES ( " +
                "(SELECT count(1) as pv FROM pvdetail where ctime > ? and ctime < ? ), " +
                "(SELECT count(1) as adpv FROM pvdetail where ctime > ? and ctime < ? and method in ('NewsResponseController.refreshFeedWithAd', 'NewsResponseController.loadFeedWithAd')), " +
                "(SELECT count(1) as iospv FROM pvdetail p inner join userlist_v2 u on p.uid = u.uid where p.uid!=0 and platf =1 and p.ctime > ? and p.ctime < ? ), " +
                "(SELECT count(1) as androidpv FROM pvdetail p inner join userlist_v2 u on p.uid = u.uid where p.uid!=0 and platf =2 and p.ctime > ? and p.ctime < ? ), " +
                "(SELECT count(distinct p.uid) as iosuv FROM pvdetail p inner join userlist_v2 u on p.uid = u.uid where p.uid!=0 and platf =1 and p.ctime > ? and p.ctime < ? ), " +
                "(SELECT count(distinct p.uid) as androiduv FROM pvdetail p inner join userlist_v2 u on p.uid = u.uid where p.uid!=0 and platf =2 and p.ctime > ? and p.ctime < ? ),?, ?, ?)";
        System.out.println(sql);
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);

            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            String last_day_time = date_sdf.format(calendar.getTime());
            String today_time = date_sdf.format(date);

            Timestamp time1 = new Timestamp(date_sdf.parse(last_day_time).getTime());
            Timestamp time2 = new Timestamp(date_sdf.parse(today_time).getTime());

            pstmt.setTimestamp(1, time1);
            pstmt.setTimestamp(2, time2);
            pstmt.setTimestamp(3, time1);
            pstmt.setTimestamp(4, time2);
            pstmt.setTimestamp(5, time1);
            pstmt.setTimestamp(6, time2);
            pstmt.setTimestamp(7, time1);
            pstmt.setTimestamp(8, time2);
            pstmt.setTimestamp(9, time1);
            pstmt.setTimestamp(10, time2);
            pstmt.setTimestamp(11, time1);
            pstmt.setTimestamp(12, time2);
            pstmt.setTimestamp(13, time2);
            pstmt.setInt(14, 2);
            pstmt.setInt(15, 0);

            pstmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(pstmt != null){   // 关闭声明
                try{
                    pstmt.close() ;
                }catch(SQLException e){
                    e.printStackTrace() ;
                }
            }
        }
    }

    public static void insertPvUvByCtype(Connection conn, int ctype, int pv, int uv, int adpv, int iospv, int iosuv, int androidpv, int androiduv) {
        String sql = "INSERT INTO pvuvcount(pv, uv, adpv, iospv, androidpv, iosuv, androiduv, data_time_count, time_type, ctype) VALUES (?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);

            Date date = new Date();
            String today_time = date_sdf.format(date);
            Timestamp time = new Timestamp(date_sdf.parse(today_time).getTime());

            pstmt.setInt(1, pv);
            pstmt.setInt(2, uv);
            pstmt.setInt(3, adpv);
            pstmt.setInt(4, iospv);
            pstmt.setInt(5, androidpv);
            pstmt.setInt(6, iosuv);
            pstmt.setInt(7, androiduv);
            pstmt.setTimestamp(8, time);
            pstmt.setInt(9, 2);
            pstmt.setInt(10, ctype);

            pstmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(pstmt != null){   // 关闭声明
                try{
                    pstmt.close() ;
                }catch(SQLException e){
                    e.printStackTrace() ;
                }
            }
        }
    }

    //分渠道,分平台统计PV
    public static int queryPV(Connection conn, int ptype, int ctype, String method) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT count(1) AS PV FROM pvdetail p inner join user_device u on p.uid = u.uid where p.ctime > ? and p.ctime < ? ");
        if(ptype > 0){
            sql.append(" and ptype = ");
            sql.append(ptype);
        }
        if(ctype > 0){
            sql.append(" and ctype = ");
            sql.append(ctype);
        }

        if(method != null){
            sql.append(" and method in ('NewsResponseController.refreshFeedWithAd', 'NewsResponseController.loadFeedWithAd') ");
        }

        System.out.println("pv======="+sql.toString());
        PreparedStatement pstmt = null;
        int pv = 0 ;
        try {
            pstmt = conn.prepareStatement(sql.toString());

            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            String last_day_time = date_sdf.format(calendar.getTime());
            String today_time = date_sdf.format(date);

            Timestamp time1 = new Timestamp(date_sdf.parse(last_day_time).getTime());
            Timestamp time2 = new Timestamp(date_sdf.parse(today_time).getTime());

            pstmt.setTimestamp(1, time1);
            pstmt.setTimestamp(2, time2);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                pv = rs.getInt(1);
            }
            return pv;
        }catch (SQLException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(pstmt != null){   // 关闭声明
                try{
                    pstmt.close() ;
                }catch(SQLException e){
                    e.printStackTrace() ;
                }
            }
            return pv;
        }
    }

    //分渠道,分平台统计UV
    public static int queryUV(Connection conn, int ptype, int ctype) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT count(distinct p.uid) AS UV FROM pvdetail p inner join user_device u on p.uid = u.uid where p.ctime > ? and p.ctime < ? ");
        if(ptype > 0){
            sql.append(" and ptype = ");
            sql.append(ptype);
        }
        if(ctype > 0){
            sql.append(" and ctype = ");
            sql.append(ctype);
        }
        System.out.println("uv======="+sql.toString());
        PreparedStatement pstmt = null;
        int pv = 0 ;
        try {
            pstmt = conn.prepareStatement(sql.toString());

            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            String last_day_time = date_sdf.format(calendar.getTime());
            String today_time = date_sdf.format(date);

            Timestamp time1 = new Timestamp(date_sdf.parse(last_day_time).getTime());
            Timestamp time2 = new Timestamp(date_sdf.parse(today_time).getTime());

            pstmt.setTimestamp(1, time1);
            pstmt.setTimestamp(2, time2);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                pv = rs.getInt(1);
            }
            return pv;
        }catch (SQLException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(pstmt != null){   // 关闭声明
                try{
                    pstmt.close() ;
                }catch(SQLException e){
                    e.printStackTrace() ;
                }
            }
            return pv;
        }
    }

    public static void insertPvUvHour(Connection conn) {
        String sql = "INSERT INTO pvuvcount(pv, iosuv, androiduv, data_time_count) VALUES ( (SELECT count(1) as pv FROM pvdetail where ctime > ? and ctime < ? ), " +
                "(SELECT count(distinct p.uid) as iosuv FROM pvdetail p inner join userlist_v2 u on p.uid = u.uid where p.uid!=0 and platf =1 and p.ctime > ? and p.ctime < ? ), " +
                "(SELECT count(distinct p.uid) as androiduv FROM pvdetail p inner join userlist_v2 u on p.uid = u.uid where p.uid!=0 and platf =2 and p.ctime > ? and p.ctime < ? ),?)";
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);

            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);
            String last_day_time = hour_sdf.format(calendar.getTime());
            String today_time = hour_sdf.format(date);

            Timestamp time1 = new Timestamp(hour_sdf.parse(last_day_time).getTime());
            Timestamp time2 = new Timestamp(hour_sdf.parse(today_time).getTime());

            pstmt.setTimestamp(1, time1);
            pstmt.setTimestamp(2, time2);
            pstmt.setTimestamp(3, time1);
            pstmt.setTimestamp(4, time2);
            pstmt.setTimestamp(5, time1);
            pstmt.setTimestamp(6, time2);
            pstmt.setTimestamp(7, time2);

            pstmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(pstmt != null){   // 关闭声明
                try{
                    pstmt.close() ;
                }catch(SQLException e){
                    e.printStackTrace() ;
                }
            }
        }
    }

    public static void main(String[] args) {
        Connection conn = ConnectionPool.getConnection();
        insertPvUvDate(conn);
//        queryRecommend("6440748", conn);
    }
}
