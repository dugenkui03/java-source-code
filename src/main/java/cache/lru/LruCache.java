package cache.lru;

import cache.Cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description LRU-cache；thread-unsafe
 * @Date 2018/9/30 下午2:52
 * -
 * @Author dugenkui
 **/

public class LruCache<K,V> implements Cache<K,V> {
    private final Map<K,V> cache;

    public LruCache(final int maxSize){
        cache=new LinkedHashMap<K,V>(maxSize){
            //返回值表示是否进行移除操作，此方法在节点插入操作中被调用，如putVal、compute、computeIfAbsent、merge
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return this.size()>maxSize;
            }
        };
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    public void put(K key, V value) {
        cache.put(key,value);
    }

    @Override
    public boolean remove(K key) {
        //remove返回之前与key绑定的value—如果不为null，表示移除的key对应的valu不为null；
        // 为null，返回false表示没有与key对应的value
        return cache.remove(key)!=null;
    }

    @Override
    public long size() {
        return cache.size();
    }

    @Override
    public void clear() {
        cache.clear();
    }
}
