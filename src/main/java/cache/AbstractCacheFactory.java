package cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Description 抽象线程工厂
 * @Date 2018/9/30 下午4:23
 * -
 * @Author dugenkui
 **/

public abstract class AbstractCacheFactory<K,V> implements CacheFactory<K,V>{

    private final ConcurrentMap<String,Cache> caches=new ConcurrentHashMap<>();

    @Override
    public Cache<K,V> getCache(Class type) {
        invalidType(type);

        Cache<K,V> cache=caches.get(type.getSimpleName());
        if(cache==null){
            caches.put(type.getSimpleName(),createCache());
            cache=caches.get(type.getSimpleName());
        }

        return cache;
    }

    /**
     * 子类继承，实现逻辑：返回具体要创建的Cache类
     */
    protected abstract Cache<K,V> createCache();

    private void invalidType(Class type){
         if(!Cache.class.isAssignableFrom(type)){
             throw new RuntimeException("can't find Cache which type is "+type.getSimpleName());
         }
    }

}
