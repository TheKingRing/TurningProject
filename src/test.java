import Fundation.Utility;
import com.huaban.analysis.jieba.JiebaSegmenter;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Created by TheKingRing on 2017/5/1.
 */
public class test {
    public static void main(String[] args) throws IOException {
        String regex = "[^\u4e00-\u9fa5a-zA-Z]+";
        Pattern pattern = Pattern.compile(regex);
        String t = "我是年报表";
        String[] words = pattern.split(t);
        for (String string: words) {
            System.out.print(string + "\t");
        }
        Utility helper = Utility.getUtility();
        JiebaSegmenter segmenter = helper.getSegmenter();
        String tes = "babyq是傻逼";
        System.out.println(segmenter.sentenceProcess(tes));
    }
}
