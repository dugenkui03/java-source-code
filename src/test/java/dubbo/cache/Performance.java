package dubbo.cache;


import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试场景：
 *      1.缓存大小65536；
 *      2.命中率50%;
 *      3.测试1亿次，约5000w数据不命中并被GC;
 *      4.存在于缓存则直接，不存在则加入并返回。
 *
 * 注意及说明：
 *      1. GC时间，JIT编译时间和时空局部性的影响——因此分开执行且执行足够长时间，减小其影响。详情见《java并发实战》12.3小节(避免性能测试的陷阱),P220
 *      2. 两者命中率需要保持一致，变量是并发读取——通过控制invoke的key的返回实现；
 *      3. CACHE_SIZE 和 KEY_SCOPE 之比约等于命中率，但是由于前期map为空，命中率为0，因此命中率稍微小于两数之比;而且由于读写锁可以并发读，所以命中率可能更低。
 *
 * 输出说明：
 *     1.编译时间、类加载时间；
 *     2.gc次数和时间；
 *     3.总用时。
 *
 * 数据：
 *      new:25195220802 24708335241
 *      old:45230098564 50%
 *
 * 结论：
 *      读写说性能更高；
 *      jit和gc时间相对总时长很小，值相差不大，因此换锁对gc和jit无影响。
 */
public class Performance {
    static final int CACHE_SIZE=1<<16;
    static final int KEY_SCOPE=1<<20;
    static final int INVOKE_TIMES=10000000;
//                                 8743965;

    static final AtomicInteger count=new AtomicInteger(0);

    static LRUCache<String, FutureTask<String>> cache = new LRUCache(CACHE_SIZE);

    static class Invocation implements Callable {
        private LRUCache<String, FutureTask<String>> cache;
        private String key;

        public Invocation(LRUCache<String, FutureTask<String>> cache, String key) {
            this.cache = cache;
            this.key = key;
        }

        @Override
        public Object call() throws Exception {
            return invoke(key);
        }

        String invoke(String key) throws ExecutionException, InterruptedException {
            FutureTask<String> task = cache.get(key);
            if (task != null) {
                count.addAndGet(1);
                return task.get();
            }
            task = new FutureTask(() -> new String(key));//新的String对象，可以GC
            FutureTask<String> existedTask = cache.putIfAbsent(key, task);
            if (existedTask == null) {
                existedTask = task;
                existedTask.run();
            }
            return existedTask.get();
        }
    }

    public static void main(String[] args){
        ExecutorService es = Executors.newCachedThreadPool();

        Random random = new Random(System.currentTimeMillis());
        long start = System.nanoTime();
        for (int i = 0; i < INVOKE_TIMES; i++) {
            Invocation invocation = new Invocation(cache, random.nextInt(KEY_SCOPE) + "");
            es.submit(invocation);
        }
        es.shutdown();

        System.out.println(System.nanoTime() - start);
        System.out.println(count.get());
    }
}
