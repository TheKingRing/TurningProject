package Fundation;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.WordDictionary;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/4/27.
 */
public class Utility {
    private int i = 0;
    private static Utility instance = new Utility();
    private Set<String> Stopword;
    private Set<String> sensewords;
    private JiebaSegmenter segmenter;
    private WordDictionary wdicAdd;
    private Pattern pattern;

    private Utility() {
        i += 1;
        String regex = "[^\u4e00-\u9fa5a-zA-Z]+"; // 特殊符号模式串
        pattern = Pattern.compile(regex);
        Stopword = new HashSet<>();
        sensewords = new HashSet<>();
        wdicAdd = WordDictionary.getInstance();
        Path path = Paths.get("F:\\intelijProject\\TurningProject\\src\\data\\user.txt");
        wdicAdd.loadUserDict(path);
        segmenter = new JiebaSegmenter();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("F:\\intelijProject\\TurningProject\\src\\data\\StopWords1.txt"));
            String s;
            while ((s = reader.readLine()) != null) {
                Stopword.add(s.trim().toLowerCase());
            }
            Workbook workbook = Workbook.getWorkbook(new File("F:\\intelijProject\\TurningProject\\src\\data\\keyWords.xls"));
            Sheet[] sheets = workbook.getSheets();
            for (Sheet sh : sheets) {
                Cell[] cells = sh.getColumn(0);
                for (int i = 1; i < cells.length; i++) {
                    String word = cells[i].getContents().toLowerCase().trim();
                    if (word.length() == 0) continue;
                    sensewords.add(word);
                }
            }
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }
    }

    public static Utility getUtility() {
        return instance;
    }

    public Set<String> getSensewords() {
        return sensewords;
    }

    public Set<String> getStopword() {
        return Stopword;
    }

    public JiebaSegmenter getSegmenter() {
        return segmenter;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public Set<String> generateVector(String sentence) {
        sentence = sentence.replaceAll("\\s*", "");
        Set<String> li = new HashSet<>();
        for (String seg : pattern.split(sentence)) {
            List<String> words = segmenter.sentenceProcess(seg);
            for (String s : words) {
                s = s.toLowerCase();
                if (!Stopword.contains(s)) {
                    li.add(s);
                }
            }
        }
        return li;
    }
}
