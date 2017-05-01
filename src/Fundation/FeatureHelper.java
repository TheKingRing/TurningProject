package Fundation;

import com.huaban.analysis.jieba.JiebaSegmenter;

import java.io.*;

import java.util.*;

/**
 * Created by wzq on 2017/4/24.
 */
public class FeatureHelper {
    private Utility helper;
    private Map<String, Double> totalNum;
    private Map<String, Set<String>> vectors;
    private final String path = "F:\\javaPro\\Java-Naive-Bayes-Classifier-master\\example\\data\\Train.txt";
    private double total = 0.0;
    private JiebaSegmenter segmenter;
    private static FeatureHelper instance = new FeatureHelper();

    private FeatureHelper() {
        helper = Utility.getUtility();
        totalNum = new HashMap<>();
        segmenter = new JiebaSegmenter();
        vectors = new HashMap<>();
        initialIDFMap();
        generaterEachVect();

        printVector();
    }

    private void printVector() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("sent-vec.txt"));
            for (String key : vectors.keySet()) {
                Set<String> set = vectors.get(key);
                StringBuilder builder = new StringBuilder();
                builder.append("[");
                for (String str : set) {
                    builder.append("\t").append(str);
                }
                builder.append("\t").append("]");
                writer.write(String.format("%s\t%s\r\n", key, builder.toString()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generaterEachVect() {
        Set<String> stops = helper.getStopword();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String s;
            while ((s = reader.readLine()) != null) {
                String[] arr = s.split("\t");
                for (String sent : arr) {
                    String ar = sent.split(",")[1];
                    List<String> list = segmenter.sentenceProcess(ar);
                    Set<String> set = new HashSet<>();
                    for (String word : list) {
                        if (stops.contains(word)) continue;
                        set.add(word);
                    }
                    vectors.put(sent, set);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initialIDFMap() {
        Set<String> stops = helper.getStopword();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String s;
            while ((s = reader.readLine()) != null) {
                String[] arr = s.split("\t");
                for (String sent : arr) {
                    vectors.put(sent, new HashSet<>());
                    total++;
                    String ar = sent.split(",")[1];
                    List<String> list = segmenter.sentenceProcess(ar);
                    Set<String> set = new HashSet<>();
                    for (String word : list) {
                        if (stops.contains(word)) continue;
                        set.add(word);
                    }
                    for (String word : set) {
                        totalNum.put(word, totalNum.getOrDefault(word, 0.0) + 1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FeatureHelper getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        FeatureHelper helper = getInstance();
    }
}

class Point {
    double idf;
    String word;

    Point(double idf, String word) {
        this.idf = idf;
        this.word = word;
    }
}