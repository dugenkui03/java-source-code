package cache;

/**
 * @Description 本地缓存实现，参考dubbo、Kafka和caffeine
 * @Date 2018/9/30 下午1:59
 * -
 * @Author dugenkui
 **/

public interface Cache<K,V> {

    V get(K key);

    void put(K key,V value);

    boolean remove(K key);

    long size();

    void clear();
}
