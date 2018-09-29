package algo.hash;

import data.DataHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cons.PathCons.UUID_PATH;

/**
 * @Description 取模时使用 % 和 & 的速度对比
 * 使用1023和1022，两个相近的数
 * <p>
 * todo 两种方式交替执行可知 & 稍快(50w数据能快10ms)。
 * 还是未能消除某些因素影响，比如局部性或者jit。而且逆序两个算法实行顺序后可查 & 的确快一些
 * @Date 2018/9/29 下午4:08
 * -
 * @Author dugenkui
 **/

public class BitCompute {
    private final List<String> uuidList = DataHolder.newBuilder(UUID_PATH+"uuid2.txt").build().uuidList;


    public static void main(String[] args) {
        BitCompute demo = new BitCompute();

        Map<Integer, Integer> h0 = new HashMap<>();
        Map<Integer, Integer> h1 = new HashMap<>();
        Map<Integer, Integer> h2 = new HashMap<>();

        /**
         * 消除局部性和jit的影响
         */
        for (int i = 0; i < Integer.MAX_VALUE >>> 8; i++) {
            int hashCode = demo.uuidList.get(i % demo.uuidList.size()).hashCode();
            h0.compute(hashCode, (k, v) -> v == null ? 1 : ++v);
        }

        long t1 = System.currentTimeMillis();
//        for (String uuid : demo.uuidList) {
//            int hashCode = uuid.hashCode() & 1023;
//            h1.compute(hashCode, (k, v) -> v == null ? 1 : ++v);
//        }

        long t2 = System.currentTimeMillis();
        for (String uuid : demo.uuidList) {
            int hashCode = uuid.hashCode() % 1021;
            h2.compute(hashCode, (k, v) -> v == null ? 1 : ++v);
        }
        long t3 = System.currentTimeMillis();

        System.out.println("位运算 和 %取模 性能比\t" + (t2 - t1) + ":" + (t3 - t2));
    }
}
