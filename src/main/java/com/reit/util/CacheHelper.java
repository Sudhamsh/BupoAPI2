package com.reit.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class CacheHelper<K, V> {

	private Cache<K, V> cache;
	int maxSize = 1000;

	public CacheHelper() {
		cache = CacheBuilder.newBuilder().maximumSize(maxSize).concurrencyLevel(1).build();
	}

	public V fetchVal(K key) {
		V val = cache.getIfPresent(key);
		// TODO: If missing get from DB
		return val;
	}

	public void putVal(K key, V val) {
		cache.put(key, val);

	}

	public void invalidateKey(K key) {
		cache.invalidate(key);
	}

}
