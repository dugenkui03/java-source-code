package jvm;

import java.util.concurrent.TimeUnit;

/**
 * @Description 类的<clinit>()方法由编译器收集 所有类变量的赋值动作和静态语句块合并而成。
 *              收集顺序由语句在源文件中出现顺序决定，静态语句块只能访问之前定义的变量，之后定义的变量可以赋值但不能访问.
 *          类的<clinit>()方法在多线程环境下会被加锁，多个线程初始化类只有一个线程成功执行，其他线程阻塞直至其完成。
 * @Date 2018/9/13 下午3:45
 * -
 * @Author dugenkui
 **/

class ClinitDemo{
    static{
        i=1;
//        System.out.println(i); 编译报错：非法向前引用
        try {
            System.out.println(Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {}
    }

    public static int i;
}

public class InitClinitMethodTest {
    public static void main(String[] args) {
        Runnable t0=()->System.out.println(ClinitDemo.i);
        Runnable t1=()->System.out.println(ClinitDemo.i);

        new Thread(t0).start();
        new Thread(t1).start();
    }
}
