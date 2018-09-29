package data;

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

    static int murmurBitHash(String uuid){
        HashCode hashCode=murmur3_128.hashString(uuid, Charset.forName("utf-8"));
        return Math.abs(hashCode.asInt())&(128);
    }


    /**
     * 最终比较的还是 部分计算+命中+不命中的代价(查缓存+计算）  与  直接计算的代价
     * @param args
     */
    public static void main(String[] args) {
        //将数据放进cache
        for (String uuid:uuidList) { uuid.trim();}

        ArrayList<Integer> holderX=new ArrayList<>();
        long t1=System.currentTimeMillis();
        //使用cache查询所有的
        for (String uuid:uuidList) {
            holderX.add(murmurBitHash(uuid));
        }

        ArrayList<Integer> holderY=new ArrayList<>();
        long t2=System.currentTimeMillis();
        //直接计算
        for (String uuid:uuidList) {
            holderY.add(murmurHash(uuid));
        }
        long t3=System.currentTimeMillis();

        System.out.println(holderX.size()==holderY.size());
        System.out.println("位运算取模和%100取模性能比\t"+((t3-t2)+":"+(t2-t1)));
//        3.8 4.3 3.9 3.75 3 4.333 3.9


//        long t1=System.currentTimeMillis();
//        ArrayList<Integer> holderX=new ArrayList<>();
//        for (int i = 0; i < Integer.MAX_VALUE/100; i++) {
//            holderX.add(i%100);
//        }
//        long t2=System.currentTimeMillis();
//        ArrayList<Integer> holderY=new ArrayList<>();
//        for (int i = 0; i < Integer.MAX_VALUE/100; i++) {
//            holderY.add(i&127);
//        }
//        long t3=System.currentTimeMillis();
//        System.out.println("取模%100位和运算取模性能比\t"+((t2-t1)+":"+(t3-t2)));
    }
}
