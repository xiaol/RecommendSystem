package dao;

import util.ConnectionPool;
import util.StringUtilsm;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Created by zhangshl on 16/9/18.
 */
public class ClickCountDao {

    private static SimpleDateFormat date_sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

    public static void insert(Connection conn, Map<Integer, Integer> mapclick, Map<Integer, Integer> mapshow, int ptype, int ctype) {
        String sql = "INSERT INTO newsclickorder(nid, clickcount, showcount, data_time_count, ptype, ctype, ctime) VALUES (?, ?, ?, ?, ?, ?, now())";
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);

            Date date = new Date();
            String today_time = date_sdf.format(date);
            Timestamp time = new Timestamp(date_sdf.parse(today_time).getTime());

            Iterator iterator = mapclick.keySet().iterator();
            while(iterator.hasNext()){
                int nid = (int) iterator.next();
                int clickcount = mapclick.get(nid);
                int showcount = 0;
                if(mapshow.get(nid) != null)
                    showcount = mapshow.get(nid);

                pstmt.setInt(1, nid);
                pstmt.setInt(2, clickcount);
                pstmt.setInt(3, showcount);
                pstmt.setTimestamp(4, time);
                pstmt.setInt(5, ptype);
                pstmt.setInt(6, ctype);

                pstmt.addBatch();
            }
            pstmt.executeBatch();
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

    //分渠道,分平台统计新闻点击次数前100条
    public static Map<Integer, Integer> queryClickCount(Connection conn, int ptype, int ctype) {
        StringBuffer sql = new StringBuffer();
        sql.append("select nid, count(*) AS clickcount FROM newsrecommendclick p inner join user_device u on p.uid = u.uid where p.ctime > ? and p.ctime < ? ");
        if(ptype > 0){
            sql.append(" and ptype = ");
            sql.append(ptype);
        }
        if(ctype > 0){
            sql.append(" and ctype = ");
            sql.append(ctype);
        }
        sql.append(" group by p.nid order by clickcount desc limit 100");

        //System.out.println(sql);

        PreparedStatement pstmt = null;
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
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
                map.put(rs.getInt(1), rs.getInt(2));
            }
            return map;
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
            return map;
        }
    }

    //前100条点击数新闻的阅读数
    public static Map<Integer, Integer> queryShowCount(Connection conn, int ptype, int ctype, List<Integer> list) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT nid, count(*) AS showcount FROM newsrecommendread p inner join user_device u on p.uid = u.uid where p.readtime > ? and p.readtime < ? ");
        if(ptype > 0){
            sql.append(" and ptype = ");
            sql.append(ptype);
        }
        if(ctype > 0){
            sql.append(" and ctype = ");
            sql.append(ctype);
        }
        if(list.size()>0){
            sql.append(" and p.nid in ( ");
            sql.append(StringUtilsm.listToString(list));
            sql.append(" )");
        }
        sql.append(" group by p.nid ");

        //System.out.println(sql);

        PreparedStatement pstmt = null;
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
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
                map.put(rs.getInt(1), rs.getInt(2));
            }
            return map;
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
            return map;
        }
    }

    public static void main(String[] args) {
        Connection conn = ConnectionPool.getConnection();
//        queryRecommend("6440748", conn);
    }
}
