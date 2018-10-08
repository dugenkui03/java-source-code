package base.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/** ReentrantLock 的 lock()方法可以连续调用两次，但是一般出现在递归调用的时候
 * @author 杜艮魁
 * @date 2018/10/8
 */
public class ReentrantLockTest {
    static ReentrantLock lock=new ReentrantLock();
    public static void main(String[] args) throws InterruptedException {
        lock.lock();
        lock.lock();
        new Thread(()->lock.lock()).start();

        TimeUnit.SECONDS.sleep(1);
        System.out.println("holdAccount"+lock.getHoldCount());
        System.out.println("wait node length:"+lock.getQueueLength());
    }
}
