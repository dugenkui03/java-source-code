package deadlock;


import java.util.concurrent.TimeUnit;

/**
 * @Description 简单顺序死锁示例:两个线程以不同的顺序对相同的资源加锁
 * @Date 2018/10/5 上午8:34
 * -
 * @Author dugenkui
 **/

public class OrderDeadLockTest {
    private final Object left = new Object();
    private final Object right = new Object();

    public void left2Right() {
        synchronized (left) {
            sleeping();
            synchronized (right) {
                System.out.println("l2r:get all monitor");
            }
        }
    }

    public void right2Left() {
        synchronized (right) {
            sleeping();
            synchronized (left) {
                System.out.println("r2l:get all monitor");
            }
        }
    }

    private void sleeping() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        OrderDeadLockTest demo = new OrderDeadLockTest();

        Runnable t1 = () -> demo.left2Right();
        Runnable t2 = () -> demo.right2Left();

        new Thread(t1).start();
        new Thread(t2).start();
    }
}
