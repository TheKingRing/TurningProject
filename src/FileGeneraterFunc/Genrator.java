package FileGeneraterFunc;

import Fundation.Utility;
import com.huaban.analysis.jieba.JiebaSegmenter;

import java.io.*;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/4/23.
 */
public class Genrator {
    public static void main(String[] args) throws IOException {
        Utility helper = Utility.getUtility();
        Set<String> sense = helper.getSensewords();
        JiebaSegmenter segmenter = helper.getSegmenter();
        InputStreamReader isr = new InputStreamReader(new FileInputStream("F:\\intelijProject\\TurningProject\\src\\data\\resourse.txt"), "GBK");
        BufferedReader reader = new BufferedReader(isr);
        BufferedWriter writer = new BufferedWriter(new FileWriter("F:\\intelijProject\\TurningProject\\src\\data\\TrainData.txt"));
        BufferedWriter writer1 = new BufferedWriter(new FileWriter("F:\\intelijProject\\TurningProject\\src\\data\\TestData.txt"));
        String s = reader.readLine();
        int N = 0;
        int P = 0;
        double NN = 0; // 人工 -1 机器 -1
        double NALL = 0; // 人工 -1
        double ALLN = 0;// 机器 -1
        int count = 0;
        while (s != null){
            String[] arr = s.split("\\t");
            String sent = arr[0].toLowerCase();
            int tag = Integer.parseInt(arr[1]) == 0? 0 : -1;
            List<String> words = segmenter.sentenceProcess(sent);
            int res = 0;
            for (String word : words){
                if (sense.contains(word)){
                    res = -1;
                    break;
                }
            }

            if (res == tag) {
                count ++;
                if (res == -1) {
                    N ++;
                    if (count == 100){
                        if (N > 1000) {
                            writer.write(String.format("%s#%s\n\r", "negative", sent));
                        }else {
                            writer1.write(String.format("%s#%s\n\r", "negative", sent));

                        }

                        count = 0;
                        continue;
                    }
                    if (N > 1000) {
                        writer.write(String.format("%s#%s|", "negative", sent));
                    }else {
                        writer1.write(String.format("%s#%s|", "negative", sent));
                    }
                }else {
                    P ++;
                    if (count == 100) {
                        if (P > 1000){
                            writer.write(String.format("%s#%s\n\r", "positive", sent));
                        }else {
                            writer1.write(String.format("%s#%s\n\r", "positive", sent));
                        }
                        count = 0;
                        continue;
                    }
                    if (P > 1000){
                        writer.write(String.format("%s#%s|", "positive", sent));
                    }else {
                        writer1.write(String.format("%s#%s|", "positive", sent));
                    }
                }
            }
           if (res == -1) {
                ALLN ++;
           }
           if (tag == -1) {
                NALL ++;
           }
           if (res == -1 && tag == -1){
                NN ++;
           }
            s = reader.readLine();
        }
        reader.close();
        writer.close();
        writer1.close();
        System.out.println("positive:" + P);
        System.out.println("negative:" + N);
        System.out.println(String.format("%s\t%s", "recall:", NN / NALL));
        System.out.println(String.format("%S\t%s", "accuracy:", NN / ALLN));
    }
}
