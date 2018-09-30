package cache;

/**
 * @Description
 * @Date 2018/9/30 下午2:50
 * -
 * @Author dugenkui
 **/

public interface CacheFactory<K,V>{
    Cache<K,V> getCache(Class type);
}
