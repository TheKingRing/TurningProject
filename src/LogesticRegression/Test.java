package LogesticRegression;

import Jama.Matrix;

/**
 * Created by wzq on 2017/4/24.
 */
public class Test {
    public static void main(String[] args) {
        Matrix matrix1 = new Matrix(10, 10, 0.1);
        Matrix matrix = new Matrix(10, 1, 1.0);
        Matrix m = Matrix.random(1, 190);
        double[] arr = m.getColumnPackedCopy();
        System.out.println(arr);
    }

    private static double sigmode(double k) {
        return 1.0 / (1 + Math.pow(Math.E, -k));
    }
}
