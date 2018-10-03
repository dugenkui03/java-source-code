package performance;

import java.util.*;

/**
 * @Description 测试hashMap不同容量时的查询效率
 * 1.查询次数均为10w；
 * 2.查询数据随机，范围限定 1/2不在map中(缓存中)
 * <p>
 * 结论：<查询范围都是数据量的2倍，因此命中率均约为50%>
 * 50w数据，10W次查询，cost 22ms左右；
 * 500w数据，10W次查询，cost 同上
 * 2000w数据，10W次查询，cost 同上
 *
 * <p>
 * 重要结论：
 * 1.插入数据时发现每隔几次就会有消耗很长的 百万次插入数据，是最短消耗300ms的几十倍十几秒至20秒。fixme 扩容影响？扩容次数少啊
 *              1000000:824；2000000:3754
 * 2.实际工作中，由于热点区域的存在，数据量太大的话性能可能饿更糟糕——fixme 比如扩容时或者热点区插入因为数据量的原因被阻塞
 * @Date 2018/10/3 上午9:14
 * -
 * @Author dugenkui
 **/

public class HashMapTest {
    static int scale = 100000000;
    static int queryCount = 100000;

    public static void main(String[] args) {
        /**
         * 缓存数列
         */
        Map<String, Integer> dataHolder = new HashMap<>();
        long c1=System.currentTimeMillis();
        for (int i = 0; i < scale; i++) {
            dataHolder.put(i + "", i);
            //由打印即可直到随着插入数据越来越多，插入速度/打印时间 越来越慢
            if (i % 1000000 == 0){
                System.out.println(i+":"+(System.currentTimeMillis()-c1));
                c1=System.currentTimeMillis();
            }
        }

        /**
         * 要查询的数据，是缓存数据范围的两倍。(提前生成随机数列,防止范围不一致导致查询时的时间开销不同)
         */
        Random query = new Random(System.currentTimeMillis());
        List<Integer> queryDataHolder = new ArrayList(scale);
        for (int i = 0; i < queryCount; i++) {
            queryDataHolder.add(query.nextInt(scale * 2));
        }


        List<Integer> cacheHolder = new LinkedList<>();
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < queryCount; i++) {
            Integer res = dataHolder.get(queryDataHolder.get(i));
            if (res != null) {
                cacheHolder.add(res);
            }
        }

        long t2 = System.currentTimeMillis();
        System.out.println("cache find:" + cacheHolder.size() + "。cost：" + (t2 - t1));
    }
}
