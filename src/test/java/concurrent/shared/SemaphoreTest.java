package concurrent.shared;

import java.util.concurrent.*;

/**
 * 目的，在添加数据操作需要互斥、全部任务为读取时是线程安全的情况下，
 * 可以使用Semaphore的公平锁来提高吞吐量，而非使用互斥锁。思路是读取时
 * 仅仅获取一个许可，而添加数据时获取全部许可。
 *
 * 注意：
 * 没有获取许可也能释放许可—相应的statu+1。但是释放后的总许可数量不能超过Integer.MAX_VALUE;
 *      int next = current + releases;
 *      if (next < current) // overflow
 *          throw new Error("Maximum permit count exceeded");
 *
 * 结论：ok
 *
 * @author 杜艮魁
 * @date 2018/10/9
 */
public class SemaphoreTest {
    /**
     * 注意一定要用公平锁，非公平锁在高并发情况下会导致put任务难以执行
     */
    private static final Semaphore sem = new Semaphore(Integer.MAX_VALUE, true);

    public static void main(String[] args) throws InterruptedException {

//        结论1
//        sem.release();
//        System.out.println(sem.availablePermits());

        /**
         * 需一个许可的读取任务
         */
        Runnable getTask = () -> {
            try {
                sem.acquire();//默认获取一个许可
                for (int i = 0; i < 5; i++) {
                    System.out.println("getting data " + Thread.currentThread().getName() + "..."+i);
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
            } finally {
                sem.release();
            }
        };

        /**
         * 需全部Integer.MAX_VALUE许可的添加数据任务
         */
        Runnable putTask = () -> {
            try {
                sem.acquire(Integer.MAX_VALUE);
                for (int i = 0; i < 5; i++) {
                    System.out.println("putting data " + Thread.currentThread().getName() + "..."+i);
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
            } finally {
                sem.release(Integer.MAX_VALUE);
            }
        };


        /**
         * 模拟这种情形，前两个线程并发执行读取任务，第三个线程put阻塞，此时第四个任务也会阻塞，会等待第三个任务执行结束后再执行
         */
        ExecutorService es= Executors.newCachedThreadPool();
        es.submit(getTask);
        TimeUnit.MILLISECONDS.sleep(10);

        es.submit(getTask);
        TimeUnit.MILLISECONDS.sleep(10);

        es.submit(putTask);
        TimeUnit.MILLISECONDS.sleep(10);

        es.submit(getTask);

        es.shutdown();
    }
}
