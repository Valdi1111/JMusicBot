package org.valdi.jmusicbot.delivery.webserver.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.commons.lang3.StringUtils;
import org.valdi.jmusicbot.delivery.webserver.response.Response;
import org.valdi.jmusicbot.delivery.webserver.response.data.JSONResponse;
import org.valdi.jmusicbot.file.ResourceCache;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Cache for any JSON data sent via {@link RootJSONHandler}.
 *
 * @author Rsl1122
 */
public class JSONCache {

    private static final Cache<String, String> cache = Caffeine.newBuilder()
            .expireAfterAccess(2, TimeUnit.MINUTES)
            .build();

    private JSONCache() {
        // Static class
    }

    public static Response getOrCache(String identifier, Supplier<JSONResponse> jsonResponseSupplier) {
        String found = cache.getIfPresent(identifier);
        if (found == null) {
            JSONResponse response = jsonResponseSupplier.get();
            cache.put(identifier, response.getContent());
            return response;
        }
        return new JSONResponse(found);
    }

    public static String getOrCacheString(DataID dataID, UUID serverUUID, Supplier<String> stringSupplier) {
        String identifier = dataID.of(serverUUID);
        String found = cache.getIfPresent(identifier);
        if (found == null) {
            String result = stringSupplier.get();
            cache.put(identifier, result);
            return result;
        }
        return found;
    }

    public static Response getOrCache(DataID dataID, Supplier<JSONResponse> jsonResponseSupplier) {
        return getOrCache(dataID.name(), jsonResponseSupplier);
    }

    public static Response getOrCache(DataID dataID, UUID serverUUID, Supplier<JSONResponse> jsonResponseSupplier) {
        return getOrCache(dataID.of(serverUUID), jsonResponseSupplier);
    }

    public static void invalidate(String identifier) {
        cache.invalidate(identifier);
    }

    public static void invalidate(DataID dataID) {
        invalidate(dataID.name());
    }

    public static void invalidate(UUID serverUUID, DataID... dataIDs) {
        for (DataID dataID : dataIDs) {
            invalidate(dataID.of(serverUUID));
        }
    }

    public static void invalidate(DataID dataID, UUID serverUUID) {
        invalidate(dataID.of(serverUUID));
    }

    public static void invalidateMatching(DataID... dataIDs) {
        Set<String> toInvalidate = Arrays.stream(dataIDs)
                .map(DataID::name)
                .collect(Collectors.toSet());
        for (String identifier : cache.asMap().keySet()) {
            for (String identifierToInvalidate : toInvalidate) {
                if (StringUtils.startsWith(identifier, identifierToInvalidate)) {
                    invalidate(identifier);
                }
            }
        }
    }

    public static void invalidateMatching(DataID dataID) {
        String toInvalidate = dataID.name();
        for (String identifier : cache.asMap().keySet()) {
            if (StringUtils.startsWith(identifier, toInvalidate)) {
                invalidate(identifier);
            }
        }
    }

    public static void invalidateAll() {
        cache.invalidateAll();
    }

    public static void cleanUp() {
        cache.cleanUp();
    }

    public static List<String> getCachedIDs() {
        List<String> identifiers = new ArrayList<>(cache.asMap().keySet());
        Collections.sort(identifiers);
        return identifiers;
    }

    public static class CleanTask implements Runnable {

        public CleanTask() {
            // Dagger requires inject constructor
        }

        @Override
        public void run() {
            cleanUp();
            ResourceCache.cleanUp();
        }
    }
}