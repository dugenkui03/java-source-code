package deadlock;

import java.util.concurrent.TimeUnit;

/**
 * @Description 动态顺序死锁:如果加锁对象是方法参数引用的对象，则很难控制加锁的顺序
 * @Date 2018/10/5 上午11:22
 * -
 * @Author dugenkui
 **/

public class DynamicOrderDeadLockTest {
    static void transferMoney(final String from, final String to) {
        synchronized (from) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
            }
            synchronized (to) {
                System.out.println(from + "->" + to);
            }
        }
    }

    public static void main(String[] args) {
        String accountX = "dugenkui";
        String accountY = "telangpu";

        new Thread(() -> transferMoney(accountX, accountY)).start();
        new Thread(() -> transferMoney(accountY, accountX)).start();
    }
}
