package cache.sync;

import cache.Cache;

/**
 * @Description 同步缓存
 * @Date 2018/9/30 下午3:14
 * -
 * @Author dugenkui
 **/

public class SyncCache<K,V> implements Cache<K,V> {
    private Cache<K,V> underlying;

    public SyncCache(Cache<K,V> underlying){
        this.underlying=underlying;
    }

    @Override
    public synchronized V get(K key) {
        return underlying.get(key);
    }

    @Override
    public synchronized void put(K key, V value) {
        underlying.put(key,value);
    }

    @Override
    public synchronized boolean remove(K key) {
        return underlying.remove(key);
    }

    @Override
    public synchronized long size() {
        return underlying.size();
    }

    @Override
    public void clear() {
        underlying.clear();
    }
}
