package services;

import dao.ClickCountDao;
import dao.DataCountDao;
import play.Logger;
import util.ConfigConstants;
import util.ConnectionPool;
import util.ConnectionPool3;

import java.sql.Connection;
import java.util.*;

/**
 * Created by zhangshl on 16/8/22.
 */
public class NewsClickCount extends TimerTask {

    @Override
    public void run() {
        Logger.info("开始统计每天新闻点击量排行-->"+new Date());
        Connection conn = ConnectionPool.getConnection();

        int[] array = {ConfigConstants.qidianzixun, ConfigConstants.huanglitianqi, ConfigConstants.wenzisuopin, ConfigConstants.lieyingliulanqi, ConfigConstants.baipai};

        //渠道类型, 1：奇点资讯， 2：黄历天气，3：纹字锁频，4：猎鹰浏览器，5：白牌
        //平台类型，1：IOS，2：安卓
        Connection conn3 = ConnectionPool3.getConnection();
        //----------------总量(不分平台,不分渠道)-----------------
        try{
            int ctype=0;
            int ptype=0;
            Map<Integer, Integer> mapclick = null;
            Map<Integer, Integer> mapshow = null;
            try{mapclick = ClickCountDao.queryClickCount(conn3, ptype, ctype);}catch (Exception e){Logger.info("统计每天渠道 ctype="+ctype+", ptype="+ptype+" 点击数出错, "+new Date()); Logger.error(e.getMessage());}
            List<Integer> list = new ArrayList<>();
            Iterator iterator = mapclick.keySet().iterator();
            while(iterator.hasNext()){
                list.add((Integer) iterator.next());
            }
            try{mapshow = ClickCountDao.queryShowCount(conn3, ptype, ctype, list);}catch (Exception e){Logger.info("统计每天渠道 ctype="+ctype+", ptype="+ptype+" 展示数出错, "+new Date()); Logger.error(e.getMessage());}

            ClickCountDao.insert(conn, mapclick, mapshow, ptype, ctype);
            Logger.info("平台: "+ptype+", 渠道: "+ctype+" 统计新闻点击量排行完成-->"+new Date());
        }catch (Exception e){}


        //----------------安卓总量(不分渠道)-----------------
        try{
            int ctype = 0;
            int ptype = ConfigConstants.android;
            Map<Integer, Integer> mapclick = null;
            Map<Integer, Integer> mapshow = null;
            try{mapclick = ClickCountDao.queryClickCount(conn3, ptype, ctype);}catch (Exception e){Logger.info("统计每天渠道 ctype="+ctype+", ptype="+ptype+" 点击数出错, "+new Date()); Logger.error(e.getMessage());}
            List<Integer> list = new ArrayList<>();
            Iterator iterator = mapclick.keySet().iterator();
            while(iterator.hasNext()){
                list.add((Integer) iterator.next());
            }
            try{mapshow = ClickCountDao.queryShowCount(conn3, ptype, ctype, list);}catch (Exception e){Logger.info("统计每天渠道 ctype="+ctype+", ptype="+ptype+" 展示数出错, "+new Date()); Logger.error(e.getMessage());}

            ClickCountDao.insert(conn, mapclick, mapshow, ptype, ctype);
            Logger.info("平台: "+ptype+", 渠道: "+ctype+" 统计新闻点击量排行完成-->"+new Date());
        }catch (Exception e){}


        //----------------IOS总量(不分渠道)-----------------
        try{
            int ctype = 0;
            int ptype = ConfigConstants.ios;
            Map<Integer, Integer> mapclick = null;
            Map<Integer, Integer> mapshow = null;
            try{mapclick = ClickCountDao.queryClickCount(conn3, ptype, ctype);}catch (Exception e){Logger.info("统计每天渠道 ctype="+ctype+", ptype="+ptype+"  点击数出错, "+new Date()); Logger.error(e.getMessage());}
            List<Integer> list = new ArrayList<>();
            Iterator iterator = mapclick.keySet().iterator();
            while(iterator.hasNext()){
                list.add((Integer) iterator.next());
            }
            try{mapshow = ClickCountDao.queryShowCount(conn3, ptype, ctype, list);}catch (Exception e){Logger.info("统计每天渠道 ctype="+ctype+", ptype="+ptype+" 展示数出错, "+new Date()); Logger.error(e.getMessage());}

            ClickCountDao.insert(conn, mapclick, mapshow, ptype, ctype);
            Logger.info("平台: "+ptype+", 渠道: "+ctype+" 统计新闻点击量排行完成-->"+new Date());
        }catch (Exception e){}


        //----------------安卓分渠道-----------------
        for (int i=0; i< array.length; i++){
            int ctype = array[i];
            int ptype = ConfigConstants.android;
            Map<Integer, Integer> mapclick = null;
            Map<Integer, Integer> mapshow = null;
            try{mapclick = ClickCountDao.queryClickCount(conn3, ptype, ctype);}catch (Exception e){Logger.info("统计每天渠道 ctype="+ctype+", ptype="+ptype+" 点击数出错, "+new Date()); Logger.error(e.getMessage());}
            List<Integer> list = new ArrayList<>();
            Iterator iterator = mapclick.keySet().iterator();
            while(iterator.hasNext()){
                list.add((Integer) iterator.next());
            }
            try{mapshow = ClickCountDao.queryShowCount(conn3, ptype, ctype, list);}catch (Exception e){Logger.info("统计每天渠道 ctype="+ctype+", ptype="+ptype+" 展示数出错, "+new Date()); Logger.error(e.getMessage());}

            ClickCountDao.insert(conn, mapclick, mapshow, ptype, ctype);
            Logger.info("平台: "+ptype+", 渠道: "+ctype+" 统计新闻点击量排行完成-->"+new Date());
        }


        //----------------IOS分渠道-----------------
        for (int i=0; i< array.length; i++){
            int ctype = array[i];
            int ptype = ConfigConstants.ios;
            Map<Integer, Integer> mapclick = null;
            Map<Integer, Integer> mapshow = null;
            try{mapclick = ClickCountDao.queryClickCount(conn3, ptype, ctype);}catch (Exception e){Logger.info("统计每天渠道 ctype="+ctype+", ptype="+ptype+"  点击数出错, "+new Date()); Logger.error(e.getMessage());}
            List<Integer> list = new ArrayList<>();
            Iterator iterator = mapclick.keySet().iterator();
            while(iterator.hasNext()){
                list.add((Integer) iterator.next());
            }
            try{mapshow = ClickCountDao.queryShowCount(conn3, ptype, ctype, list);}catch (Exception e){Logger.info("统计每天渠道 ctype="+ctype+", ptype="+ptype+" 展示数出错, "+new Date()); Logger.error(e.getMessage());}

            ClickCountDao.insert(conn, mapclick, mapshow, ptype, ctype);
            Logger.info("平台: "+ptype+", 渠道: "+ctype+" 统计新闻点击量排行完成-->"+new Date());
        }

        ConnectionPool.closeConnection(conn);
        ConnectionPool3.closeConnection(conn3);
        Logger.info("结束统计每天新闻点击量排行-->"+new Date());
    }

    public static void main(String[] args) {
        NewsClickCount r = new NewsClickCount();
        r.run();
    }


}
