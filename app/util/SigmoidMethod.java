package util;

/**
 * Created by zhangshl on 16/11/25.
 */
public class SigmoidMethod {
    public static double sigmoid(double src) {
        return (double) (1.0 / (1 + Math.exp(-src)));
    }

    public static void main(String[] args) {
        System.out.print(sigmoid(0));
    }

}
