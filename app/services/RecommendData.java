package services;

import beans.Newsrecommendforuser;
import dao.DataBaseDao;
import play.Logger;
import util.ConnectionPool;
import util.ConnectionPool3;

import java.sql.Connection;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhangshl on 16/8/22.
 */
public class RecommendData extends TimerTask {

    static AtomicInteger atomic = new AtomicInteger(0);

    private int number;//第几个任务
    private int hash = 1;//总任务数

    public RecommendData(int number, int hash){
        this.number = number;
        this.hash = hash;
    }

    @Override
    public void run(){
        ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 20, 200, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(10000));
        executor.prestartAllCoreThreads();
        Logger.info("开始推荐数据-->"+new java.util.Date());
        Connection connection = ConnectionPool3.getConnection();
        List<Long> uids = DataBaseDao.queryuids(connection);
        ConnectionPool3.closeConnection(connection);
        Map map = new HashMap<Long, List<Newsrecommendforuser>>();
        for(int i=0; i<uids.size(); i++){
            Long uid =  uids.get(i);
            if(uid%hash == number){
                final int finalI = i;
                executor.execute(new Runnable() {
                    public void run() {
                        try{
                            Logger.info(finalI+"----"+"线程池中线程数目："+executor.getPoolSize()+"，队列中等待执行的任务数目："+
                                    executor.getQueue().size()+"，已执行完的任务数目："+executor.getCompletedTaskCount());
                            Connection conn = ConnectionPool3.getConnection();
                            List<Newsrecommendforuser> listRec = DataBaseDao.queryRecommend(uid, conn);
                            map.put(uid, listRec);
                            if(listRec.size()>0){
                                atomic.addAndGet(1);
//                                System.out.println("uid:  "+uid+",  list:  "+list.size());
                            }
                            ConnectionPool3.closeConnection(conn);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }

        }

        executor.shutdown();
        while(true){
            if(executor.isTerminated()){
                System.out.println("有数据的用户: "+ atomic.get());

                Connection conn = ConnectionPool.getConnection();
                Iterator iterator = map.keySet().iterator();
                while(iterator.hasNext()){
                    Long uid = (Long) iterator.next();
                    List<Newsrecommendforuser> list = (List<Newsrecommendforuser>) map.get(uid);
                    DataBaseDao.insert(list, conn, uid);
                }

                ConnectionPool.closeConnection(conn);
                Logger.info("结束推荐数据-->"+new java.util.Date());
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new RecommendData(0, 5), 1000*1, 1000*60*50);

//        RecommendData r = new RecommendData();
//        r.run();
//        Connection conn = ConnectionPool3.getConnection();
//        List<Newsrecommendforuser> list = DataBaseDao.queryRecommend(6440748L, conn);
//        for (int i = 0; i < list.size(); i++) {
//            Newsrecommendforuser news = list.get(i);
//            System.out.println(news.getNid());
//        }
    }

}
