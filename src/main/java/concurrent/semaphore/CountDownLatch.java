//package concurrent.semaphore;
//
///**
// * @Description TODO
// * @Date 2018/9/25 上午1:00
// * -
// * @Author dugenkui
// **/
//
//import concurrent.locks.AbstractQueuedSynchronizer;
//
//import java.util.concurrent.TimeUnit;
//
///**
// * A synchronization aid that allows one or more threads to wait until
// * a set of operations being performed in other threads completes.
// *
// * <p>A {@code CountDownLatch} is initialized with a given <em>count</em>.
// * The {@link #await await} methods block until the current count reaches
// * zero due to invocations of the {@link #countDown} method, after which
// * all waiting threads are released and any subsequent invocations of
// * {@link #await await} return immediately.  This is a one-shot phenomenon
// * -- the count cannot be reset.  If you need a version that resets the
// * count, consider using a {@link CyclicBarrier}.
// *
// * <p>A {@code CountDownLatch} is a versatile synchronization tool
// * and can be used for a number of purposes.  A
// * {@code CountDownLatch} initialized with a count of one serves as a
// * simple on/off latch, or gate: all threads invoking {@link #await await}
// * wait at the gate until it is opened by a thread invoking {@link
// * #countDown}.  A {@code CountDownLatch} initialized to <em>N</em>
// * can be used to make one thread wait until <em>N</em> threads have
// * completed some action, or some action has been completed N times.
// *
// * <p>A useful property of a {@code CountDownLatch} is that it
// * doesn't require that threads calling {@code countDown} wait for
// * the count to reach zero before proceeding, it simply prevents any
// * thread from proceeding past an {@link #await await} until all
// * threads could pass.
// *
// * <p><b>Sample usage:</b> Here is a pair of classes in which a group
// * of worker threads use two countdown latches:
// * <ul>
// * <li>The first is a start signal that prevents any worker from proceeding
// * until the driver is ready for them to proceed;
// * <li>The second is a completion signal that allows the driver to wait
// * until all workers have completed.
// * </ul>
// *
// *  <pre> {@code
// * class Driver { // ...
// *   void main() throws InterruptedException {
// *     CountDownLatch startSignal = new CountDownLatch(1);
// *     CountDownLatch doneSignal = new CountDownLatch(N);
// *
// *     for (int i = 0; i < N; ++i) // create and start threads
// *       new Thread(new Worker(startSignal, doneSignal)).start();
// *
// *     doSomethingElse();            // don't let run yet
// *     startSignal.countDown();      // let all threads proceed
// *     doSomethingElse();
// *     doneSignal.await();           // wait for all to finish
// *   }
// * }
// *
// * class Worker implements Runnable {
// *   private final CountDownLatch startSignal;
// *   private final CountDownLatch doneSignal;
// *   Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
// *     this.startSignal = startSignal;
// *     this.doneSignal = doneSignal;
// *   }
// *   public void run() {
// *     try {
// *       startSignal.await();
// *       doWork();
// *       doneSignal.countDown();
// *     } catch (InterruptedException ex) {} // return;
// *   }
// *
// *   void doWork() { ... }
// * }}</pre>
// *
// * <p>Another typical usage would be to divide a problem into N parts,
// * describe each part with a Runnable that executes that portion and
// * counts down on the latch, and queue all the Runnables to an
// * Executor.  When all sub-parts are complete, the coordinating thread
// * will be able to pass through await. (When threads must repeatedly
// * count down in this way, instead use a {@link CyclicBarrier}.)
// *
// *  <pre> {@code
// * class Driver2 { // ...
// *   void main() throws InterruptedException {
// *     CountDownLatch doneSignal = new CountDownLatch(N);
// *     Executor e = ...
// *
// *     for (int i = 0; i < N; ++i) // create and start threads
// *       e.execute(new WorkerRunnable(doneSignal, i));
// *
// *     doneSignal.await();           // wait for all to finish
// *   }
// * }
// *
// * class WorkerRunnable implements Runnable {
// *   private final CountDownLatch doneSignal;
// *   private final int i;
// *   WorkerRunnable(CountDownLatch doneSignal, int i) {
// *     this.doneSignal = doneSignal;
// *     this.i = i;
// *   }
// *   public void run() {
// *     try {
// *       doWork(i);
// *       doneSignal.countDown();
// *     } catch (InterruptedException ex) {} // return;
// *   }
// *
// *   void doWork() { ... }
// * }}</pre>
// *
// * <p>Memory consistency effects: Until the count reaches
// * zero, actions in a thread prior to calling
// * {@code countDown()}
// * <a href="package-summary.html#MemoryVisibility"><i>happen-before</i></a>
// * actions following a successful return from a corresponding
// * {@code await()} in another thread.
// *
// * @since 1.5
// * @author Doug Lea
// */
//public class CountDownLatch {
//    /**
//     * Synchronization control For CountDownLatch.
//     * Uses AQS state to represent count.
//     *
//     * CountDownLatch的同步控制，使用AQS状态代表count
//     */
//    private static final class Sync extends AbstractQueuedSynchronizer {
//
//        private static final long serialVersionUID = 4982264981922014374L;
//
//        /**
//         * 只有在构造参数中才能够写入count值
//         * @param count
//         */
//        Sync(int count) {
//            setState(count);
//        }
//
//        /**
//         * 使用AQS状态(int)代表count数量，getCount是通过获取AQS状态的方法获取count值
//         */
//        int getCount() {
//            return getState();
//        }
//
//        /**
//         * 当前状态是否为0，即已经可以执行后继程序，是则1，否则-1。
//         */
//        @Override
//        protected int tryAcquireShared(int acquires) {
//            return (getState() == 0) ? 1 : -1;
//        }
//
//        /**
//         * fixme 只有一种状态返回true：statu/count 值刚减小到0时。已经为0或者还未到0都返回false。返回值表示是否尝试释放所有等待的线程
//         * Decrement count - AQS的状态值代表count数，此方法为使用CAS操作减小state数量
//         * signal when transition to zero - 当状态值为0时，返回false
//         */
//        @Override
//        protected boolean tryReleaseShared(int releases) {
//            for (;;) {
//                int c = getState();
//                //当前值state为0，则返回false
//                if (c == 0)
//                    return false;
//                //CAS操作减小statu值，操作后的值为0，true。
//                int nextc = c-1;
//                if (compareAndSetState(c, nextc))
//                    return nextc == 0;
//            }
//        }
//    }
//
//    private final Sync sync;
//
//    /**
//     * Constructs a {@code CountDownLatch} initialized with the given count.
//     *
//     * @param count the number of times {@link #countDown} must be invoked
//     *        before threads can pass through {@link #await}
//     * @throws IllegalArgumentException if {@code count} is negative
//     */
//    public CountDownLatch(int count) {
//        if (count < 0) throw new IllegalArgumentException("count < 0");
//        this.sync = new Sync(count);
//    }
//
//    /**
//     * Causes the current thread to wait until the latch has counted down to
//     * zero, unless the thread is {@linkplain Thread#interrupt interrupted}.
//     *
//     * <p>If the current count is zero then this method returns immediately.
//     *
//     * <p>If the current count is greater than zero then the current
//     * thread becomes disabled for thread scheduling purposes and lies
//     * dormant until one of two things happen:
//     * <ul>
//     * <li>The count reaches zero due to invocations of the
//     * {@link #countDown} method; or
//     * <li>Some other thread {@linkplain Thread#interrupt interrupts}
//     * the current thread.
//     * </ul>
//     *
//     * <p>If the current thread:
//     * <ul>
//     * <li>has its interrupted status set on entry to this method; or
//     * <li>is {@linkplain Thread#interrupt interrupted} while waiting,
//     * </ul>
//     * then {@link InterruptedException} is thrown and the current thread's
//     * interrupted status is cleared.
//     *
//     * @throws InterruptedException if the current thread is interrupted
//     *         while waiting
//     */
//    public void await() throws InterruptedException {
//        sync.acquireSharedInterruptibly(1);
//    }
//
//    /**
//     * Causes the current thread to wait until the latch has counted down to
//     * zero, unless the thread is {@linkplain Thread#interrupt interrupted},
//     * or the specified waiting time elapses.
//     *
//     * <p>If the current count is zero then this method returns immediately
//     * with the value {@code true}.
//     *
//     * <p>If the current count is greater than zero then the current
//     * thread becomes disabled for thread scheduling purposes and lies
//     * dormant until one of three things happen:
//     * <ul>
//     * <li>The count reaches zero due to invocations of the
//     * {@link #countDown} method; or
//     * <li>Some other thread {@linkplain Thread#interrupt interrupts}
//     * the current thread; or
//     * <li>The specified waiting time elapses.
//     * </ul>
//     *
//     * <p>If the count reaches zero then the method returns with the
//     * value {@code true}.
//     *
//     * <p>If the current thread:
//     * <ul>
//     * <li>has its interrupted status set on entry to this method; or
//     * <li>is {@linkplain Thread#interrupt interrupted} while waiting,
//     * </ul>
//     * then {@link InterruptedException} is thrown and the current thread's
//     * interrupted status is cleared.
//     *
//     * <p>If the specified waiting time elapses then the value {@code false}
//     * is returned.  If the time is less than or equal to zero, the method
//     * will not wait at all.
//     *
//     * @param timeout the maximum time to wait
//     * @param unit the time unit of the {@code timeout} argument
//     * @return {@code true} if the count reached zero and {@code false}
//     *         if the waiting time elapsed before the count reached zero
//     * @throws InterruptedException if the current thread is interrupted
//     *         while waiting
//     */
//    public boolean await(long timeout, TimeUnit unit)
//            throws InterruptedException {
//        return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
//    }
//
//    /**
//     * Decrements the count of the latch, releasing all waiting threads if
//     * the count reaches zero.
//     *
//     * <p>If the current count is greater than zero then it is decremented.
//     * If the new count is zero then all waiting threads are re-enabled for
//     * thread scheduling purposes.
//     *
//     * <p>If the current count equals zero then nothing happens.
//     *
//     * <p></p>
//     *  通过CAS操作减小AQS的状态值来减小latch的count值，如果count值为0，则释放所有等待的线程:
//     *          1.如果当前线程大于0，则操作仅仅是decrement操作；
//     *          2.如果操作后的线程为0，则所有等待的线程重新启用；
//     *          3.如果当前count值为0，则不会发生任何事。
//     */
//    public void countDown() {
//        sync.releaseShared(1);
//    }
//
//    /**
//     * Returns the current count.
//     *
//     * <p>This method is typically used for debugging and testing purposes.
//     *
//     * @return the current count
//     */
//    public long getCount() {
//        return sync.getCount();
//    }
//
//    /**
//     * Returns a string identifying this latch, as well as its state.
//     * The state, in brackets, includes the String {@code "Count ="}
//     * followed by the current count.
//     *
//     * @return a string identifying this latch, as well as its state
//     */
//    public String toString() {
//        return super.toString() + "[Count = " + sync.getCount() + "]";
//    }
//}
