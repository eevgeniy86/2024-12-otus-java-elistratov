package ru.otus.cachehw;

import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    // Надо реализовать эти методы

    private HwListener<K, V> listener = null;
    private final Map<K, V> cache = new WeakHashMap<>();

    @Override
    public void put(K key, V value) {
        callToListener(key, value, "added");
        cache.put(key, value);
    }

    @Override
    public void remove(K key) {
        callToListener(key, cache.get(key), "removed");
        cache.remove(key);
    }

    @Override
    public V get(K key) {
        callToListener(key, cache.get(key), "received");
        return cache.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        this.listener = listener;
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        this.listener = null;
    }

    private void callToListener(K key, V value, String action) {
        if (listener != null) {
            listener.notify(key, value, action);
        }
    }
}
