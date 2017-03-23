package services;

import beans.Newsrecommendclick;
import beans.Newsrecommendread;
import beans.persona.AppInfo;
import beans.persona.UserProfile;
import dao.PersonaDao;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.mllib.classification.LogisticRegressionModel;
import org.apache.spark.mllib.classification.LogisticRegressionWithSGD;
import org.apache.spark.mllib.linalg.SparseVector;
import org.apache.spark.mllib.regression.LabeledPoint;
import play.Logger;
import util.ConfigConstants;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhangshl on 16/12/9.
 */
//利用java代码将数据取出,并拼装好,然后用spark训练模型
//看是否可以利用spark的reduce的功能,每个用户取一部分数据

public class UserProfileService extends TimerTask implements Serializable {

    @Override
    public void run() {
//        public static void main(String[] args) {
        Logger.info("训练用户模型开始-->"+new java.util.Date());
        //用户信息
        List<UserProfile> userProfiles = PersonaDao.getUserProfile();
        //特征
        List<String> list = new ArrayList<>();
        //uid对应用户信息,后面组合用
        Map<Integer, UserProfile> userProfileMap = new HashMap<>();

        for (UserProfile userProfile : userProfiles){
            userProfileMap.put(userProfile.getUid(), userProfile);
        }

        //用户app信息
        List<AppInfo> appInfos = PersonaDao.getUserAppInfo();
        int uid = 0;
        //uid对应用户app信息,后面组合用
        Map<Integer, ArrayList<String>> appInfosMap = new HashMap<>();
        //单个用户app信息,放入上面的map
        ArrayList<String>  appNames = new ArrayList<> ();

        for (AppInfo appInfo : appInfos){
            if(appInfo.getUid() != uid && uid != 0){
                appInfosMap.put(uid, appNames);
                appNames = new ArrayList<> ();
            }
            uid = appInfo.getUid();
            appNames.add(appInfo.getApp_name());
        }
        if(appNames.size()>0){
            appInfosMap.put(uid, appNames);
        }

        //标签和特征的组合
        List<String> data = new ArrayList<>();

        //点击事件
        List<Newsrecommendclick> clicks = PersonaDao.getUserClick();
        for(Newsrecommendclick click : clicks){
            int cuid = click.getUid();
            StringBuffer sbf = new StringBuffer();
            sbf.append("1;");
            sbf.append(click.getChid());
            sbf.append(",");
            UserProfile up = userProfileMap.get(cuid);
            if(up!=null){
                sbf.append(userProfileMap.get(cuid).getBrand());
                sbf.append(",");
                sbf.append(userProfileMap.get(cuid).getModel());
                sbf.append(",");
            }
            ArrayList<String> arr = appInfosMap.get(cuid);
            if(arr!=null){
                for(String appname: arr){
                    sbf.append(appname);
                    sbf.append(",");
                }
            }

            if(sbf.toString().endsWith(",")){
                data.add(sbf.toString().substring(0, sbf.toString().length()-1));
            }else{
                data.add(sbf.toString());
            }
        }

        //未点击事件
        List<Newsrecommendread> reads = PersonaDao.getUserRead();
        for(Newsrecommendread read : reads){
            int cuid = read.getUid();
            StringBuffer sbf = new StringBuffer();
            sbf.append("0;");
            sbf.append(read.getChid());
            sbf.append(",");
            UserProfile up = userProfileMap.get(cuid);
            if(up!=null){
                sbf.append(userProfileMap.get(cuid).getBrand());
                sbf.append(",");
                sbf.append(userProfileMap.get(cuid).getModel());
                sbf.append(",");
            }
            ArrayList<String> arr = appInfosMap.get(cuid);
            if(arr!=null){
                for(String appname: arr){
                    sbf.append(appname);
                    sbf.append(",");
                }
            }
            if(sbf.toString().endsWith(",")){
                data.add(sbf.toString().substring(0, sbf.toString().length()-1));
            }else{
                data.add(sbf.toString());
            }
        }

//        //组装模型数据
//        try{
//            JavaRDD<String> datardd = ModelInject.getInstance().getSparkContext().parallelize(data);
//            //所有特征
//            Map<String, Long> features = datardd.flatMap(new FlatMapFunction<String, String>() {
//                private static final long serialVersionUID = 1L;
//                @Override
//                public Iterator<String> call(String s) {
//                    return Arrays.asList(s.split(";")[1].split(",")).iterator();
//                }
//            }).distinct().zipWithIndex().collectAsMap();
//            //组装训练数据
//            JavaRDD<LabeledPoint> traindata = datardd.map(line -> {
//                String[] parts = line.split(";");
//                String[] fs = parts[1].split(",");
//                int[] indices = new int[fs.length];
//                double[] values = new double[fs.length];
//
//                for(int i=0;i<fs.length;i++){
//                    indices[i] = Integer.parseInt(features.get(fs[i]).toString());
//                    values[i] = 1;
//                }
//                SparseVector sparseVector = new SparseVector(features.size(), indices, values);
//                return new LabeledPoint(Double.parseDouble(parts[0]), sparseVector);
//            }).cache();
//
//            int numIterations = 10; //迭代次数
//            LogisticRegressionModel model = LogisticRegressionWithSGD.train(traindata.rdd(), numIterations);
//            SimpleDateFormat sdf = new SimpleDateFormat(ConfigConstants.dateFormat);
//            model.save(ModelInject.getInstance().getSparkContext().sc(), ConfigConstants.personaPath+sdf.format(new Date()));
//            Logger.info("训练用户模型结束-->"+new java.util.Date());
//        }catch (Exception e){
//            Logger.info("训练用户模型出现错误-->"+new java.util.Date());
//            e.printStackTrace();
//        }

    }
}
