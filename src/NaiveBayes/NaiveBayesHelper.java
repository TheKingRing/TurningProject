package NaiveBayes;

import Fundation.Utility;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/4/23.
 */
public class NaiveBayesHelper {
    private double alpha = 0.004;
    private double minP = 1.0;
    private double minN = 1.0;
    private double maxP = 0;
    private double maxN = 0;
    private Map<String, Map<String, Double>> unionNum;// count (word, tag);
    private Map<String, Map<String, Double>> conditionProb; // P(word | tag)
    private Set<String> wordsall;
    private Map<String, Double> tagNum;
    private Map<String, Double> tagProb;
    private Utility helper;

    public NaiveBayesHelper() {
        unionNum = new HashMap<>();
        conditionProb = new HashMap<>();
        tagNum = new HashMap<>();
        tagProb = new HashMap<>();
        wordsall = new HashSet<>();
        helper = Utility.getUtility();
        unionNum.put("negative", new HashMap<>());
        unionNum.put("positive", new HashMap<>());
        conditionProb.put("negative", new HashMap<>());
        conditionProb.put("positive", new HashMap<>());
    }

    public void learn(String tag, Set<String> words) {
        tagNum.put(tag, tagNum.getOrDefault(tag, 0.0) + 1);
        for (String word : words) {
            double num = unionNum.get(tag).getOrDefault(word, 0.0) + 1;
            unionNum.get(tag).put(word, num);
            wordsall.add(word);
        }
    }

    public void getConditionProb() throws IOException {
       /* BufferedWriter writer1 = new BufferedWriter(new FileWriter("negMore.txt"));
        BufferedWriter writer2 = new BufferedWriter(new FileWriter("posMore.txt"));*/
        double total = 0.0;
        for (Double b : tagNum.values()) {
            total += b;
        }
        for (String key : tagNum.keySet()) {
            tagProb.put(key, tagNum.get(key) / total);
        }
        for (String tag : unionNum.keySet()) {
            Map<String, Double> map = unionNum.get(tag);
            for (String word : map.keySet()) {
                conditionProb.get(tag).put(word, unionNum.get(tag).get(word) / tagNum.get(tag));
            }
        }

        //get the min P N and max P N
        for (String word : wordsall) {
            double pos = conditionProb.get("positive").getOrDefault(word, 0.0);
            double neg = conditionProb.get("negative").getOrDefault(word, 0.0);
            if (neg != 0.0) {
                minN = Math.min(minN, neg);
                maxN = Math.max(maxN, neg);
            }
            if (pos != 0.0) {
                minP = Math.min(minP, pos);
                maxP = Math.max(maxP, pos);
            }
        }
    }

    /**
     * 学习函数
     */
    public String classify(Set<String> words) {
        return innerFunc(words, alpha);

    }

    /**
     * 该分类为学习alpha使用的分类
     */
    public String classify(Set<String> words, double alpha) {
        return innerFunc(words, alpha);
    }

    /**
     * @param alpha:迭代参数，使得F1值可以最大化
     * @param words:需要分类的语句的词向量
     */
    private String innerFunc(Set<String> words, double alpha) {
        double neg = 0.0;
        double pos = 0.0;
        Set<String> black = helper.getSensewords();
        for (String word : words) {
            // 对于在训练集里没有出现过的词语，我们采取黑名单过滤，若不在黑名单中，那么有极大的可能是正向的思想。所以赋值概率为maxP,
            if (!wordsall.contains(word)) {
                if (black.contains(word)) {
                    return "negative";
                } else {
                    neg += Math.log(minN * tagProb.get("negative"));
                    pos += Math.log(maxP * alpha * tagProb.get("positive"));
                }
                continue;
            }
            double negProb = conditionProb.get("negative").getOrDefault(word, minN / 2);
            double posProb = conditionProb.get("positive").getOrDefault(word, minP);

            neg += Math.log(negProb * tagProb.get("negative"));
            pos += Math.log(posProb * tagProb.get("positive"));
        }
        return pos > neg ? "positive" : "negative";
    }

}
