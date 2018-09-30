package concurrent.lang;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description 疑问：
 *                  背景：调用对象的startDeamonThread方法启动了一个守护线程
 *                      1.当我这个对象不在引用链——可以被回收时，这个线程不会自动终止嘛?
 *                          fixme 应该不会，因为此线程可能跟此对象无关啊，不管是否是守护线程。
 *
 *                      2.这个守护线程会不会影响这个对象的回收嘛？
 *                          fixme 如果有引用则会影响此对象的回收，需要注意对象处于可回收状态时，线程要释放对此对象的引用。
 * @Date 2018/10/1 上午12:09
 * -
 * @Author dugenkui
 **/

public class DeamonThreadGC {
    public AtomicInteger count=new AtomicInteger(0);


    private class ThreadDemo implements Runnable{


        @Override
        public void run() {

        }
    }
}
