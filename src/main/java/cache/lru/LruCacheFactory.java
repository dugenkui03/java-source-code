package cache.lru;

import cache.AbstractCacheFactory;
import cache.Cache;

/**
 * @Description 同名称
 * @Date 2018/9/30 下午4:44
 * -
 * @Author dugenkui
 **/

public class LruCacheFactory<K,V> extends AbstractCacheFactory<K,V>{

    private int maxSize;

    public LruCacheFactory(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    protected Cache<K,V> createCache() {
        return new LruCache(maxSize);
    }
}
