package algo.hash;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description TODO
 * @Date 2018/9/29 上午12:11
 * -
 * @Author dugenkui
 **/

public class HashMapCacheSpeedTest {
    static List<String> uuidList = new ArrayList<>();
    static {
        try {
            File file = new File("/Users/moriushitorasakigake/Desktop/uuid1.txt");
            BufferedReader fileReader = new BufferedReader(new FileReader(file));
            String tmp;
            while ((tmp = fileReader.readLine()) != null) {
                uuidList.add(tmp);
            }
        } catch (Exception e) {
            System.out.println("请在指定目录添加样本数据");
        }
    }

    static final long EXP_COUNT = uuidList.size();
    static final long MODEL = 100;
    static final double AVG_COUNT = (double)EXP_COUNT / MODEL;

    static double murmurHash(){
        Map<Integer,Integer> pointCount=new HashMap<>();
        for (String uuid:uuidList) {
            HashFunction murmur3_128= Hashing.murmur3_128();
            HashCode hashCode=murmur3_128.hashString(uuid, Charset.forName("utf-8"));
            int hash=Math.abs(hashCode.asInt())%100+1;
            pointCount.compute(hash, (k, v) -> v == null ? 1 : ++v);
        }
        double tmpPowSum = pointCount.values().stream().collect(Collectors.summingDouble(x -> Math.pow((x - AVG_COUNT), 2)));
        return Math.sqrt(tmpPowSum / MODEL);
    }

    public static void main(String[] args) {
        System.out.println(murmurHash() + "\tmurmurHash");
    }
}
