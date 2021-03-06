package algo.hash;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import data.DataHolder;

import java.util.LinkedList;
import java.util.List;

import static cons.PathCons.UUID_PATH;

/**
 * @Description 5w数据分别直接计算和采用调试过的LRU缓存策略时间对比。
 *                  数据(dis:total=1:3).实验证明直接计算快。
 *                  原因可能有2：
 *                         1.数据命中率太低；
 *                         2.即使命中，从缓存取数据的hash算法不一定比murmurHash算法快多少
 * @Date 2018/9/29 上午10:35
 * -
 * @Author dugenkui
 **/

public class ConcurrentLinkedHashMapTest {
    private final int cacheSize = 65534;

    //    速度比 3：1
    private final Cache<String, String> cache = Caffeine.newBuilder().maximumSize(cacheSize).build();

//    速度比：5：1
//    private final Cache<String,String> cache= Caffeine.newBuilder()
//            .maximumSize(cacheSize).expireAfterAccess(1, TimeUnit.MICROSECONDS).build();

//      速度比 3：1
//    private final Cache<String,String> cache= Caffeine.newBuilder()
//            .maximumSize(cacheSize).expireAfterWrite(25, TimeUnit.MICROSECONDS).build();


    public static void main(String[] args) {
        ConcurrentLinkedHashMapTest demoInstance = new ConcurrentLinkedHashMapTest();
        List<String> uuidList=DataHolder.newBuilder(UUID_PATH+"uuid2.txt").tag("tag").build().uuidList;

        /**
         * 减小局部性原理 和 即使编译 的影响：调用数据和5w此函数
         */
        List<String> h0 = new LinkedList<>();
        for (String uuid : uuidList) {
            h0.add(HashAlgorithm.murmurHash(uuid));
        }

        /**
         * 使用caffeine缓存
         */
        long t1 = System.currentTimeMillis();
        List<String> h1 = new LinkedList<>();
        for (String uuid :uuidList) {
            h1.add(demoInstance.cache.get(uuid, k -> HashAlgorithm.murmurHash(k)));
        }

        /**
         * 直接计算
         */
        long t2 = System.currentTimeMillis();
        List<String> h2 = new LinkedList<>();
        for (String uuid : uuidList) {
            h2.add(HashAlgorithm.murmurHash(uuid));
        }
        long t3 = System.currentTimeMillis();

        System.out.println("使用缓存和直接计算的时间比\t" + (t2 - t1) + ":" + (t3 - t2));
    }
}
