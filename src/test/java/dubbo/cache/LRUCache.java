/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dubbo.cache;

import java.util.LinkedHashMap;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private static final long serialVersionUID = -5167631809472116969L;

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private static final int DEFAULT_MAX_CAPACITY = 1000;

    private final ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock(true);
    private final Lock readLock = rwlock.readLock();
    private final Lock writeLock = rwlock.writeLock();

    private volatile int maxCapacity;

    public LRUCache() {
        this(DEFAULT_MAX_CAPACITY);
    }

    public LRUCache(int maxCapacity) {
        super(16, DEFAULT_LOAD_FACTOR, true);
        this.maxCapacity = maxCapacity;
    }

    @Override
    protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
        return super.size() > maxCapacity;
    }

    @Override
    public boolean containsKey(Object key) {
        readLock.lock();
        try {
            return super.containsKey(key);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public V get(Object key) {
        readLock.lock();
        try {
            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return super.get(key);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public V put(K key, V value) {
        writeLock.lock();
        try {
            return super.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public V remove(Object key) {
        writeLock.lock();
        try {
            return super.remove(key);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public int size() {
        writeLock.lock();
        try {
            return super.size();
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void clear() {
        writeLock.lock();
        try {
            super.clear();
        } finally {
            writeLock.unlock();
        }
    }

    public int getMaxCapacity() {
        readLock.lock();
        try {
            return maxCapacity;
        } finally {
            readLock.unlock();
        }
    }

    public void setMaxCapacity(int maxCapacity) {
        writeLock.lock();
        try {
            this.maxCapacity = maxCapacity;
        } finally {
            writeLock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        LRUCache<String, Integer> cache = new LRUCache<String, Integer>(3);
        cache.put("one",1);

        ExecutorService es = Executors.newCachedThreadPool();
        Callable readTask = ()->cache.get("one");
        Callable writeTask = ()->cache.put("two",2);


        System.out.println(es.submit(readTask).get());//1
        System.out.println(es.submit(readTask).get());//1
        TimeUnit.MILLISECONDS.sleep(10);
        System.out.println(es.submit(writeTask).get());//null
        TimeUnit.MILLISECONDS.sleep(10);
        System.out.println(es.submit(readTask).get());//1
        es.shutdown();
    }
}