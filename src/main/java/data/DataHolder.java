package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description TODO
 * @Date 2018/9/29 下午2:48
 * -
 * @Author dugenkui
 **/

public class DataHolder {
    public static List<String> uuidList = new ArrayList<>();
    static {
        try {
            File file = new File("/Users/moriushitorasakigake/Desktop/uuid-dugenkui_waimai_algorithm_2018-09-29-00-34-41_sql1.txt");
            BufferedReader fileReader = new BufferedReader(new FileReader(file));
            String tmp;
            while ((tmp = fileReader.readLine()) != null) {
                uuidList.add(tmp);
            }
        } catch (Exception e) {
            System.out.println("请在指定目录添加样本数据");
        }
    }
}
