package LogesticRegression;

import Jama.Matrix;
import com.huaban.analysis.jieba.JiebaSegmenter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by wzq on 2017/4/24.
 */
public class LRHelper {
    private JiebaSegmenter segmenter;
    private Set<String> stopWords;
    private static LRHelper instance = new LRHelper();
    private Map<String, Integer> feature;
    private Set<Sample> vectors;
    private double[][] tmpArr;

    private Matrix dataMatrix;
    private Matrix labelMatrix;
    private Matrix Weights;// 优化目标；权值
    int m = 0;
    int n = 0;

    private LRHelper() {
        feature = new HashMap<>();
        vectors = new HashSet<>();
        segmenter = new JiebaSegmenter();
        initialStopWords();
    }

    private double sigmode(double k) {
        return 1.0 / (1 + Math.pow(Math.E, -k));
    }

    private void initialStopWords() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("E:\\TurningPro\\src\\data\\StopWords1.txt"));
            String s;
            while ((s = reader.readLine()) != null) {
                stopWords.add(s.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static LRHelper getInstance() {
        return instance;
    }

    public void addTraining(String tag, String sent) {
        int t = "positive".equals(tag) ? 1 : 0;
        vectors.add(new Sample(t, sent));
        m++;
    }

    public void initialMatrix() {
        tmpArr = new double[m][n];
        double[] label = new double[m];
        int row = 0;
        for (Sample s : vectors) {
            double[] arr = tmpArr[row];
            label[row++] = s.tag;
            Set<String> vec = s.vec;
            for (String word : vec) {
                arr[feature.get(word)]++;
            }
        }
        dataMatrix = new Matrix(tmpArr);
        labelMatrix = new Matrix(label, 1);
        Weights = Matrix.random(n, 1); // 随机初始化权值（0.0-1.0）
    }

    /**
     * @param alpha      : 步长
     * @param maxCycles: 训练次数
     */
    public void Learning(double alpha, int maxCycles) {
        for (int i = 0; i < maxCycles; i++) {
            Matrix tmp = dataMatrix.times(Weights);
            int k = 0;
            for (double d : tmp.getColumnPackedCopy()) {
                tmp.set(k++, 0, sigmode(d));
            }
            Matrix error = tmp.minus(labelMatrix);
            Weights = Weights.minus(dataMatrix.transpose().times(error));
        }
    }

    class Sample {
        //0- positive; 1 - negative
        int tag;
        String content;
        Set<String> vec;

        Sample(int tag, String content) {
            this.tag = tag;
            this.content = content;
            vec = new HashSet<>();
            List<String> seg = segmenter.sentenceProcess(content);
            for (String word : seg) {
                if (stopWords.contains(word)) continue;
                vec.add(word);
                if (!feature.containsKey(word)) {
                    feature.put(word, n++);
                }
            }
        }
    }
}

