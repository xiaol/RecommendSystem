package services;

import dao.DataBaseDao;
import play.Logger;

import java.util.Date;
import java.util.TimerTask;

/**
 * Created by zhangshl on 16/8/22.
 */
public class PvUvHourCount extends TimerTask {

    @Override
    public void run() {
        Logger.info("开始统计每小时PV、UV-->"+new Date());
        DataBaseDao.insertPvUvHour();
        Logger.info("结束统计每小时PV、UV-->"+new Date());
    }


}
