package controllers;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
//        Map<String, double[]> map = ModelInject.getInstance().getModelMap();
//        System.out.println("模型总数->"+map.keySet().size());
//        Iterator iterator = map.keySet().iterator();
//        while(iterator.hasNext()){
//            String key = iterator.next().toString();
//            System.out.println(key);
//            double[] b = map.get(key);
//            for(double c:b){
//                System.out.print(c+", ");
//            }
//            System.out.println("========================");
//        }
        return ok("Your new application is ready.");
    }

}
