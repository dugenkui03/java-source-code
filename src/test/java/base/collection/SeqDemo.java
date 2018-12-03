package base.collection;

import java.util.ArrayList;
import java.util.List;

/**
 * Collection使用下标取值时，必须有相应个数的元素，否则报出“超边界”异常
 *
 * @author 杜艮魁
 * @date 2018/11/2
 */
public class SeqDemo {
    public static void main(String[] args) {
        List<String> list=new ArrayList();
        String str=list.get(0);
        list.add("du");

        System.out.println(str);
    }
}
