package algo.hash;

/**
 * @Description TODO
 * @Date 2018/9/28 下午2:11
 * -
 * @Author dugenkui
 **/

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.springframework.util.DigestUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description 标准差衡量数据总体波动范围(方差描述离散程度)
 * fixme:一千万次调用，一百个桶，每个桶期望值十万；
 * @Date 2018/9/8 下午4:48
 * -
 * @Author dugenkui
 **/

public class SourceHashBalanceTest {

    /**
     * 获取uuid数据集合
     */
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

    /**
     * 实验次数，分桶和每个桶期望值
     */
    static final long EXP_COUNT = uuidList.size();
    static final long MODEL = 100;
    static final double AVG_COUNT = (double)EXP_COUNT / MODEL;


    //基准测试的标准差：证明样本数据每个值完全不同而且变化时标准差为0.0
    static double baseTest() {
        Map<Integer, Integer> pointCount = new HashMap();
        for (int i = 0; i < EXP_COUNT; i++) {
            int pos = i % 100;
            pointCount.compute(pos, (k, v) -> v == null ? 1 : ++v);
        }

        //标准差计算公式
        double tmpPowSum = pointCount.values().stream().collect(Collectors.summingDouble(x -> Math.pow((x - AVG_COUNT), 2)));
        return Math.sqrt(tmpPowSum / MODEL);
    }


    /**
     * 随机方法的误差：理论上请求量越大误差越小
     */
    static double randomRoutain() {
        Random rand = new Random(System.currentTimeMillis());
        Map<Integer, Integer> pointCount = new HashMap();
        for (int i = 0; i < EXP_COUNT; i++) {
            int hash = Math.abs(rand.nextInt() % 100);
            pointCount.compute(hash, (k, v) -> v == null ? 1 : ++v);
        }
        //标准差计算公式
        double tmpPowSum = pointCount.values().stream().collect(Collectors.summingDouble(x -> Math.pow((x - AVG_COUNT), 2)));
        return Math.sqrt(tmpPowSum / MODEL);
    }

    /**
     * 字符串简单哈希求值
     *
     * @return
     */
    static double simpleHash() {
        Map<Integer, Integer> pointCount = new HashMap();
        for (String uuid : uuidList) {
            int hash = Math.abs(uuid.hashCode() % 100);
            pointCount.compute(hash, (k, v) -> v == null ? 1 : ++v);
        }
        //标准差计算公式
        double tmpPowSum = pointCount.values().stream().collect(Collectors.summingDouble(x -> Math.pow((x - AVG_COUNT), 2)));
        return Math.sqrt(tmpPowSum / MODEL);
    }

    /**
     * 字符串注意高位影响的哈希(参考HashMap)
     *
     * @return
     */
    static double bitHash() {
        Map<Integer, Integer> pointCount = new HashMap();
        for (String uuid : uuidList) {
            int hash = Math.abs((uuid.hashCode() ^ (uuid.hashCode() >>> 16)) % 100);
            pointCount.compute(hash, (k, v) -> v == null ? 1 : ++v);
        }
        //标准差计算公式
        double tmpPowSum = pointCount.values().stream().collect(Collectors.summingDouble(x -> Math.pow((x - AVG_COUNT), 2)));
        return Math.sqrt(tmpPowSum / MODEL);
    }


    /**
     * 字符串求MD5值取模
     *
     * @return
     */
    static double md5Hash() {
        Map<Integer, Integer> pointCount = new HashMap();
        for (String uuid : uuidList) {
            int md5Num=Integer.parseInt(DigestUtils.md5DigestAsHex(uuid.getBytes()).substring(30),16);
            int hash = Math.abs(md5Num % 100);
            pointCount.compute(hash, (k, v) -> v == null ? 1 : ++v);
        }
        //标准差计算公式
        double tmpPowSum = pointCount.values().stream().collect(Collectors.summingDouble(x -> Math.pow((x - AVG_COUNT), 2)));
        return Math.sqrt(tmpPowSum / MODEL);
    }

    /**
     * 字符串求MD5值后在hash
     *
     * @return
     */
    static double md5ThenHash() {
        Map<Integer, Integer> pointCount = new HashMap();
        for (String uuid : uuidList) {
            int hash = Math.abs(DigestUtils.md5DigestAsHex(uuid.getBytes()).hashCode() % 100);
            pointCount.compute(hash, (k, v) -> v == null ? 1 : ++v);
        }
        //标准差计算公式
        double tmpPowSum = pointCount.values().stream().collect(Collectors.summingDouble(x -> Math.pow((x - AVG_COUNT), 2)));
        return Math.sqrt(tmpPowSum / MODEL);
    }

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
        System.out.println("expect："+AVG_COUNT);
        System.out.println(baseTest() + "\tbase");
        System.out.println(randomRoutain() + "\trandomRoutain");
        System.out.println(simpleHash() + "\tsimpleHash");
        System.out.println(bitHash() + "\tbitHash from HashMap:");
        System.out.println(md5Hash() + "\tmd5Hash:");
        System.out.println(md5ThenHash() + "\tmd5ThenHash");
        System.out.println(murmurHash() + "\tmurmurHash");
    }
}