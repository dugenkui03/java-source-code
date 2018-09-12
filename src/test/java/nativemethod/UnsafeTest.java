package nativemethod;

import sun.misc.Unsafe;

/**
 * @Description Unsafe类的cas操作
 * @Date 2018/9/12 上午9:33
 * -
 * @Author dugenkui
 **/

public class UnsafeTest {

    private int status;

    private static final Unsafe UNSAFE;

    private static final long statusOffset;


    static {
        try {
            UNSAFE = Unsafe.getUnsafe();

            Class<?> clz = UnsafeTest.class;
            statusOffset = UNSAFE.objectFieldOffset(clz.getField("status"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }


    /**
     * 使用CAS累加，并返回旧值
     */
    public int casFunc() {
        int old;
        {
            old = UNSAFE.getIntVolatile(this, statusOffset);
        }
        while (!UNSAFE.compareAndSwapInt(this, statusOffset, old, old + 1)) ;

        return old;
    }

    /**
     * getAndAddInt使用comparaAndSwapInt实现，三个参数含义分别是：
     * 要改变的对象、对象偏移量、增加的值。操作是线程安全的
     */
    public int addFunc() {
        return UNSAFE.getAndAddInt(this, statusOffset, 1);
    }

}