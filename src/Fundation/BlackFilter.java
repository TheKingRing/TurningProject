package Fundation;

import com.huaban.analysis.jieba.JiebaSegmenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/4/23.
 */
public class BlackFilter {
    private JiebaSegmenter segmenter;
    private Utility helper;
    private Pattern pattern;

    public BlackFilter() {
        helper = Utility.getUtility();
        segmenter = helper.getSegmenter();
        pattern = helper.getPattern();
    }

    public String classify(String sentence) {
        Set<String> stopwords = helper.getStopword();
        List<String> words = segmenter.sentenceProcess(sentence);
        List<String> l = new ArrayList<>();
        for (String word : words) {
            if (!stopwords.contains(word.toLowerCase())) {
                l.add(word);
            }
        }
        if (l.size() == 0) {
            return "positive";
        }
        if (check(l)) {
            return "negative";
        }
        return "positive";
    }

    private boolean check(List<String> list) {
        StringBuilder sb = new StringBuilder();
        Set<String> sensewords = helper.getSensewords();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i).toLowerCase());
            if (sensewords.contains(sb.toString())) return true;
            for (int j = i + 1; j < list.size(); j++) {
                String t = list.get(j);
                sb.append(t);
                if (sensewords.contains(sb.toString())) return true;
                int len = sb.length();
                sb.delete(len - t.length(), len);
            }
            sb = new StringBuilder();
        }
        return false;
    }


}
