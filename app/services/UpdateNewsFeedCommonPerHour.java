package services;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import play.Logger;
import redis.clients.jedis.Jedis;
import util.ConfigConstants;
import util.IoUtil;
import util.RedisUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.TimerTask;

/**
 * Created by zhangshl on 16/8/22.
 */
public class UpdateNewsFeedCommonPerHour extends TimerTask {

    @Override
    public void run() {
        // 创建HttpClient实例
        HttpClient httpclient = new DefaultHttpClient();
        // 创建Get方法实例
        HttpGet httpgets = new HttpGet(ConfigConstants.updateNewsFeedCommonurl);
        try {
            HttpResponse response = httpclient.execute(httpgets);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instreams = entity.getContent();
                String str = IoUtil.convertStreamToString(instreams);
                Logger.info("更新公共池新闻: "+str);
                httpgets.abort();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Jedis jedis = RedisUtil.getJedis();
        System.out.println("公共新闻池大小:    "+jedis.smembers("webapi:news:feed:common").size());
        jedis.close();
//        UpdateNewsFeedCommonPerHour r = new UpdateNewsFeedCommonPerHour();
//        r.run();
    }


}
