package deadlock;

/**
 * @Description 对应DynamicOrderDeadLockTest：
 *          fixme: 当使用多个参数对象加锁时，需要按照一定次序加锁,比如 System.identifityHashCode方法或者加锁对象唯一的可比较的变量值;
 *          todo:如果System.idenfifityHashCode()相同(极小可能发生)而且没有唯一可比较键值，则在获必要的锁在前先获得"加时赛锁"。
 * @Date 2018/10/5 上午11:33
 * -
 * @Author dugenkui
 **/

public class OrderLockInMethodTest {

    /**
     * 总是现对hashcode比较大的参数进行加锁
     */
    static void transferMoney(final String x,final String y){
        if(System.identityHashCode(x)>System.identityHashCode(y)){
            synchronized (x){
                synchronized (y){
                    System.out.println("transfer "+x+"to "+y);
                }
            }
        }else if(System.identityHashCode(x)<System.identityHashCode(y)){
            synchronized (y){
                synchronized (x){
                    System.out.println("transfer "+x+"to "+y);
                }
            }
        }
        /**
         * 两个参数hashCode相同，则先获取加时赛锁，然后在获取两个账号的锁
         * fixme: 注意 加时赛锁 一定是全局唯一，否则没意义，相互等锁的线程仍然可能形成环状
         */
        else{
            synchronized (OrderLockInMethodTest.class){
                synchronized (x){
                    synchronized (y){
                        System.out.println("transfer "+x+"to "+y);
                    }
                }
            }
        }
    }
}
