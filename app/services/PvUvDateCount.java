package services;

import dao.DataCountDao;
import play.Logger;
import util.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.TimerTask;

/**
 * Created by zhangshl on 16/8/22.
 */
public class PvUvDateCount extends TimerTask {

    @Override
    public void run() {
        Logger.info("开始统计每天PV、UV-->"+new Date());
        Connection conn = ConnectionPool.getConnection();
        try{DataCountDao.insertPvUvDate(conn);}catch (Exception e){Logger.info("统计每天PV、UV出错, "+new Date()); Logger.error(e.getMessage());}


        int[] array = {ConfigConstants.qidianzixun, ConfigConstants.huanglitianqi, ConfigConstants.wenzisuopin, ConfigConstants.lieyingliulanqi, ConfigConstants.baipai};

        //渠道类型, 1：奇点资讯， 2：黄历天气，3：纹字锁频，4：猎鹰浏览器，5：白牌
        //平台类型，1：IOS，2：安卓
        Connection conn3 = ConnectionPool3.getConnection();
        for (int i=0; i< array.length; i++){
            int ctype = array[i];
            int pv = 0;
            int uv = 0;
            int ad_pv = 0;
            int ios_pv =0;
            int android_pv =0;
            int ios_uv =0;
            int android_uv =0;

            try{pv = DataCountDao.queryPV(conn3, 0, ctype, null);}catch (Exception e){Logger.info("统计每天渠道PV、UV--> ctype="+ctype+",  pv出错, "+new Date()); Logger.error(e.getMessage());}
            try{uv = DataCountDao.queryUV(conn3, 0, ctype);}catch (Exception e){Logger.info("统计每天渠道PV、UV--> ctype="+ctype+",  uv出错, "+new Date()); Logger.error(e.getMessage());}
            try{ad_pv = DataCountDao.queryPV(conn3, 0, ctype, "ad");}catch (Exception e){Logger.info("统计每天渠道PV、UV--> ctype="+ctype+",  带广告pv出错, "+new Date()); Logger.error(e.getMessage());}
            try{ios_pv = DataCountDao.queryPV(conn3, ConfigConstants.ios, ctype, null);}catch (Exception e){Logger.info("统计每天渠道PV、UV--> ctype="+ctype+",  ios pv出错, "+new Date()); Logger.error(e.getMessage());}
            try{android_pv = DataCountDao.queryPV(conn3, ConfigConstants.android, ctype, null);}catch (Exception e){Logger.info("统计每天渠道PV、UV--> ctype="+ctype+",  Android pv出错, "+new Date()); Logger.error(e.getMessage());}
            try{ios_uv = DataCountDao.queryUV(conn3, ConfigConstants.ios, ctype);}catch (Exception e){Logger.info("统计每天渠道PV、UV--> ctype="+ctype+",  ios uv出错, "+new Date()); Logger.error(e.getMessage());}
            try{android_uv = DataCountDao.queryUV(conn3, ConfigConstants.android, ctype);}catch (Exception e){Logger.info("统计每天渠道PV、UV--> ctype="+ctype+",  Android uv出错, "+new Date()); Logger.error(e.getMessage());}

            System.out.println();
            DataCountDao.insertPvUvByCtype(conn, ctype, pv, uv, ad_pv, ios_pv, ios_uv, android_pv, android_uv);

            Logger.info("渠道: "+ctype+" 统计PV、UV完成-->"+new Date());
        }

        ConnectionPool.closeConnection(conn);
        ConnectionPool3.closeConnection(conn3);
        Logger.info("结束统计每天PV、UV-->"+new Date());
    }

    public static void main(String[] args) {
        PvUvDateCount r = new PvUvDateCount();
        r.run();
    }


}
