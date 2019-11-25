package org.valdi.jmusicbot.delivery.domain.container;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Caching layer between Supplier and caller.
 * <p>
 * Refreshes the value if 30 seconds have passed since the last call.
 *
 * @author Rsl1122
 */
public class CachingSupplier<T> implements Supplier<T> {

    private final Supplier<T> original;
    private T cachedValue;
    private long cacheTime;
    private long timeToLive;

    public CachingSupplier(Supplier<T> original) {
        this(original, TimeUnit.SECONDS.toMillis(30L));
    }

    public CachingSupplier(Supplier<T> original, long timeToLive) {
        this.original = original;
        this.timeToLive = timeToLive;

        cacheTime = 0L;
    }

    @Override
    public T get() {
        if (cachedValue == null || System.currentTimeMillis() - cacheTime > timeToLive) {
            cachedValue = original.get();
            cacheTime = System.currentTimeMillis();
        }
        return cachedValue;
    }

    public boolean isCached() {
        return cachedValue != null;
    }

    public long getCacheTime() {
        return cacheTime;
    }
}