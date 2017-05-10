package util;

/**
 * Created by zhangshl on 16/8/18.
 */
public class ConfigConstants {
//    //模型根目录
    public static final String modelRootPath = "/data/spark/data/modelpath/";
    public static final String tempdataRootPath = "/data/spark/data/tempdata/";
    public static final String dataRootPath = "/data/spark/data/traindata/";
    public static final String tmp = "/data/spark/data/tmp/recommendsystem/";
    public static final String personaPath = "/data/spark/data/persona/";

//    public static final String modelRootPath = "/Users/wangzhen/Downloads/zsl_download/projects/sparkdata/modelpath/";
//    public static final String tempdataRootPath = "/Users/wangzhen/Downloads/zsl_download/projects/sparkdata/tempdatapath/";
//    public static final String dataRootPath = "/Users/wangzhen/Downloads/zsl_download/projects/sparkdata/datapath/";
//    public static final String tmp = "/Users/wangzhen/Downloads/zsl_download/projects/sparkdata/tmp/recommendsystem/";
//    public static final String personaPath = "/Users/wangzhen/Downloads/zsl_download/projects/sparkdata/persona/";

    public static final String dateFormat = "yyyyMMdd";
    public static final String dataFileName = "part-00000";
    //初始化模型加载近7天的模型文件
    public static final int initdays = -7;
    //更新模型加载近2天的模型文件
    public static final int updatedays = -2;
    public static final String appName = "LogisticRegressionModel";
    //spark线程数
    public static final String threads = "local[4]";

    public static final String hoturl = "http://120.27.162.110:9001/hot_points";
    public static final String chidurl = "http://120.55.88.11/ml/newsClassifyOnNids";
    public static final String searchurl = "http://bdp.deeporiginalx.com/v2/ns/es/sh?keywords=";
    public static final String updateNewsFeedCommonurl = "http://bdp.deeporiginalx.com/v2/ns/fed/up";

    //从人工推荐条数
    public static final int number_person = 20;
    //从系统推荐条数
    public static final int number_system = 8;

    //渠道类型, 1：奇点资讯， 2：黄历天气，3：纹字锁频，4：猎鹰浏览器，5：白牌
    public static final int qidianzixun = 1;
    public static final int huanglitianqi = 2;
    public static final int wenzisuopin = 3;
    public static final int lieyingliulanqi = 4;
    public static final int baipai = 5;

    //平台类型，1：IOS，2：安卓
    public static final int ios = 1;
    public static final int android = 2;


}
