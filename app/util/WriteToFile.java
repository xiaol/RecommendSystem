package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by zhangshl on 16/12/12.
 */
public class WriteToFile {

    public static void write(List<String> list, String path){
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(path);
            bw = new BufferedWriter(fw);

            for (String data : list) {
                bw.write(data);
                bw.newLine();//换行
            }

        } catch (IOException e) {
            System.out.println("写入文件出错");
        } finally {
            if (bw != null) {
                try {
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            if (fw != null)
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
