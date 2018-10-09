package dubbo.cache;

import java.util.Random;
import java.util.concurrent.*;

/**
 * 结果：
 *      LRUCache:1607
 *      LRUCacheOld: 100588
 *
 * 结论：
 *      读写锁性能更高；
 */
public class Performance {
    static final int CACHE_SIZE=1<<16;
    static final int INVOKE_TIMES=50000;

    static Random rand=new Random();

    static LRUCache<String,String> cache = new LRUCache(CACHE_SIZE);
    static{
        for (int i = 0; i < CACHE_SIZE; i++) {
            cache.put(i+"","");
        }
    }

    public static void main(String[] args){
        ExecutorService executorService = Executors.newFixedThreadPool(5000);

        Runnable getTask=()->cache.get(rand.nextInt(CACHE_SIZE*2)+"");

        long start = System.currentTimeMillis();
        for (int i = 0; i < INVOKE_TIMES; i++) {
            executorService.execute(getTask);
        }
        executorService.shutdown();
        while(!executorService.isTerminated()){ }

        System.out.println(System.currentTimeMillis() - start);
    }
}
