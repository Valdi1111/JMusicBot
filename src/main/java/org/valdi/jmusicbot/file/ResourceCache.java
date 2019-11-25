package org.valdi.jmusicbot.file;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * In-memory cache for different resources on disk or jar.
 *
 * @author Rsl1122
 */
public class ResourceCache {

    private static final Cache<String, String> cache = Caffeine.newBuilder()
            .expireAfterAccess(1, TimeUnit.MINUTES)
            .build();

    private ResourceCache() {
        // Static class
    }

    public static Resource getOrCache(String resourceName, Supplier<Resource> resourceSupplier) {
        String found = cache.getIfPresent(resourceName);
        if (found == null) {
            return new StringCachingResource(resourceSupplier.get());
        }
        return new StringResource(resourceName, found);
    }

    public static void cache(String resourceName, String got) {
        cache.put(resourceName, got);
    }

    public static void invalidateAll() {
        cache.invalidateAll();
    }

    public static void cleanUp() {
        cache.cleanUp();
    }

    public static List<String> getCachedResourceNames() {
        List<String> resourceNames = new ArrayList<>(cache.asMap().keySet());
        Collections.sort(resourceNames);
        return resourceNames;
    }
}