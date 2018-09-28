package concurrent.algo.data;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @Description TODO
 * @Date 2018/9/29 上午12:37
 * -
 * @Author dugenkui
 **/

public class AnalyzeUtil {

    static HashFunction murmur3_128= Hashing.murmur3_128();
    static Map<String,Integer> cache = new HashMap<>();
    static ArrayList<String> uuidList=new ArrayList<>();
    static {
        try {
            File file = new File("/Users/moriushitorasakigake/Desktop/uuid-dugenkui_waimai_algorithm_2018-09-29-00-34-41_sql1.txt");
            BufferedReader fileReader = new BufferedReader(new FileReader(file));
            String uuid;
            while ((uuid = fileReader.readLine()) != null) {
                uuidList.add(uuid);
                cache.put(uuid,murmurHash(uuid));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("请在指定目录添加样本数据");
        }
    }

    static final long EXP_COUNT = uuidList.size();
    static final long MODEL = 100;
    static final double AVG_COUNT = (double)EXP_COUNT / MODEL;

    static int murmurHash(String uuid){
            HashCode hashCode=murmur3_128.hashString(uuid, Charset.forName("utf-8"));
            return Math.abs(hashCode.asInt())%100+1;
    }


    public static void main(String[] args) {
        //将数据放进cache
        for (String uuid:uuidList) { uuid.trim();}

        ArrayList<Integer> holderX=new ArrayList<>();
        long t1=System.currentTimeMillis();
        //使用cache查询所有的
        for (String uuid:uuidList) {
            holderX.add(cache.get(uuid));
        }
        long t2=System.currentTimeMillis();

//        System.out.println("没有局部性时，查询缓存速度\t"+(t2-t1));
        //13 11 12 15 15  14 13 12 16 15

        System.out.println("有局部性时，查询缓存速度\t"+(t2-t1));
        //9 9 12 10 9  9 11 12 9 12
    }
}
