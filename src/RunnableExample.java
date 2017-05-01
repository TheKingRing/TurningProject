import Fundation.Utility;
import NaiveBayes.NaiveBayesHelper;

import java.io.*;
import java.util.Set;

public class RunnableExample {

    public static void main(String[] args) throws IOException {
        NaiveBayesHelper bayesHelper = new NaiveBayesHelper();
      /*  Classifier<String, String> bayes = new BayesClassifier<String, String>();*/
        Utility helper = Utility.getUtility();
        BufferedReader reader = new BufferedReader(new FileReader("F:\\intelijProject\\TurningProject\\src\\data\\TrainData.txt"));
        String s;
        while ((s = reader.readLine()) != null) {
            s = s.trim();
            String[] Arr = s.split("\\|");
            for (String str : Arr) {
                String[] ar = str.split("#");
                if (ar.length < 2) {
                    continue;
                }
                Set<String> vector = helper.generateVector(ar[1]);
                if (vector.size() == 0) {
                    continue;
                }
                bayesHelper.learn(ar[0], vector);
            }
        }
        bayesHelper.getConditionProb();
        reader = new BufferedReader(new FileReader("F:\\intelijProject\\TurningProject\\src\\data\\TestData.txt"));
        double NN = 0.0;
        double NALL = 0.0;
        double ALLN = 0.0;
        while ((s = reader.readLine()) != null) {
            String[] Arr = s.split("\\|");
            for (String str : Arr) {
                String res;
                String[] ar = str.trim().split("#");
                if (ar.length < 2) {
                    continue;
                }
                Set<String> vector = helper.generateVector(ar[1]);
                if (vector.size() == 0) {
                    res = "positive";
                } else {
                    res = bayesHelper.classify(vector);
                }
                if (ar[0].equals("negative") && res.equals("negative")) {
                    NN++;
                }
                if (ar[0].equals("negative")) {
                    NALL++;
                }
                if (res.equals("negative")) {
                    ALLN++;
                }
            }
        }
        double f1 = ((2 * NN) / (ALLN + NALL));
        double accuracy = NN / ALLN;
        double recall = NN / NALL;
        System.out.println(String.format("%s\t%s\t%s", f1, accuracy, recall));

    }
}
