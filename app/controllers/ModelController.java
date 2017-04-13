package controllers;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class ModelController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index(String uid, String data) {
//        Map<String, double[]> map = ModelInject.getInstance().getModelMap();
        String nids = "";
        //test git  
//        if(map.containsKey(uid)){
//            LogisticRegressionModel model = map.get("1");
//            double[] d = new double[]{1.0, 1.0};
//            Vector v = Vectors.dense(d);
//            System.out.println("预测结果"+model.predict(v));
//        }

        return ok(nids);
    }
}
