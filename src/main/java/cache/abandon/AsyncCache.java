package cache.abandon;

import cache.Cache;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @Description fixme 嗨，查缓存应该是速度很快的，还用个啥异步任务啊
 * @Date 2018/9/30 下午3:19
 * -
 * @Author dugenkui
 **/

public class AsyncCache<K,V> implements Cache<K,V> {
    private Cache<K,V> underlying;
    private ExecutorService executor;

    public AsyncCache(Cache<K,V> underlying){
        this(underlying, Executors.newCachedThreadPool());
    }

    public AsyncCache(Cache<K,V> underlying, ExecutorService executor){
        this.underlying=underlying;
        this.executor=executor;
    }


    public Future<V> asynGet(K key){
        return executor.submit(()->get(key));
    }

    public void asynPut(K key,V value){
        executor.submit(()->put(key,value));
    }

    public Future<Boolean> asynRemove(K key){
        return executor.submit(()->remove(key));
    }

    public Future<Long> asynSize(){
        return executor.submit(()->size());
    }

    @Override
    public V get(K key){
        return underlying.get(key);
    }

    @Override
    public void put(K key, V value) {
        underlying.put(key,value);
    }

    @Override
    public boolean remove(K key) {
        return underlying.remove(key);
    }

    @Override
    public long size() {
        return underlying.size();
    }

    @Override
    public void clear() {

    }
}
