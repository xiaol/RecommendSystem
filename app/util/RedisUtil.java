package util;

/**
 * Created by zhangshl on 17/3/1.
 */

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public final class RedisUtil {

//    private static String host = "127.0.0.1";
//    private static String password = null;

    private static String host = "ccd827d637514872.m.cnhza.kvstore.aliyuncs.com";
    private static String password = "ccd827d637514872:LYcache2015";

    //redis {
//  host = "ccd827d637514872.m.cnhza.kvstore.aliyuncs.com"
//  port = 6379
//  password = "ccd827d637514872:LYcache2015"
//  database = 1
//}
    //Redis的端口号
    private static int PORT = 6379;

    private static int db = 1;

    //可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static int MaxTotal = 1024;

    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_IDLE = 200;

    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private static int MAX_WAIT = 10000;

    private static int TIMEOUT = 10000;

    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = true;

    private static JedisPool jedisPool = null;

    /**
     * 初始化Redis连接池
     */
    static {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxIdle(MAX_IDLE);
            config.setMaxTotal(MaxTotal);
            config.setTestOnBorrow(TEST_ON_BORROW);
            jedisPool = new JedisPool(config, host, PORT, TIMEOUT, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取Jedis实例
     * @return
     */
    public synchronized static Jedis getJedis() {
        try {
            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
                resource.select(db);
                return resource;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 释放jedis资源
     * @param jedis
     */
    public static void closeJedis(final Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
}
