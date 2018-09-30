package cache.threadlocal;

import cache.AbstractCacheFactory;
import cache.Cache;

/**
 * @Description TODO
 * @Date 2018/9/30 下午10:30
 * -
 * @Author dugenkui
 **/

public class ThreadLocalCacheFactory<K,V> extends AbstractCacheFactory<K,V> {
    /**
     * 子类继承，实现逻辑：返回具体要创建的Cache类
     */
    @Override
    protected Cache<K, V> createCache() {
        return new ThreadLocalCache();
    }
}
