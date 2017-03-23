package services;

import beans.HotNews;
import beans.HotRespone;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.DataBaseDao;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import util.ConfigConstants;
import util.ConnectionPool;
import util.IoUtil;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by zhangshl on 16/8/19.
 */
public class GetHotNews extends TimerTask {

    @Override
    public void run() {
        // 创建HttpClient实例
        HttpClient httpclient = new DefaultHttpClient();
        // 创建Get方法实例
        HttpGet httpgets = new HttpGet(ConfigConstants.hoturl);
        try {
            HttpResponse response = httpclient.execute(httpgets);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instreams = entity.getContent();
                String str = IoUtil.convertStreamToString(instreams);
                ObjectMapper mapper = new ObjectMapper();
                HotRespone h = mapper.readValue(str, HotRespone.class);
                List<HotNews> list = h.getResult();
                //全部放list里,查询哪些已经插入数据库,得到一个交集list,过滤此list select from T where nid not in (list)
                List<Integer> nids = new ArrayList<Integer>();

                for(int i=0;i<list.size();i++){
                    try {
                        String hotword = h.getResult().get(i).getBaiduHotWord().get(0);
                        System.out.println(hotword);
                        HttpGet httpgetnid = new HttpGet(ConfigConstants.searchurl+hotword.replaceAll("\"","").replaceAll("\'",""));
                        HttpClient httpclient2 = new DefaultHttpClient();
                        HttpResponse response2 = httpclient2.execute(httpgetnid);
                        HttpEntity entity2 = response2.getEntity();
                        String json = IoUtil.convertStreamToString(entity2.getContent());
    //                    System.out.println(json);
                        Map<String, Integer> maps = mapper.readValue(json, Map.class);
                        nids.add(maps.get("data"));
                        httpgetnid.abort();
                    } catch (ClientProtocolException e) {
                        //e.printStackTrace();
                    }catch (IOException e) {
                        //e.printStackTrace();
                    }
                }
                httpgets.abort();

                if(nids.size()>0){
                    Connection conn = ConnectionPool.getConnection();
                    DataBaseDao.insertHotData(DataBaseDao.getHotWordNotExistsNids(nids, conn), conn);
                    ConnectionPool.closeConnection(conn);
                }

            }
        } catch (ClientProtocolException e) {
            //e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }



    public static void main(String[] args) {
        Timer timer5 = new Timer();
        timer5.schedule(new GetHotNews(), 1000*1, 1000*60*5);
    }
}
