package deadlock;

import java.util.concurrent.TimeUnit;

/**
 * @Description 测试调用已经获取的资源对象的wait()放弃锁的方式来破解死锁 todo:实际开发中需要配合Lock类一起使用，因为synchronized会阻塞
 * @Date 2018/10/5 上午8:56
 * -
 * @Author dugenkui
 **/

public class OrderDeadLocal4WatiTest {
    private Object left;
    private Object right;

    public OrderDeadLocal4WatiTest(Object left, Object right) {
        this.left = left;
        this.right = right;
    }

    public void left2Right() {
        synchronized (left) {
            System.out.println("waiting");
            waiting();
            System.out.println("start work");
            synchronized (right) {
                System.out.println("l2r:get all monitor");
            }
        }
    }

    public void right2left() {
        synchronized (right) {
            sleeping();
            System.out.println("wait up");
            synchronized (left) {
                System.out.println("l2r:get all monitor");
                left.notify();
                right.notify();
            }
        }
    }

    private void waiting() {
        try {
            left.wait(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
        Object lockX = new Object();
        Object lockY = new Object();

        OrderDeadLocal4WatiTest objX = new OrderDeadLocal4WatiTest(lockX, lockY);
        OrderDeadLocal4WatiTest objY = new OrderDeadLocal4WatiTest(lockX, lockY);

        new Thread(() -> objX.left2Right()).start();
        new Thread(() -> objY.right2left()).start();

    }
}
