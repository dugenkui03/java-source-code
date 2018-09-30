package cache.threadlocal;

import cache.Cache;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 每个线程对应一个缓存；
 *              应该很少机会用到这个，因为线程池可能管理了很多个线程，fixme 而他们处理的任务可能有重复
 * @Date 2018/9/30 下午10:01
 * -
 * @Author dugenkui
 **/

public class ThreadLocalCache<K,V> implements Cache<K,V> {

    private final ThreadLocal<Map<K,V>> store;

    public ThreadLocalCache(){
        /**
         * ThreadLocal.withInitial(Supplier)直接返回副本
         */
        this.store=ThreadLocal.withInitial(()->new HashMap());
    }

    @Override
    public V get(K key) {
        return store.get().get(key);
    }

    @Override
    public void put(K key, V value) {
        store.get().put(key,value);
    }

    @Override
    public boolean remove(K key) {
        return store.get().remove(key)!=null;
    }

    @Override
    public long size() {
        return store.get().size();
    }

    /**
     * 设置缓存值
     */
    public void setCache(Map<K,V> map){
        store.set(map);
    }

    /**
     * 删除此线程对应的副本
     *             ——必须回收自定义的ThreadLocal变量，尤其在线程池场景下，线程经常会被复用，如果不清理自定义的 ThreadLocal变量，
     *               可能会影响后续业务逻辑和造成内存泄露等问题。尽量在代理中使用try-finally块进行回收
     */
    public void clearCache(){
        store.remove();
    }
}
