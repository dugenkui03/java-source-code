package visual;

import java.util.concurrent.TimeUnit;

/**
 * 停止标志需要使用volatile标记，否则不能保证多个线程之间的可见性
 *
 * @author 杜艮魁
 * @date 2018/10/31
 */
public class SimpleDemo {
    private static boolean stopReq = false;//用volatile修饰则可及时停止使用此变量作为停止标志的线程

    public static void main(String[] args) throws InterruptedException {
        Thread task = new Thread(() -> {
            int i = 0;
            while (!stopReq) {
//                System.out.println(i++);//奇怪，打印的话也可以在一秒钟后停止此线程
                i++;
            }
        });
        task.start();

        TimeUnit.SECONDS.sleep(1);
        stopReq = true;
    }
}
