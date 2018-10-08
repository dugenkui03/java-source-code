package concurrent.shared;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用ReentrantLock实现信号量
 *
 * @author 杜艮魁
 * @date 2018/10/8
 */
public class SemaphoreOnLock {
    private final Lock lock = new ReentrantLock();
    private final Condition permitsAvailable = lock.newCondition();

    private int permits;

    public SemaphoreOnLock(int permits) {
        lock.lock();
        try {
            this.permits = permits;
        } finally {
            lock.unlock();
        }
    }

    public void acquire() throws InterruptedException {
        lock.lock();
        try {
            /**
             * 没有许可时线程挂起，并且await()会将状态值设置为0
             */
            while (permits <= 0) {
                permitsAvailable.await();
            }
            --permits;
        } finally {
            lock.unlock();
        }
    }

    public void release() {
        lock.lock();
        try {
            /**
             * 唤醒可能在没有许可时挂起的线程
             */
            ++permits;
            permitsAvailable.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
