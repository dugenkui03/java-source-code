package cache.expiring;

import cache.AbstractCacheFactory;
import cache.Cache;

import java.util.concurrent.TimeUnit;

/**
 * @Description 有超时检测的缓存，需要制定数据未访问时的存活时间，
 * @Date 2018/10/1 上午2:01
 * -
 * @Author dugenkui
 **/

public class ExpiringMapFactory<K,V> extends AbstractCacheFactory<K,V> {

    private long timeToLive;

    private long checkInterval;

    private TimeUnit timeUnit;

    public ExpiringMapFactory(long timeToLive,long checkInterval,TimeUnit timeUnit){
        this.timeToLive=timeToLive;
        this.checkInterval=checkInterval;
        this.timeUnit=timeUnit;
    }

    /**
     * 子类继承，实现逻辑：返回具体要创建的Cache类
     */
    @Override
    protected Cache<K, V> createCache() {
        return new ExpiringCache<K,V>(timeToLive,checkInterval,timeUnit);
    }
}
