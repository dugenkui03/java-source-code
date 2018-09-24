package concurrent;

import java.util.concurrent.TimeUnit;

/**
 * @Description TODO
 * @Date 2018/9/12 下午11:01
 * -
 * @Author dugenkui
 **/

public class InterrupteMethodTest {
    public static void main(String[] args) {

        Thread t1=new Thread(()->{
            System.out.println("start");
            try {
                TimeUnit.SECONDS.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("end");
        });

        t1.start();

        t1.interrupt();
    }
}
