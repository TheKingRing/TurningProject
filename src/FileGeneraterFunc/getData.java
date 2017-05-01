package FileGeneraterFunc;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/4/23.
 */
public class getData {
    private static final int SEX = 1;
    private static final int ABUSE = 2;
    private static final int LAW = 3;
    private static final int VIOLENT = 4;
    public static void main(String[] args) throws IOException, BiffException {
        Workbook workbook = Workbook.getWorkbook(new File("F:\\intelijProject\\TurningProject\\src\\data\\res1.xls"));
        Sheet sheet = workbook.getSheet(0);
        Map<Integer, Set<String>> persons = new HashMap<>();
        initialMap(persons);
        Cell[] per = sheet.getColumn(1);
        Map<Integer, Set<String>> robots = new HashMap<>();
        initialMap(robots);
        Cell[] rob = sheet.getColumn(2);
        Cell[] words = sheet.getColumn(3);
        for (int i = 2; i < rob.length; i ++) {
            int tag = Integer.parseInt(rob[i].getContents());
            if (tag == 0) continue;
            String res = words[i].getContents();
            String[] arr = null;
            if (res.contains("|")) {
                arr = res.split("\\|");
            }
            if (arr == null) {
                robots.get(tag).add(res);
            }else {
                for (String s : arr) {
                    robots.get(tag).add(s);
                }
            }
            tag = Integer.parseInt(per[i].getContents());
            if (tag == 0) continue;
            if (arr == null) {
                persons.get(tag).add(res);
            }else {
                for (String s : arr) {
                    persons.get(tag).add(s);
                }
            }
        }
        BufferedWriter bw1 = new BufferedWriter(new FileWriter("S:\\TurningPro\\src\\data\\persons.txt"));
        BufferedWriter bw2 = new BufferedWriter(new FileWriter("S:\\TurningPro\\src\\data\\robots.txt"));
        write(bw1, persons);
        write(bw2,robots);
        bw1.close();
        bw2.close();

    }

    private static void write(BufferedWriter bw, Map<Integer, Set<String>> map) throws IOException {
        for (Integer k : map.keySet()) {
            switch (k) {
                case SEX:
                    bw.write("SEX: \r\n");
                    break;
                case ABUSE:
                    bw.write("ABUSE: \r\n");
                    break;
                case LAW:
                    bw.write("LAW: \r\n");
                    break;
                case VIOLENT:
                    bw.write("VIOLENT: \r\n");
                    break;
            }
            Set<String> set = map.get(k);
            int count = 0;
            for (String word : set) {
                if (count == 5) {
                    bw.write(word + "\r\n");
                    count = 0;
                    continue;
                }
                bw.write(word + "\t");
                count ++;
            }
        }
    }

    private static void initialMap(Map<Integer, Set<String>> map) {
        map.put(SEX, new HashSet<>());
        map.put(ABUSE, new HashSet<>());
        map.put(LAW, new HashSet<>());
        map.put(VIOLENT, new HashSet<>());
    }
}
