package concurrent.algo.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 * @Description TODO
 * @Date 2018/9/29 上午12:37
 * -
 * @Author dugenkui
 **/

public class AnalyzeUtil {

    static Map<String,Integer> uuidMap = new HashMap<>();
    static {
        try {
            File file = new File("/Users/moriushitorasakigake/Desktop/uuid-dugenkui_waimai_algorithm_2018-09-29-00-34-41_sql1.txt");
            BufferedReader fileReader = new BufferedReader(new FileReader(file));
            String uuid;
            while ((uuid = fileReader.readLine()) != null) {
                uuidMap.compute(uuid,(k,v)->v==null?1:++v);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("请在指定目录添加样本数据");
        }
    }


    public static void main(String[] args) {
        uuidMap.entrySet().stream().sorted((x,y)->(x.getValue()-y.getValue())).forEach(x->{
            System.out.println(x.getKey()+":"+x.getValue());
        });
    }
}
