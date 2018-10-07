package concurrent.shared;

import concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @author 单发闭锁：1表示开发状态；其他表示非开放状态。
 *
 * @date 2018/10/7
 */
public class OneShotLatch {

    private class Sync extends AbstractQueuedSynchronizer{
        @Override
        protected int tryAcquireShared(int ignored) {
            /**
             * state为1表示闭锁时候开放状态，返回1
             */
            return (getState()==1)?1:-1;
        }

        @Override
        protected boolean tryReleaseShared(int ignored) {
            /**
             * 因为是释放操作，所以状态设置为1
             */
            setState(1);
            return true;
        }
    }

    private final Sync sync=new Sync();

    public void singal(){
        /**
         * 会调用Sync中实现的tryReleaseShared()
         */
        sync.releaseShared(0);
    }

    public void await() throws InterruptedException {
        /**
         * 会调用Sync中实现的tryAcquireShared()
         */
        sync.acquireSharedInterruptibly(0);
    }

}
