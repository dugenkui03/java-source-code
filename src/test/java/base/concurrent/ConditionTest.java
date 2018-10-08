package base.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition的await()方法 挂起当前线程 并且 将状态清空到0(相当于synchronized释放锁)
 * @author 杜艮魁
 * @date 2018/10/8
 */
public class ConditionTest {
    static Lock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {

        Runnable task = () -> {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + " hold lock monitor success");
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        /**
         * 使用lock加锁并挂起当前线程
         */
        new Thread(task).start();
        TimeUnit.SECONDS.sleep(1);


        new Thread(task).start();
    }
}
