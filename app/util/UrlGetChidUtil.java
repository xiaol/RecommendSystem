package util;

import beans.ChidRespone;
import beans.NidChid;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangshl on 16/9/26.
 */
public class UrlGetChidUtil {

    public static Map<Integer, Integer> getChids(List<Integer> nids, Map<Integer, Integer> map){
        BufferedReader in = null;
        try {
            URL yahoo = new URL(ConfigConstants.chidurl+"?"+StringUtilsm.listToStringWithUrl(nids));
            in = new BufferedReader(
                    new InputStreamReader(
                            yahoo.openStream()));
            String inputLine;
            ObjectMapper mapper = new ObjectMapper();
            ChidRespone chidRespone = null;
            while ((inputLine = in.readLine()) != null){
                chidRespone  = mapper.readValue(inputLine, ChidRespone.class);
            }
            List<NidChid> list = chidRespone.getResult();


            for(NidChid nc:list){
                map.put(nc.getNid(), nc.getChid());
            }
            in.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return map;
    }

    public static void main(String[] args) {
        List<Integer> nidsErrorChid = new ArrayList<Integer>();
        nidsErrorChid.add(4965366);
        nidsErrorChid.add(4965367);
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        System.out.println(UrlGetChidUtil.getChids(nidsErrorChid, map));
    }


}
