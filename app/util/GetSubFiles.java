package util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangshl on 16/8/21.
 */
public class GetSubFiles {
    //获取所有的模型文件路径
    public static List<String> modelPaths(File dir){
        System.out.println(dir.getAbsolutePath());
        File[] subdirs= dir.listFiles();
        List<String> list = new ArrayList<String>();
        for (File f:subdirs){
            if(!f.getName().startsWith("."))
                list.add(f.getName().replaceAll("d-",""));
        }
        return list;
    }
}
