package cache.expiring;

import cache.Cache;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description 缓存
 * @Date 2018/10/1 上午1:04
 * -
 * @Author dugenkui
 **/

public class ExpiringCache<K,V> implements Cache<K,V> {
    private final Map<K,V> store;

    public ExpiringCache(long cacheTime,long checkInterval,TimeUnit timeUnit){
        final long timeToLive=timeUnit.toMillis(cacheTime);
        final long intervalTime=timeUnit.toMillis(checkInterval);
        ExpiringMap<K,V> expiringMap=new ExpiringMap(timeToLive,intervalTime);
        expiringMap.getExpireThread().startExpiryIfNotStarted();
        store=expiringMap;
    }

    @Override
    public V get(K key) {
        return store.get(key);
    }

    @Override
    public void put(K key, V value) {
        store.put(key,value);
    }

    @Override
    public boolean remove(K key) {
        return store.remove(key)!=null;
    }

    @Override
    public long size() {
        return store.size();
    }

    @Override
    public void clear() {
        store.clear();
    }
}
