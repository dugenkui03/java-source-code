package cache.expiring;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @Description 数据会过期的map - 包含一个线程去检查数据是否过期
 *
 *          注意不使用这个缓存时，需要调用clear方法或者stopExpiring()方法停止工作线程，否则存活的线程和相互引用的对象导致缓存对象不能被回收。
 *
 * @Date 2018/9/30 下午11:01
 * -
 * @Author dugenkui
 **/

public class ExpiringMap<K, V> implements Map<K, V> {

    /**
     * 数据默认存活时间
     */
    private static final int DEFAULT_TIME_TO_LIVE = 1 << 8;

    /**
     * 检查线程默认执行的时间周期
     */
    private static final int DEFAULT_EXPIRATION_INTERVAL = 1;

    private static volatile int expireCount = 1;

    /**
     * fixme 负责检查数据是否过期
     */
    private final ExpireThread expireThread;

    /**
     * fixme 存放数据的缓存
     */
    private final ConcurrentHashMap<K, ExpiryObject> delegateMap;

    public ExpiringMap() {
        this(DEFAULT_TIME_TO_LIVE, DEFAULT_EXPIRATION_INTERVAL);
    }

    public ExpiringMap(long timeToLive, long expirationInterval) {
        this(new ConcurrentHashMap(), timeToLive, expirationInterval);
    }

    private ExpiringMap(ConcurrentHashMap<K, ExpiryObject> delegateMap, long timeToLive, long expirationInterval) {
        this.delegateMap = delegateMap;

        this.expireThread = new ExpireThread();
        expireThread.setTimeToLive(timeToLive);
        expireThread.setExpirationInterval(expirationInterval);
    }

    public ExpireThread getExpireThread() {
        return expireThread;
    }

    @Override
    public int size() {
        return delegateMap.size();
    }

    @Override
    public boolean isEmpty() {
        return delegateMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return delegateMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return delegateMap.containsKey(value);
    }

    @Override
    public V get(Object key) {
        ExpiryObject obj = delegateMap.get(key);
        if(obj!=null){
            //获取时，更新时间
            obj.setLastAccessTime(System.currentTimeMillis());
            return obj.getValue();
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        /**
         * 记录数据放入缓存时间。put返回之前和此key关联的value，无则null
         */
        ExpiryObject answer = delegateMap.put(key, new ExpiryObject(key, value, System.currentTimeMillis()));
        if (answer == null) {
            return null;
        }
        return answer.getValue();
    }

    @Override
    public V remove(Object key) {
        //obj 可能为null，要注意
        ExpiryObject obj = delegateMap.remove(key);
        if (obj == null) {
            return null;
        }
        return obj.getValue();
    }

    /**
     * 复用已有方法
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.entrySet().forEach(x ->
                this.put(x.getKey(), x.getValue())
        );
    }

    /**
     * remove all mapping from map。
     * fxime 只有调用clear方法才能停止线程
     */
    @Override
    public void clear() {
        delegateMap.clear();
        expireThread.stopExpiring();
    }

    @Override
    public Set<K> keySet() {
        return delegateMap.keySet();
    }

    @Override
    public Collection<V> values() {
        return delegateMap.values().stream() //获取delegateMap的value对象流(k,v,time)
                .map(x -> x.getValue()) //取出ExpireObject对象流中的value值
                .collect(Collectors.toList()); //收集为List返回
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }


    public class ExpireThread implements Runnable {
        private long timeToLive;
        private long expirationInterval;
        private volatile boolean running = false;
        private final Thread expirerThread;

        public ExpireThread() {
            expirerThread = new Thread(this, "expirymapExpire-" + expireCount++);
            /**
             * 守护线程两个特点：1.只剩下守护线程时，JVM停止；2.在线程启动前调用此方法
             */
            expirerThread.setDaemon(true);
        }

        @Override
        public void run() {
            while (running) {
                processExpires();
                try {
                    //sleep放弃CUP时间片，但是不会放弃锁。fixme speep、wait、join期间如果调用interrup方法，则抛异常，并破坏循环条件而停止任务
                    Thread.sleep(expirationInterval);
                } catch (InterruptedException e) {
                    running = false;
                }
            }
        }

        private void processExpires() {
            long timeNow = System.currentTimeMillis();
            for (ExpiryObject o : delegateMap.values()) {
                //fixme 存活时间设置负数表示不会移除数据
                if (timeToLive <= 0) {
                    continue;
                }
                long timeIdle = timeNow - o.getLastAccessTime();
                if (timeIdle >= timeToLive) {
                    //注意这是一个并发map，所有没有并发问题存在
                    delegateMap.remove(o.getKey());
                }
            }
        }

        /**
         * 开始检查任务
         */
        public void startExpiring() {
            if (!running) {
                running = true;
                expirerThread.start();
            }
        }

        /**
         * 检查任务没启动的话则启动
         */
        public void startExpiryIfNotStarted() {
            if (running) {
                return;
            }
            startExpiring();
        }

        /**
         * 停止任务
         */
        public void stopExpiring() {
            if (running) {
                running = false;
                expirerThread.interrupt();
            }
        }

        public boolean isRunning() {
            return running;
        }

        public void setTimeToLive(long timeToLive) {
            this.timeToLive = timeToLive;
        }

        public void setExpirationInterval(long expirationInterval) {
            this.expirationInterval = expirationInterval;
        }

        @Override
        public String toString() {
            return "ExpireThread{" +
                    ", timeToLiveMillis=" + timeToLive +
                    ", expirationIntervalMillis=" + expirationInterval +
                    ", running=" + running +
                    ", expirerThread=" + expirerThread +
                    '}';
        }
    }

    /**
     * 将value包装正带有时间的数据对象
     */
    private class ExpiryObject {
        private K key;
        private V value;
        private AtomicLong lastAccessTime;

        ExpiryObject(K key, V value, long lastAccessTime) {
            if (value == null) {
                throw new IllegalArgumentException("过期对象不能为null");
            }
            this.key = key;
            this.value = value;
            this.lastAccessTime = new AtomicLong(lastAccessTime);
        }

        public long getLastAccessTime() {
            return lastAccessTime.get();
        }

        public void setLastAccessTime(long lastAccessTime) {
            this.lastAccessTime.set(lastAccessTime);
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        @Override
        public boolean equals(Object obj) {
            return value.equals(obj);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public String toString() {
            return "ExpiryObject{ key=" + key + ",value=" + value + ",lastAccessTime=" + lastAccessTime + "}";
        }
    }
}
