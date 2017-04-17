package controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import dao.DataBaseDao;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import util.ConfigConstants;
import util.ConnectionPool;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fengjigang on 17/4/14.
 * 热点新闻上传接口
 */

public class HotNewsController extends Controller {

    public Result processHotNews() {
        String requestRType = request().getHeader(Http.HeaderNames.CONTENT_TYPE);
        if (requestRType != null && !requestRType.contains(Http.MimeTypes.FORM)){
            return ok("{'code':'4015','message':'Request Header ContentType Not Supported'}");
        }
        String[] news = request().body().asFormUrlEncoded().get("news");
        if (news == null || news.length == 0){
            return ok("{'code':'4002','message':'InvalidArgument'}");
        }
        ObjectMapper mapper = new ObjectMapper();
        //全部放list里,查询哪些已经插入数据库,得到一个交集list,过滤此list select from T where nid not in (list)
        List<Integer> nids = new ArrayList<Integer>();
        for(int i=0;i<news.length;i++){
            try {
                String hotword = news[i];
                System.out.println(hotword);
                HttpGet httpgetnid = new HttpGet(ConfigConstants.searchurl+hotword.replaceAll("\"","").replaceAll("\'",""));
                HttpClient httpclient2 = HttpClientBuilder.create().build();
                HttpResponse response2 = httpclient2.execute(httpgetnid);
                String json = EntityUtils.toString(response2.getEntity(),"utf-8");
                Map<String, Integer> maps = mapper.readValue(json, Map.class);
                nids.add(maps.get("data"));
                httpgetnid.abort();
                if(nids.size()>0){
                    Connection conn = ConnectionPool.getConnection();
                    DataBaseDao.insertHotData(DataBaseDao.getHotWordNotExistsNids(nids, conn), conn);
                    ConnectionPool.closeConnection(conn);
                }
            } catch (ClientProtocolException e) {
                //e.printStackTrace();
            }catch (IOException e) {
                //e.printStackTrace();
            }
        }
        return ok("{'code':2000,'mesage':'Upload Hot News Success'}");
    }
}
