package controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import dao.DataBaseDao;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import play.libs.Json;
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
        if (requestRType == null || requestRType != null && !requestRType.contains(Http.MimeTypes.FORM)){
            return ok(statusCodeJson(4015,"Request Header ContentType Not Supported"));
        }
        String[] news = request().body().asFormUrlEncoded().get("news");
        if (!verifyParams(news)){
            return ok(statusCodeJson(4002,"InvalidArgument"));
        }
        ObjectMapper mapper = new ObjectMapper();
        //全部放list里,查询哪些已经插入数据库,得到一个交集list,过滤此list select from T where nid not in (list)
        List<Integer> nids = new ArrayList<Integer>();
        for(int i=0;i<news.length;i++){
            try {
                String hotword = news[i];
                System.out.println(hotword);
                HttpGet httpgetnid = new HttpGet(ConfigConstants.searchurl+java.net.URLEncoder.encode(hotword,"utf-8"));
                HttpClient httpclient2 = HttpClientBuilder.create().build();
                HttpResponse response2 = httpclient2.execute(httpgetnid);
                String json = EntityUtils.toString(response2.getEntity(),"utf-8");
                Map<String, Integer> maps = mapper.readValue(json, Map.class);
                if ("2000".equals(maps.get("code")))
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
        return ok(statusCodeJson(2000,"Upload Hot News Success"));
    }

    public static String statusCodeJson(int code,String message){
        return Json.newObject().put("code",code).put("message",message).toString();
    }

    public boolean verifyParams(String[] news){
        if (news == null || news.length == 0) return false;
        for (String n:news){
            if (n == null || "".equals(n.trim())){
                return false;
            }
        }
        return true;
    }
}
