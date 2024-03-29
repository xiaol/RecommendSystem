package dao;

import beans.Newsrecommendforuser;
import util.ConfigConstants;
import util.ConnectionPool3;
import util.StringUtilsm;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * Created by zhangshl on 16/9/18.
 */
public class DataBaseDao {

    private static SimpleDateFormat date_sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
    private static SimpleDateFormat hour_sdf = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
    private static Connection conn1 = null;


    public static void insert(List<Newsrecommendforuser> list, Connection conn, Long uid) {
        String tablename2 = "newsrecommendforuser_" + uid % 10;
        String sql = "insert into " + tablename2 + " (uid,nid,predict,sourcetype,ctime) values(?,?,?,?,?)";
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            long time = new Date().getTime();
            Timestamp t = new Timestamp(time);
            int length = list.size();

            for (int i = 0; i < length; i++) {
                Newsrecommendforuser n = list.get(i);
                pstmt.setInt(1, n.getUid());
                pstmt.setInt(2, n.getNid());
                pstmt.setDouble(3, n.getPredict());
                pstmt.setInt(4, n.getSourcetype());
                pstmt.setTimestamp(5, t);//.setDate(4, time);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pstmt != null) {   // 关闭声明
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static List<Long> queryuids(Connection conn) {
        String sql = "select distinct uid from pvdetail where uid!=0 and ctime > (now() - interval '30 SECOND')";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Long> list = new ArrayList<>();
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {   // 关闭记录集
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pstmt != null) {   // 关闭声明
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * 根据算法查询推荐给用户的新闻
     *
     * @param uid
     * @return
     */
    public static List<Newsrecommendforuser> queryRecommend(Long uid) {
        CountDownLatch latch = new CountDownLatch(3);
        List<Newsrecommendforuser> listLDA = new ArrayList<>();
        List<Newsrecommendforuser> listKmeans = new ArrayList<>();
        List<Newsrecommendforuser> listCF = new ArrayList<>();
        new Thread() {
            @Override
            public void run() {
                listLDA.addAll(DataBaseDao.queryRecommendLDA(uid));
                latch.countDown();
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                listKmeans.addAll(DataBaseDao.queryRecommendKmeans(uid));
                latch.countDown();
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                listCF.addAll(DataBaseDao.queryRecommendCF(uid));
                latch.countDown();
            }
        }.start();
        new Thread() {
        }.start();

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        listLDA.addAll(listKmeans);
        listLDA.addAll(listCF);
        return listLDA;

    }

    public static List<Newsrecommendforuser> queryRecommendLDA(Long uid) {
        //LDA取10条,每个主题最多两条, KMeans取10条,每个cluster最多2条
        String tablename1 = "newsrecommendread_" + uid % 100;
        String tablename2 = "newsrecommendforuser_" + uid % 10;
        String sql = "select uid,nid,probability,1 from  (\n" +
                "SELECT ROW_NUMBER() OVER (partition by u.topic_id ORDER BY u.probability*t.probability*log(n.comment + 2) DESC) AS rownum,\n" +
                "u.uid,n.nid,u.probability*t.probability*log(n.comment + 2) as probability from  user_topics_v2 u inner join news_topic_v2 t \n" +
                "on u.topic_id=t.topic_id and u.model_v = t.model_v \n" +
                "inner join newslist_v2 n on  t.nid = n.nid where  u.uid= " + uid + " and n.ctime >(now() - interval '1 day')  \n" +
                "and t.ctime > (now() - interval '6 hour')\n" +
                "and not exists(select 1 from " + tablename1 + " r  where n.nid=r.nid and  uid=" + uid + " and readtime > (now() - interval '1 day'))  \n" +
                "and not exists(select 1 from " + tablename2 + " r  where n.nid=r.nid and  uid=" + uid + " and ctime> (now() - interval '1 day')) \n" +
                ") as te \n" +
                "where te.rownum <3 ORDER BY probability DESC LIMIT "+ ConfigConstants.number_system;

//        System.out.println(sql);
        return fetchData(sql);
    }

    public static List<Newsrecommendforuser> queryRecommendKmeans(Long uid) {
        //LDA取10条,每个主题最多两条, KMeans取10条,每个cluster最多2条
        String tablename1 = "newsrecommendread_" + uid % 100;
        String tablename2 = "newsrecommendforuser_" + uid % 10;
        String sql = "select uid,nid,times,2 from  (\n" +
                "SELECT ROW_NUMBER() OVER (partition by u.chid,u.cluster_id ORDER BY times*log(n.comment + 2) DESC) AS rownum,\n" +
                "u.uid,n.nid,n.title, u.times*log(n.comment + 2) as times,u.cluster_id, u.chid from  user_kmeans_cluster u inner join news_kmeans t \n" +
                "on u.cluster_id=t.cluster_id and u.chid=t.chid and u.model_v = t.model_v \n" +
                "inner join newslist_v2 n on  t.nid = n.nid where  u.uid= " + uid + " and n.ctime >(now() - interval '1 day')  \n" +
                "and t.ctime > (now() - interval '6 hour')\n" +
                "and not exists(select 1 from " + tablename1 + " r  where n.nid=r.nid and  uid=" + uid + " and readtime > (now() - interval '1 day'))  \n" +
                "and not exists(select 1 from " + tablename2 + " r  where n.nid=r.nid and  uid=" + uid + " and ctime> (now() - interval '1 day')) \n" +
                ") as te \n" +
                "where te.rownum <3 ORDER BY times DESC LIMIT "+ ConfigConstants.number_system;

//        System.out.println(sql);
        return fetchData(sql);
    }

    public static List<Newsrecommendforuser> queryRecommendCF(Long uid) {
        //LDA取10条,每个主题最多两条, KMeans取10条,每个cluster最多2条
        String tablename1 = "newsrecommendread_" + uid % 100;
        String tablename2 = "newsrecommendforuser_" + uid % 10;
        String sql = "select uid,nid,probability as probability,3 as sourcetype\n" +
                "from (SELECT ROW_NUMBER() OVER (partition by u.topic_id ORDER BY u.property * t.probability * log(n.comment + 2) DESC) AS rownum,\n" +
                "u.uid,n.nid,u.property*t.probability*log(n.comment + 2) as probability from  user_topic_cf u inner join news_topic_v2 t \n" +
                "on u.topic_id=t.topic_id and u.model_v = t.model_v \n" +
                "inner join newslist_v2 n on t.nid = n.nid where u.uid = " + uid + " \n" +
                "and n.ctime > (now() - interval '1 day')\n" +
                "and t.ctime > (now() - interval '6 hour')\n" +
                "and not exists(select 1 from " + tablename1 + " r  where n.nid=r.nid and  uid=" + uid + " and readtime > (now() - interval '1 day')) \n" +
                "and not exists(select 1 from " + tablename2 + " r  where n.nid=r.nid and  uid=" + uid + " and ctime> (now() - interval '1 day')) \n" +
                ") as te\n" +
                "where te.rownum <3 ORDER BY probability DESC LIMIT "+ ConfigConstants.number_system;

//        System.out.println(sql);
        return fetchData(sql);
    }

    /**
     * 根据sql 从数据中获取数据
     *
     * @param sql
     * @return
     */
    private static List<Newsrecommendforuser> fetchData(String sql) {
        Connection conn = ConnectionPool3.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Newsrecommendforuser> list = new ArrayList<>();
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Newsrecommendforuser nu = new Newsrecommendforuser();
                nu.setUid(rs.getInt(1));
                nu.setNid(rs.getInt(2));
                nu.setPredict(rs.getDouble(3));
                nu.setSourcetype(rs.getInt(4));
//                System.out.println(nu.toString());
                list.add(nu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pstmt != null) {   // 关闭声明
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
        return list;
    }

    public static List<Integer> getHotWordNotExistsNids(List<Integer> nids, Connection conn) {
        String nidstr = StringUtilsm.listToString(nids);
        String sql = "select nid from newsrecommendhot where nid in(" + nidstr + ")";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Set set = new HashSet();
        List<Integer> list = new ArrayList<Integer>();
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                set.add(rs.getInt(1));
            }
            for (int nid : nids) {
                if (!set.contains(nid)) {
                    list.add(nid);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {   // 关闭记录集
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pstmt != null) {   // 关闭声明
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public static void insertHotData(List<Integer> list, Connection conn) {
        String sql = "insert into newsrecommendhot (nid,ctime,status) values(?,?,?)";
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            long time = new Date().getTime();
            Timestamp t = new Timestamp(time);
            int length = list.size();

            for (int i = 0; i < length; i++) {
                pstmt.setInt(1, list.get(i));
                pstmt.setTimestamp(2, t);
                pstmt.setInt(3, 1);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pstmt != null) {   // 关闭声明
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void insertPvUvDate(Connection conn) {
        String sql = "INSERT INTO pvuvcount(pv, iospv, androidpv, iosuv, androiduv, data_time_count, time_type) VALUES ( " +
                "(SELECT count(1) as pv FROM pvdetail where ctime > ? and ctime < ? ), " +
                "(SELECT count(1) as iospv FROM pvdetail p inner join userlist_v2 u on p.uid = u.uid where p.uid!=0 and platf =1 and p.ctime > ? and p.ctime < ? ), " +
                "(SELECT count(1) as androidpv FROM pvdetail p inner join userlist_v2 u on p.uid = u.uid where p.uid!=0 and platf =2 and p.ctime > ? and p.ctime < ? ), " +
                "(SELECT count(distinct p.uid) as iosuv FROM pvdetail p inner join userlist_v2 u on p.uid = u.uid where p.uid!=0 and platf =1 and p.ctime > ? and p.ctime < ? ), " +
                "(SELECT count(distinct p.uid) as androiduv FROM pvdetail p inner join userlist_v2 u on p.uid = u.uid where p.uid!=0 and platf =2 and p.ctime > ? and p.ctime < ? ),?, ?)";
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
            pstmt.setTimestamp(11, time2);
            pstmt.setInt(12, 2);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pstmt != null) {   // 关闭声明
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void insertPvUvHour() {
        String sql = "INSERT INTO pvuvcount(pv, iosuv, androiduv, data_time_count) VALUES ( (SELECT count(1) as pv FROM pvdetail where ctime > ? and ctime < ? ), " +
                "(SELECT count(distinct p.uid) as iosuv FROM pvdetail p inner join userlist_v2 u on p.uid = u.uid where p.uid!=0 and platf =1 and p.ctime > ? and p.ctime < ? ), " +
                "(SELECT count(distinct p.uid) as androiduv FROM pvdetail p inner join userlist_v2 u on p.uid = u.uid where p.uid!=0 and platf =2 and p.ctime > ? and p.ctime < ? ),?)";
        PreparedStatement pstmt = null;
        try {
            pstmt = conn1.prepareStatement(sql);

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
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pstmt != null) {   // 关闭声明
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        Connection conn = ConnectionPool3.getConnection();
//        insertPvUvDate(conn);
        queryRecommend(6440748L);
    }
}
