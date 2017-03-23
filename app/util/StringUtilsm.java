package util;

import java.util.List;

/**
 * Created by zhangshl on 16/9/18.
 */
public class StringUtilsm {
    public static String convert(String utfString){
        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;

        while((i=utfString.indexOf("\\u", pos)) != -1){
            sb.append(utfString.substring(pos, i));
            if(i+5 < utfString.length()){
                pos = i+6;
                sb.append((char)Integer.parseInt(utfString.substring(i+2, i+6), 16));
            }
        }

        return sb.toString();
    }

    public static String listToString(List<Integer> nids){
        StringBuffer nidstr = new StringBuffer();
        for (int nid : nids){
            nidstr.append(nid);
            nidstr.append(",");
        }
        return nidstr.toString().substring(0, nidstr.toString().length()-1);
    }

    public static String listToStringWithUrl(List<Integer> nids){
        StringBuffer nidstr = new StringBuffer();
        for (int nid : nids){
            nidstr.append("nid=");
            nidstr.append(nid);
            nidstr.append("&");
        }
        return nidstr.toString().substring(0, nidstr.toString().length()-1);
    }
}
