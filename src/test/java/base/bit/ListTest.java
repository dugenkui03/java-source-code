package base.bit;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author 杜艮魁
 * @date 2018/10/15
 */
public class ListTest {
    public static void main(String[] args) {
        Map<String, List<Integer>> res = new HashMap<>();
        for (int i = 0; i < 2; i++) {
            List<Integer> out = new LinkedList<>();
            for (int j = 0; j < 3; j++) {
                out.add(j*i);
            }
            res.put(i+"",out);
        }

        for (Object obj:res) {
            System.out.println(obj);
        }
    }
}
